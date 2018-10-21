package com.example.documentApp.resources;

import com.example.documentApp.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.client.Entity;
import java.util.Base64;

public class HTTPHelper {



    private static ObjectMapper objectMapper =  new ObjectMapper();

    public static Response post(ResourceTestRule resource, String url, Entity json) throws Exception{
        javax.ws.rs.core.Response httpResponse = resource.target(url).request().post(json);
        String jsonString = httpResponse.readEntity(String.class);
        return objectMapper.readValue(jsonString, Response.class);
    }

    public static Response post(ResourceTestRule resource, String url, Entity json, String username, String password) throws Exception{
        String encodedUsernamePassword = base64Encode(username+":"+password);
        javax.ws.rs.core.Response httpResponse = resource.target(url).request().header("Authorization", "Basic " + encodedUsernamePassword).post(json);
        String jsonString = httpResponse.readEntity(String.class);
        return objectMapper.readValue(jsonString, Response.class);
    }

    public static Response get(ResourceTestRule resource, String url, String username, String password) throws Exception{
        String encodedUsernamePassword = base64Encode(username+":"+password);
        javax.ws.rs.core.Response httpResponse = resource.target(url).request().header("Authorization", "Basic " + encodedUsernamePassword).get();
        String jsonString = httpResponse.readEntity(String.class);
        return objectMapper.readValue(jsonString, Response.class);
    }

    private static String base64Encode(String s){
        byte[] bytesEncoded = Base64.getEncoder().encode(s.getBytes());
        return new String(bytesEncoded);
    }
}
