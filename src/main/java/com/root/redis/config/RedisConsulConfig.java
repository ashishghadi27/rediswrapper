package com.root.redis.config;

import com.root.redis.vo.ConnectionDataVO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RedisConsulConfig {

    @Value("${redis-connection-properties}")
    private String redisConfigStr;

    public List<ConnectionDataVO> getRedisConnectionHostPort(){
        List<ConnectionDataVO> connectionDataList= new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(redisConfigStr);
            JSONArray jsonArray = jsonObject.getJSONArray("connectionList");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String host = object.optString("host");
                String port = object.optString("port");
                ConnectionDataVO connectionDataVO = new ConnectionDataVO(host, port);
                connectionDataList.add(connectionDataVO);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return connectionDataList;
    }

    public boolean hasMultipleClusters(){
        try {
            JSONObject jsonObject = new JSONObject(redisConfigStr);
            return jsonObject.optBoolean("hasMultipleClusters");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }


}
