package com.root.redis.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonSerializationUtils {

    private JsonSerializationUtils (){

    }

    public static String objectToJson(Object object){
        String value = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            value = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static <T> T jsonToObject(String json, Class<T> contextClass){
        T value = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            value = mapper.readValue(json, contextClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

}
