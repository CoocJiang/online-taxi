package org.example.remote;


import org.example.constant.AmapConfigConstants;
import org.example.dto.ResponseResult;
import org.example.request.PointDTO;
import org.example.request.PointRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class PonitClient {

    @Value("${amap.key}")
    private  String key;

    @Value("${amap.sid}")
    private String sid;

    public ResponseResult upload(PointRequest pointRequest){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.POINT_UPLOAD)
                .append("?key="+key)
                .append("&sid="+sid)
                .append("&tid="+pointRequest.getTid())
                .append("&trid="+pointRequest.getTrid())
                .append("&points=%5B");
        PointDTO [] pointDTOS = pointRequest.getPoints();

        //位置字符串拼接
        for (PointDTO p:pointDTOS){
            url.append("%7B").append("%22location%22%3A"+"%22"+p.getLocation()+"%22"+"%2C")
                    .append("%22locatetime%22%3A"+"%22"+p.getLocatetime()+"%22");

            url.append("%7D");
        }
        url.append("%5D");
        System.out.println(url.toString());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(URI.create(url.toString()), null, String.class);
        System.out.println(stringResponseEntity);
        return ResponseResult.success();
    }
}
