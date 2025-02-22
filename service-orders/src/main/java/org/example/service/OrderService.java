package org.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.aspectj.weaver.ast.Or;
import org.example.constant.CommonStatusEnum;
import org.example.constant.DriverCarConstants;
import org.example.constant.IdentityConstants;
import org.example.constant.OrderConstants;
import org.example.dto.DriverCarBindingRelationship;
import org.example.dto.DriverOrderStatics;
import org.example.dto.OrderInfo;
import org.example.dto.ResponseResult;
import org.example.mapper.DriverOrderStaticsMapper;
import org.example.mapper.OrderInfoMapper;
import org.example.remote.ServiceDriverUserClient;
import org.example.remote.ServiceMapClient;
import org.example.remote.ServicePriceClient;
import org.example.remote.ServicePushClient;
import org.example.request.DriverGrabRequest;
import org.example.request.OrderRequest;
import org.example.response.OrderDriverResponse;
import org.example.utils.RedisPrefixUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class OrderService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    ServicePriceClient servicePriceClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    ServiceMapClient serviceMapClient;

//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

    /**
     * 用户下订单
     * @param orderRequest
     * @return
     */
    public ResponseResult addorder(OrderRequest orderRequest) {


        //判断是否在黑名单中
        if (isblackdevice(orderRequest.getDeviceCode())){
            return ResponseResult.fail(CommonStatusEnum.DEVICE_IS_BLACK.getCode(),
                    CommonStatusEnum.DEVICE_IS_BLACK.getValue());
        }

        //判断当前城市是否开通服务，就是判断是否有计价规则
        boolean flag = (boolean) servicePriceClient.isexistrule(orderRequest.getAddress(),orderRequest.getVehicleType());
        if (!flag){
            return ResponseResult.fail(CommonStatusEnum.CITY_SERVICE_NOT_SERVICE.getCode(),
                    CommonStatusEnum.CITY_SERVICE_NOT_SERVICE.getValue());
        }

        //判断当城市是否有可用司机
        boolean result = serviceDriverUserClient.isAvailableDriver(orderRequest.getAddress());
        if (!result){
            return ResponseResult.fail(CommonStatusEnum.CITY_DRIVER_EMPTY.getCode(),
                    CommonStatusEnum.CITY_DRIVER_EMPTY.getValue());
        }

        //先判断计价规则是否为最新
        ResponseResult responseResult= servicePriceClient.isnewpricerule(orderRequest.getFareType(),
                String.valueOf(orderRequest.getFareVersion()));

//        如果计价规则变化，直接返回失败
        if (responseResult.getData()==null||(boolean)responseResult.getData()==false){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_NOT_EDIT.getCode(),
                    CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
        }


        //判断用户是否有在进行的订单
        if (ispassengerGoingon(orderRequest.getPassengerId())>0){
            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(),
                    CommonStatusEnum.ORDER_GOING_ON.getValue());
        }

        OrderInfo orderInfo  = new OrderInfo();
        BeanUtils.copyProperties(orderRequest,orderInfo);
        //订单创建
        orderInfo.setOrderStatus(OrderConstants.ORDER_START);
        LocalDateTime now = LocalDateTime.now();
        //设置订单创建和修改时间
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);
        orderInfo.setPrice(orderRequest.getPrice());
        orderInfo.setDriveMile(orderRequest.getDistance());
        orderInfoMapper.insert(orderInfo);
        int ans = 0;
        //循环三次，寻找司机是否存在
        for (int i=0;i<3;i++){
             ans = dispatchRealTimeOrder(orderInfo);
            if (ans>0){
                break;
            }else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // 执行其他业务逻辑
        if (ans<1){
            //没找到的话设置订单无效
            orderInfo.setOrderStatus(OrderConstants.ORDER_INVALID);
            orderInfoMapper.updateById(orderInfo);
            return ResponseResult.fail(CommonStatusEnum.FAIL);
        }
        return ResponseResult.success();
    }
