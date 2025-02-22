package org.example.remote;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.example.constant.AmapConfigConstants;
import org.example.dto.ResponseResult;
import org.example.response.TerminalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;



@Slf4j
@Service
public class TerminalClient {

        @Value("${amap.key}")
    private  String key;

    @Value("${amap.sid}")
    private String sid;

    /**
     * 添加终端
     * @param name
     * @param desc
     * @return
     */
    public ResponseResult addteminal(String name,String desc){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_ADD);
        url.append("?key="+key)
                .append("&sid="+sid)
                .append("&name="+name)
                .append("&desc="+desc);
        log.info("高德apiurl:"+url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        JSONObject jsonObject = JSONObject.fromObject(stringResponseEntity);
        log.info(String.valueOf(jsonObject));
        String tid = jsonObject.getJSONObject("body").getJSONObject("data").getString("tid");
        TerminalResponse terminalResponse= new TerminalResponse();
        terminalResponse.setTid(tid);
        terminalResponse.setCarId(Long.parseLong(desc));
        return ResponseResult.success(terminalResponse);

    }

    public ResponseResult aroundSearch(String center, String radius) {
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_AROUNDSEARCH)
                .append("?key="+key)
                .append("&sid="+sid)
                .append("&center="+center)
                .append("&radius="+radius);
        log.info("高德搜索终端url:"+url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        JSONObject jsonObject = JSONObject.fromObject(stringResponseEntity);
        log.info(String.valueOf(jsonObject));
        JSONObject js = jsonObject.getJSONObject("body").getJSONObject("data");
        if (js.size()==0){
            return ResponseResult.fail(null);
        }
        System.out.println(js);
        JSONArray results = js.getJSONArray("results");
        System.out.println(results);
        if (results==null){
            return ResponseResult.fail(null);
        }
            return ResponseResult.success(results);
    }




    public ResponseResult trsearch(String tid,Long starttime ,Long endtime ){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_TRSEARCH)
                .append("?key="+key)
                .append("&sid="+sid)
                .append("&tid="+tid)
                .append("&starttime="+starttime)
                .append("&endtime="+endtime);
        log.info("查询轨迹url:"+url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        JSONObject jsonObject = JSONObject.fromObject(stringResponseEntity);
        log.info(String.valueOf(jsonObject));
        return null;
    }
}
