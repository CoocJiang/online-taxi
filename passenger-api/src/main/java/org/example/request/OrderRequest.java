package org.example.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    // 乘客ID
    private Long passengerId;

    // 乘客手机号
    @NotNull
    private String passengerPhone;

    // 下单行政区域

    private String address;
    // 出发时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departTime;
    // 下单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;
    // 出发地址

    @NotNull
    private String departure;
    // 出发经度
    @NotBlank
    @Pattern(regexp = "^[\\-]?(0?\\d{1,2}\\.\\d{1,6}|1[0-7]\\d{1}\\.\\d{1,6}|180\\.0{1,6})$",message = "请输入正确的经度")
    private String depLongitude;
    // 出发纬度
    @NotBlank
    @Pattern(regexp = "^[\\-]?([1-8]?\\d{1}\\.\\d{1,6}|90\\.\\d{1,6})$",message = "请输入正确的纬度")
    private String depLatitude;
    // 目的地地址


    private String destination;
    // 目的地经度
    @NotBlank
    @Pattern(regexp = "^[\\-]?(0?\\d{1,2}\\.\\d{1,6}|1[0-7]\\d{1}\\.\\d{1,6}|180\\.0{1,6})$",message = "请输入正确的经度")
    private String destLongitude;
    // 目的地纬度
    @NotBlank
    @Pattern(regexp = "^[\\-]?([1-8]?\\d{1}\\.\\d{1,6}|90\\.0{1,6})$",message = "请输入正确的纬度")
    private String destLatitude;
    // 坐标加密标识 1:gcj-02,2:wgs84,3:bd-09,4:cgcs2000北斗 0：其他

    @NotNull
    private Integer encrypt;
    // 运价类型编码
    @NotBlank
    private String fareType;
    // 运价版本
    @NotNull
    private Integer fareVersion;

    // 请求设备唯一码
    @NotBlank
    private String deviceCode;

    /**
     * 司机去接乘客出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toPickUpPassengerTime;

    /**
     * 去接乘客时，司机的经度
     */
    private String toPickUpPassengerLongitude;

    /**
     * 去接乘客时，司机的纬度
     */
    private String toPickUpPassengerLatitude;

    /**
     * 去接乘客时，司机的地点
     */
    private String toPickUpPassengerAddress;

    /**
     * 接到乘客，乘客上车经度
     */
    private String pickUpPassengerLongitude;

    /**
     * 接到乘客，乘客上车纬度
     */
    private String pickUpPassengerLatitude;

    /**
     * 乘客下车经度
     */
    private String passengerGetoffLongitude;

    /**
     * 乘客下车纬度
     */
    private String passengerGetoffLatitude;

    /**
     * 车型
     */
    private String vehicleType;

    /**
     * 价格
     */
    private double price;
    /**
     * 距离
     */
    private Long distance;

}