//    public void testorder(OrderRequest orderRequest){
//        rocketMQTemplate.send("order-topic", MessageBuilder.withPayload(orderRequest).build());
//    }

    //判断用户是否可以下单
    private int ispassengerGoingon(Long passengerid){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("passenger_id",passengerid)
                .and(wrapper->wrapper.eq("order_status",OrderConstants.ORDER_START)
                        .or().eq("order_status",OrderConstants.DRIVER_RECEIVE_ORDER)
                        .or().eq("order_status",OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                        .or().eq("order_status",OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                        .or().eq("order_status",OrderConstants.PICK_UP_PASSENGER)
                        .or().eq("order_status",OrderConstants.PASSENGER_GETOFF)
                        .or().eq("order_status",OrderConstants.TO_START_PAY));

        List<OrderInfo> orderInfos = orderInfoMapper.selectList(queryWrapper);

        return orderInfos.size();
    }


    //判断用户是否是黑名单
    private boolean isblackdevice(String deviceCode){
        String deviceCodekey = RedisPrefixUtils.blackDeviceCodePrefix + deviceCode;
       boolean flag = Boolean.TRUE.equals(stringRedisTemplate.hasKey(deviceCodekey));
        if (flag){
            String s = stringRedisTemplate.opsForValue().get(deviceCodekey);
            Integer time  = Integer.parseInt(s);
            time++;
            if (Integer.parseInt(s)>2){
                stringRedisTemplate.opsForValue().set(deviceCodekey, String.valueOf(time),1,TimeUnit.HOURS);
                return true;
            }else {
                return false;
            }
        }else {
           return false;
        }
    }


    @Autowired
    RedissonClient redissonClient;


    @Autowired
    ServicePushClient servicePushClient;


    @Autowired
    DriverOrderStaticsMapper driverOrderStaticsMapper;


    /**
     * 实时派送订单
     * @param orderInfo
     * @return
     */
    public int dispatchRealTimeOrder(OrderInfo orderInfo){
        int ans = 0;
        //获取乘客纬度
        String depLatitude = orderInfo.getDepLatitude();
        //获取乘客经度
        String depLongitude = orderInfo.getDepLongitude();
        String center = depLongitude+","+depLatitude;

        List<String> radiuslist = new ArrayList<>();
        radiuslist.add("2000");
        radiuslist.add("4000");
        radiuslist.add("5000");
        for (int i = 0;i<radiuslist.size();i++){
          ResponseResult   result  =  serviceMapClient.aroundSearch(center, radiuslist.get(i));
          JSONObject jsonObject = JSONObject.fromObject(result);
          if (result.getData()!=null){
                JSONArray data = jsonObject.getJSONArray("data");
              log.info(String.valueOf("data:----"+data));
              for (Object Object:data){
                  Long carId = Long.parseLong(JSONObject.fromObject(Object).getString("desc"));
                  JSONObject location = JSONObject.fromObject(Object).getJSONObject("location");
                  //获取司机纬度和经度
                  String latitude = location.getString("latitude");
                  String longitude = location.getString("longitude");
                  //查询可接单的司机
                  ResponseResult<OrderDriverResponse> driverResult =
                        serviceDriverUserClient.getAvailableDriver(carId);
                if (driverResult.getCode()== CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode()){
                    continue;
                }
                //获取司机id
                  log.info("driverresult:----"+String.valueOf(driverResult));
                  JSONObject driverresult  = JSONObject.fromObject(driverResult).getJSONObject("data");
                  Long driverid = driverresult.getLong("driverId");

                  String lockkey = (driverid+"").intern();
                  // intern方法把字符串放到常量池内,使用这种方式只适合于单机的并发，分布式并发不适合

//                  synchronized(lockkey)){
                  RLock lock = redissonClient.getLock(lockkey);
                  lock.lock();
                  //根据id判断司机是否有进行中的订单
                  int driverGoingon = isDriverGoingon(driverid);
                  if(driverGoingon>0){
                      log.info("没有找到司机");
                      return ans;
                  }
                  //没有问题再获取司机详细信息
                  String driverPhone = driverresult.getString("driverPhone");
                  String licenseId = driverresult.getString("licenseId");
                  String vehicleNo = driverresult.getString("vehicleNo");
                  String vehicleType = driverresult.getString("vehicleType");
                  //获取司机经纬度
                  if (driverGoingon >  0){
                      lock.unlock();
                      log.info("没有找到司机");
                      continue;
                  }else {
//                      设置订单信息
                      orderInfo.setDriverId(driverid);
                      orderInfo.setDriverPhone(driverPhone);
                      orderInfo.setCarId(carId);
                      orderInfo.setVehicleType(vehicleType);
                      orderInfo.setReceiveOrderCarLongitude(longitude);
                      orderInfo.setReceiveOrderCarLatitude(latitude);
                      orderInfo.setReceiveOrderTime(LocalDateTime.now());
                      orderInfo.setLicenseId(licenseId);
                      orderInfo.setVehicleNo(vehicleNo);
                      orderInfo.setOrderStatus(2);

                      orderInfoMapper.updateById(orderInfo);

                      //通知司机 乘客的信息
                      JSONObject paasengerMessage = new JSONObject();
                      paasengerMessage.put("passengerId",orderInfo.getPassengerId());
                      paasengerMessage.put("passengerPhone",orderInfo.getPassengerPhone());
                      paasengerMessage.put("departure",orderInfo.getDeparture());
                      paasengerMessage.put("destination",orderInfo.getDestination());
                      paasengerMessage.put("orderId",orderInfo.getId());
                      servicePushClient.push(String.valueOf(driverid), IdentityConstants.DRIVER_IDENTITY,paasengerMessage.toString());

                      //通知乘客 司机的信息
                      JSONObject driverMessage = new JSONObject();
                      driverMessage.put("driverId",driverid);
                      driverMessage.put("passengerPhone",driverPhone);
                      driverMessage.put("departure",orderInfo.getToPickUpPassengerAddress());
                      driverMessage.put("destination",orderInfo.getDestination());
                      servicePushClient.push(String.valueOf(orderInfo.getPassengerId()), IdentityConstants.PASSENGER_IDENTITY,driverMessage.toString());
                      log.info("找到司机:"+driverid);
                      ans =1;
                      i=radiuslist.size();
                      lock.unlock();
                      break;
                  }
              }
            }else {
              log.info("没有找到司机");
          }
        }
       return ans;
    }

    /**
     * 预约派送订单,分发订单给司机
     * @param orderInfo
     * @return
     */
    public int dispatchBookOrder(OrderInfo orderInfo){
        int ans = 0;
        //获取乘客纬度
        String depLatitude = orderInfo.getDepLatitude();
        //获取乘客经度
        String depLongitude = orderInfo.getDepLongitude();
        String center = depLongitude+","+depLatitude;
        //业务代码 为了方便测试 暂时注释
//        List<String> radiuslist = new ArrayList<>();
//        radiuslist.add("2000");
//        radiuslist.add("4000");
//        radiuslist.add("5000");
//        for (int i = 0;i<radiuslist.size();i++){
//            ResponseResult   result  =  serviceMapClient.aroundSearch(center, radiuslist.get(i));
//            JSONObject jsonObject = JSONObject.fromObject(result);
//            if (result.getData()!=null){
//                JSONArray data = jsonObject.getJSONArray("data");
//                log.info(String.valueOf("data:----"+data));
//                for (Object Object:data){
//                    Long carId = Long.parseLong(JSONObject.fromObject(Object).getString("desc"));
//                    JSONObject location = JSONObject.fromObject(Object).getJSONObject("location");
//                    //获取司机纬度和经度
//                    String latitude = location.getString("latitude");
//                    String longitude = location.getString("longitude");

                    // {-------以下是测试代码，数据写死，方便测试
                    Long carId = Long.parseLong("1821575168016388097");
                    //获取司机纬度和经度
                    String latitude = "latitude";
                    String longitude = "116.408969";
                    //查询可接单的司机
                    ResponseResult<OrderDriverResponse> driverResult =
                            serviceDriverUserClient.getAvailableDriver(carId);
                    if (driverResult.getCode()== CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode()){
                       return 0;
                    }
                    log.info("driverresult:----"+String.valueOf(driverResult));
                    JSONObject driverresult  = JSONObject.fromObject(driverResult).getJSONObject("data");
                    Long driverid = driverresult.getLong("driverId");
                    if(isDriverGoingon(driverid)>0){
                        return 0;
                    }
                    // ------以上代码用于方便测试    }


                    //查询可接单的司机
//                    ResponseResult<OrderDriverResponse> driverResult =
//                            serviceDriverUserClient.getAvailableDriver(carId);
//                    if (driverResult.getCode()== CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode()){
//                        continue;
//                    }
//                    //获取司机id
//                    log.info("driverresult:----"+String.valueOf(driverResult));
//                    JSONObject driverresult  = JSONObject.fromObject(driverResult).getJSONObject("data");
//                    Long driverid = driverresult.getLong("driverId");

                    //根据id判断司机是否有进行中的订单
//                    if(isDriverGoingon(driverid)>0){
//                       continue;
//                    }
                    //没有问题再获取司机详细信息
                    String driverPhone = driverresult.getString("driverPhone");
                    String licenseId = driverresult.getString("licenseId");
                    String vehicleNo = driverresult.getString("vehicleNo");
                    String vehicleType = driverresult.getString("vehicleType");
                    //获取司机经纬度
                    //设置订单信息
                    orderInfo.setDriverId(driverid);
                    orderInfo.setDriverPhone(driverPhone);
                    orderInfo.setCarId(carId);
                    orderInfo.setVehicleType(vehicleType);
                    orderInfo.setReceiveOrderCarLongitude(longitude);
                    orderInfo.setReceiveOrderCarLatitude(latitude);
                    orderInfo.setReceiveOrderTime(LocalDateTime.now());
                    orderInfo.setLicenseId(licenseId);
                    orderInfo.setVehicleNo(vehicleNo);

                    orderInfoMapper.updateById(orderInfo);

                    //通知司机 乘客的信息
                    JSONObject paasengerMessage = new JSONObject();
                    paasengerMessage.put("passengerId",orderInfo.getPassengerId());
                    paasengerMessage.put("passengerPhone",orderInfo.getPassengerPhone());
                    paasengerMessage.put("departure",orderInfo.getDeparture());
                    paasengerMessage.put("destination",orderInfo.getDestination());
                    paasengerMessage.put("orderId",orderInfo.getId());
                    servicePushClient.push(String.valueOf(driverid), IdentityConstants.DRIVER_IDENTITY,
                            paasengerMessage.toString());
                    log.info("找到司机:"+driverid);
                    ans =1;
//                    i=radiuslist.size();
//                    break;
//                }
//            }else {
//                log.info("没有找到司机");
//            }
//        }
        return ans;
    }

    /**
     * 判断用户司机此时有无订单
     * @param driverid
     * @return
     */
    private int isDriverGoingon(Long driverid){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverid)
                .and(wrapper->wrapper.eq("order_status",OrderConstants.DRIVER_RECEIVE_ORDER)
                        .or().eq("order_status",OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                        .or().eq("order_status",OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                        .or().eq("order_status",OrderConstants.PICK_UP_PASSENGER));

        List<OrderInfo> orderInfos = orderInfoMapper.selectList(queryWrapper);
        log.info("司机正在进行中的订单数量为"+orderInfos.size());
        return orderInfos.size();
    }


    /**
     * 司机出发去接乘客修改订单状态
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest) {

        Long orderid  = orderRequest.getOrderId();

        LocalDateTime topickUpPassengerTime = orderRequest.getToPickUpPassengerTime();
        String topickUpPassengerLatitude = orderRequest.getToPickUpPassengerLatitude();
        String topickUpPassengerLongitude = orderRequest.getToPickUpPassengerLongitude();
        String toPickUpPassengerAddress = orderRequest.getToPickUpPassengerAddress();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderid);

        //数据库查出对应的订单数据
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        //修改订单数据
        orderInfo.setToPickUpPassengerLongitude(topickUpPassengerLongitude);
        orderInfo.setToPickUpPassengerLatitude(topickUpPassengerLatitude);
        orderInfo.setToPickUpPassengerTime(topickUpPassengerTime);
        orderInfo.setToPickUpPassengerAddress(toPickUpPassengerAddress);
        orderInfo.setOrderStatus(OrderConstants.DRIVER_TO_PICK_UP_PASSENGER);
        servicePushClient.push(String.valueOf(orderInfo.getPassengerId()),IdentityConstants.PASSENGER_IDENTITY,"司机已经出发");

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }

    /**
     * 到达到乘客目的地修改订单
     * @param orderRequest
     * @return
     */
    public ResponseResult ArriveDepature(OrderRequest orderRequest) {

        Long orderid  = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderid);

        //数据库查出对应的订单数据
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        orderInfo.setOrderStatus(OrderConstants.DRIVER_ARRIVED_DEPARTURE);
        orderInfo.setDriverArrivedDepartureTime(LocalDateTime.now());

        int i = orderInfoMapper.updateById(orderInfo);
        if (i>0){
            servicePushClient.push(String.valueOf(orderInfo.getPassengerId()),IdentityConstants.PASSENGER_IDENTITY,"司机已经抵达目的地");
            return ResponseResult.success();
        }else {
            return ResponseResult.fail(CommonStatusEnum.FAIL);
        }
    }


    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger(OrderRequest orderRequest) {

        Long orderid  = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderid);

        //数据库查出对应的订单数据
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        orderInfo.setPickUpPassengerTime(LocalDateTime.now());
        orderInfo.setPickUpPassengerLatitude(orderRequest.getPickUpPassengerLatitude());
        orderInfo.setPickUpPassengerLongitude(orderRequest.getPickUpPassengerLongitude());
        orderInfo.setOrderStatus(OrderConstants.PICK_UP_PASSENGER);
        servicePushClient.push(String.valueOf(orderInfo.getDriverId()),IdentityConstants.DRIVER_IDENTITY,"乘客已经上车");
        int i = orderInfoMapper.updateById(orderInfo);
        if (i>0){
            return ResponseResult.success();
        }else {
            return ResponseResult.fail(CommonStatusEnum.FAIL);
        }
    }



    /**
     * 到达目的地
     * @param orderRequest
     * @return
     */
    public ResponseResult passengerGetOff(OrderRequest orderRequest) {

        Long orderid  = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderid);

        //数据库查出对应的订单数据
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        orderInfo.setPassengerGetoffTime(LocalDateTime.now());
        orderInfo.setPassengerGetoffLatitude(orderRequest.getPassengerGetoffLatitude());
        orderInfo.setPassengerGetoffLongitude(orderRequest.getPassengerGetoffLongitude());

        orderInfo.setOrderStatus(OrderConstants.PASSENGER_GETOFF);
        servicePushClient.push(String.valueOf(orderInfo.getPassengerId()),IdentityConstants.PASSENGER_IDENTITY,"目的地抵达");
        servicePushClient.push(String.valueOf(orderInfo.getDriverId()),IdentityConstants.DRIVER_IDENTITY,"目的地抵达");

        int i = orderInfoMapper.updateById(orderInfo);
        if (i>0){
            return ResponseResult.success();
        }else {
            return ResponseResult.fail(CommonStatusEnum.FAIL);
        }
    }

    /**
     * 司机发起收款
     * @param OrderId
     * @return
     */
    public ResponseResult toStartPay(@RequestParam("OrderId") String OrderId){

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",OrderId);

        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        orderInfo.setOrderStatus(OrderConstants.TO_START_PAY);
        int i = orderInfoMapper.updateById(orderInfo);
        servicePushClient.push(String.valueOf(orderInfo.getPassengerId()),IdentityConstants.PASSENGER_IDENTITY,"价格为："+orderInfo.getPrice()+"点击下方链接支付");
        servicePushClient.pushprice(String.valueOf(orderInfo.getPassengerId()),IdentityConstants.PASSENGER_IDENTITY,String.valueOf(orderInfo.getPrice()), String.valueOf(orderInfo.getId()));
        if (i>0){
            return ResponseResult.success(orderInfo);
        }else {
            return ResponseResult.fail(CommonStatusEnum.FAIL);
        }
    }


    /**
     * 取消订单
     * @param orderId
     * @param identity
     * @return
     */
    public ResponseResult cancel(Long orderId, String identity) {

        //获取订单
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);

        LocalDateTime canceltime = LocalDateTime.now();
        orderInfo.setCancelTime(canceltime);

        Integer orderStatus = orderInfo.getOrderStatus();

        if (identity.trim().equals(IdentityConstants.PASSENGER_IDENTITY)){
            //如果是乘客取消的
            orderInfo.setCancelOperator(Integer.valueOf(IdentityConstants.PASSENGER_IDENTITY));
            if (orderStatus==OrderConstants.ORDER_START||orderStatus==OrderConstants.DRIVER_RECEIVE_ORDER){
                //司机还未接单 or 司机已经接单
                orderInfo.setCancelTypeCode(OrderConstants.CANCEL_PASSENGER_BEFORE);
                orderInfo.setOrderStatus(OrderConstants.ORDER_CANCEL);
            } else if (orderStatus==OrderConstants.DRIVER_TO_PICK_UP_PASSENGER||orderStatus==OrderConstants.DRIVER_ARRIVED_DEPARTURE) {
                //司机出发去接乘客
                //判断时间是否大于1分钟，大于则视为乘客违约
            LocalDateTime toPickUpPassengerTime = orderInfo.getToPickUpPassengerTime();
            Duration duration = Duration.between(canceltime,toPickUpPassengerTime).abs();
            long minutes = duration.toMinutes();
            log.info(duration.toString());
            if (minutes > 1) {
                // 如果时间差大于1分钟，视为乘客违约
                orderInfo.setCancelTypeCode(OrderConstants.CANCEL_PASSENGER_ILLEGAL);
            }else {
                //视为正常取消
                orderInfo.setCancelTypeCode(OrderConstants.CANCEL_PASSENGER_BEFORE);
            }
                orderInfo.setOrderStatus(OrderConstants.ORDER_CANCEL);
        }else {
                //不可以取消
                return ResponseResult.fail(CommonStatusEnum.ORDER_CANCEL_ERROR.getCode(),CommonStatusEnum.ORDER_CANCEL_ERROR.getValue());
            }
        }else {
            //如果是司机取消的
            orderInfo.setCancelOperator(Integer.valueOf(IdentityConstants.DRIVER_IDENTITY));
             if (orderStatus==OrderConstants.DRIVER_RECEIVE_ORDER){
                //司机已经接单
                 orderInfo.setCancelTypeCode(OrderConstants.CANCEL_DRIVER_BEFORE);
                 orderInfo.setOrderStatus(OrderConstants.ORDER_CANCEL);
            } else if (orderStatus==OrderConstants.DRIVER_TO_PICK_UP_PASSENGER) {
                //司机出发去接乘客
                 LocalDateTime receiveOrderTime = orderInfo.getReceiveOrderTime();
                 Duration duration = Duration.between(canceltime, receiveOrderTime).abs();
                 long minutes = duration.toMinutes();
                 if (minutes > 1) {
                     // 如果时间差大于1分钟，视为乘客违约
                     orderInfo.setCancelTypeCode(OrderConstants.CANCEL_DRIVER_BEFORE);
                 }else {
                     //视为正常取消
                     orderInfo.setCancelTypeCode(OrderConstants.CANCEL_DRIVER_ILLEGAL);
                 }
                 orderInfo.setOrderStatus(OrderConstants.ORDER_CANCEL);
            }else if(orderStatus==OrderConstants.DRIVER_ARRIVED_DEPARTURE){
                //司机到达出发地视为违约取消
                 orderInfo.setCancelTypeCode(OrderConstants.CANCEL_DRIVER_ILLEGAL);
                 orderInfo.setOrderStatus(OrderConstants.ORDER_CANCEL);
            }else {
                return ResponseResult.fail(CommonStatusEnum.ORDER_CANCEL_ERROR.getCode(),CommonStatusEnum.ORDER_CANCEL_ERROR.getValue());
            }
        }
        int i = orderInfoMapper.updateById(orderInfo);
        if (i>0){
            return ResponseResult.success();
        }else {
            return ResponseResult.fail(CommonStatusEnum.ORDER_CANCEL_ERROR.getCode(),CommonStatusEnum.ORDER_CANCEL_ERROR.getValue()) ;
        }
    }


    /**
     * 乘客支付成功
     * @param orderId
     * @return
     */
    public ResponseResult payFare(String orderId){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);

        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setOrderStatus(OrderConstants.SUCCESS_PAY);
        int i = orderInfoMapper.updateById(orderInfo);
        servicePushClient.push(String.valueOf(orderInfo.getDriverId()),IdentityConstants.DRIVER_IDENTITY,"支付成功");
        if (i>0){
            return ResponseResult.success(orderInfo);
        }else {
            return ResponseResult.fail(CommonStatusEnum.FAIL);
        }
    }

    /**
     * 乘客预约订单
     * @param orderRequest
     * @return
     */
    public ResponseResult book(OrderRequest orderRequest) {

        OrderInfo orderInfo  = new OrderInfo();
        BeanUtils.copyProperties(orderRequest,orderInfo);
        //订单创建
        orderInfo.setOrderStatus(OrderConstants.ORDER_START);
        LocalDateTime now = LocalDateTime.now();
        //设置订单创建和修改时间
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);
        orderInfo.setPrice(orderRequest.getPrice());
        orderInfo.setDriveMile(orderRequest.getDistance());
        orderInfoMapper.insert(orderInfo);
        int ans = 0;
        //循环三次，寻找司机是否存在
        for (int i=0;i<3;i++){
            ans = dispatchBookOrder(orderInfo);
            if (ans>0){
                break;
            }else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // 执行其他业务逻辑
        if (ans<1){
            //没找到的话设置订单无效
            orderInfo.setOrderStatus(OrderConstants.ORDER_INVALID);
            orderInfoMapper.updateById(orderInfo);
            return ResponseResult.fail(CommonStatusEnum.FAIL);
        }
        return ResponseResult.success();
    }


    /**
     * 司机抢订单
     * @param driverGrabRequest
     */
    @Transactional
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        Long orderId = driverGrabRequest.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        if (orderInfo==null){
            return ResponseResult.fail(CommonStatusEnum.ORDER_NOT_EXIST.getCode(),CommonStatusEnum.ORDER_NOT_EXIST.getValue());
        }
        Integer orderStatus = orderInfo.getOrderStatus();
        if (orderStatus!=OrderConstants.ORDER_START){
            return ResponseResult.fail(CommonStatusEnum.ORDER_GRABING.getCode(),CommonStatusEnum.ORDER_GRABING.getValue());
        }
        Long driverId = driverGrabRequest.getDriverId();
        String driverPhone = driverGrabRequest.getDriverPhone();
        String licenseId = driverGrabRequest.getLicenseId();
        Long carId = driverGrabRequest.getCarId();
        String vehicleType = driverGrabRequest.getVehicleType();
        String receiveOrderCarLatitude = driverGrabRequest.getReceiveOrderCarLatitude();
        String receiveOrderCarLongitude = driverGrabRequest.getReceiveOrderCarLongitude();

        //设置订单信息
        orderInfo.setDriverId(driverId);
        orderInfo.setDriverPhone(driverPhone);
        orderInfo.setLicenseId(licenseId);
        orderInfo.setVehicleType(vehicleType);
        orderInfo.setCarId(carId);
        orderInfo.setReceiveOrderCarLatitude(receiveOrderCarLatitude);
        orderInfo.setReceiveOrderCarLongitude(receiveOrderCarLongitude);
        orderInfo.setReceiveOrderTime(LocalDateTime.now());
        orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);
        //更新订单
        int i = orderInfoMapper.updateById(orderInfo);
        //同时更新司机接单表的中的司机接单数量
        QueryWrapper<DriverOrderStatics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverId);
        DriverOrderStatics driverOrderStatics = driverOrderStaticsMapper.selectOne(queryWrapper);
        //int is = 1/0;
        if (driverOrderStatics==null){
             driverOrderStatics = new DriverOrderStatics();
             driverOrderStatics.setDriverId(driverId);
             driverOrderStatics.setGrabOrderSuccessCount(1L);
             driverOrderStatics.setGrab_order_date(LocalDateTime.now());
             driverOrderStaticsMapper.insert(driverOrderStatics);
        }else {
            driverOrderStatics.setGrabOrderSuccessCount(driverOrderStatics.getGrabOrderSuccessCount()+1);
            driverOrderStaticsMapper.updateById(driverOrderStatics);
        }
        if (i>0){
            return ResponseResult.success();
        }else {
            return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(),CommonStatusEnum.FAIL.getValue());
        }
    }
}
