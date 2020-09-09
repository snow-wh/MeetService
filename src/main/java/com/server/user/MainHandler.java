package com.server.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.DBConnection;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

public abstract class MainHandler {

    private final ObjectMapper objectMapper;


    public MainHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public abstract void execute(HttpExchange exchange);

    protected <T> T readRequest(InputStream is, Class<T> type) throws IOException {
        return objectMapper.readValue(is,type);
    }


    protected <T> T writeResponse(T response) throws JsonProcessingException {
        return (T) objectMapper.writeValueAsBytes(response);
    }

    protected Headers getHeaders(String key, String value){
        Headers headers = new Headers();
        headers.set(key,value);
        return headers;
    }
}
