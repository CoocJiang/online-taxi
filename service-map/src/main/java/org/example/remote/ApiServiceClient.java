package org.example.remote;



import net.sf.json.JSONObject;
import org.example.constant.AmapConfigConstants;
import org.example.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ApiServiceClient {

    @Autowired
    RestTemplate resttemplate;

    private final String key = "69d7396ecfe71d97d68624b25266d2ad";

    public ResponseResult addservice(String name){

        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.SERVICE_ADD_URL);
        url.append("?");
        url.append("key="+key);
        url.append("&name="+name);
        System.out.println(url.toString());
        resttemplate = new RestTemplate();
        //postForEntity和postForObject的区别就是前者包含了请求状态码信息等等，后者只包含信息主体
        ResponseEntity<String> stringResponseEntity = resttemplate.postForEntity(url.toString(), null, String.class);
        JSONObject jsonObject = JSONObject.fromObject(stringResponseEntity);
        System.out.println("--------服务添加成功");
        JSONObject data= jsonObject.getJSONObject("body").getJSONObject("data");
        String sid = data.getString("sid");
        return ResponseResult.success(sid);
    }
}
