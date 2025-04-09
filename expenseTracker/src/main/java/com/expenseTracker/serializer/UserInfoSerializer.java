package com.expenseTracker.serializer;

import com.expenseTracker.DTO.UserDetailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserInfoSerializer implements Serializer<UserDetailDTO> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, UserDetailDTO data) {
        byte[] retVal=null;
        ObjectMapper objectMapper=new ObjectMapper();
        try{
            retVal = objectMapper.writeValueAsString(data).getBytes();
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }

    @Override
    public void close() {
    }
}
