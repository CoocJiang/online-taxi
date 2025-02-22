package org.example.remote;


import net.sf.json.JSONObject;
import org.example.constant.AmapConfigConstants;
import org.example.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class TrackClient {

    @Value("${amap.key}")
    private  String key;

    @Value("${amap.sid}")
    private String sid;

    public ResponseResult addtrack(String tid){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TRACK_ADD).append("?key="+key)
                .append("&sid="+sid)
                .append("&tid="+tid);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        JSONObject jsonObject = JSONObject.fromObject(stringResponseEntity);
        String trid = jsonObject.getJSONObject("body").getJSONObject("data").getString("trid");
        return ResponseResult.success(trid);
    }



}
