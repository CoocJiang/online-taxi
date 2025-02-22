package org.example.remote;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.example.constant.AmapConfigConstants;
import org.example.response.DirectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class MapDirectionClient {
    private final String key = "69d7396ecfe71d97d68624b25266d2ad";

    public DirectionResponse direction(String depLongitude, String depLatitude, String destLongitude, String destLatitude){
        //https://restapi.amap.com/v5/direction/driving?isindoor=0&origin=116.466485,39.995197&
        // destination=116.46424,40.020642&key=69d7396ecfe71d97d68624b25266d2ad
        StringBuilder url = new StringBuilder();
        url.append("https://restapi.amap.com/v3/direction/driving");
        url.append("?origin=").append(depLongitude).append(",").append(depLatitude).append("&");//加上经度，纬度
        url.append("destination=").append(destLongitude).append(",").append(destLatitude).append("&");
        url.append("key="+key);
        System.out.println("目标url:"+url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> direction = restTemplate.getForEntity(url.toString(), String.class);
        log.info(direction.getBody());
        return parse(direction.getBody());
    }



    public static DirectionResponse parse(String directions){
        JSONObject result = JSONObject.fromObject(directions);
        DirectionResponse directionResponse = null;
        if (result==null){
            return null;
        }
            directionResponse = new DirectionResponse();
            if(1==result.getInt(AmapConfigConstants.STATUS)){
                JSONObject routeobject = result.getJSONObject(AmapConfigConstants.ROUTE);
                JSONArray patharray = routeobject.getJSONArray(AmapConfigConstants.PATHS);
                int distance = patharray.getJSONObject(0).getInt(AmapConfigConstants.DISTANCE);
                int duration = patharray.getJSONObject(0).getInt("duration");
                int price = routeobject.getInt("taxi_cost");
                System.out.println(duration);
                System.out.println(distance);
                directionResponse.setDuration(duration);
                directionResponse.setDistance(distance);
                directionResponse.setPrice(price);
            }
            return directionResponse;
    }

}
