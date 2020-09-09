package com.server;

import com.sun.net.httpserver.Headers;

public class EntityResponse<T> {

    private final T body;
    private final Headers headers;
    private final String statusCode;


    public EntityResponse(T body, Headers headers, String statusCode) {
        this.body = body;
        this.headers = headers;
        this.statusCode = statusCode;
    }
}
