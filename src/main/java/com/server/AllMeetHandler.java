package com.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.user.registration.RegistrationRequest;
import com.server.user.registration.RegistrationResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class AllMeetHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        byte[] finalResponse;

        ObjectMapper objectMapper = new ObjectMapper();

        RegistrationRequest request = objectMapper.readValue(httpExchange.getRequestBody(), RegistrationRequest.class);
        DBConnection dbConnection = new DBConnection();
        RegistrationResponse response = new RegistrationResponse();

        Headers headers = new Headers();
        headers.set("Content-Type","application/json");
        httpExchange.getResponseHeaders().putAll(headers);
        httpExchange.sendResponseHeaders(200,0);
        OutputStream out = httpExchange.getResponseBody();
        if(dbConnection.AllMeet(request).isEmpty()){
            response.setResponse("Встречь не назначено");
            finalResponse = objectMapper.writeValueAsBytes(response);
            out.write(finalResponse);
            out.flush();
        }else {
            for (int i = 0; i < dbConnection.AllMeet(request).size() ; i++) {

                finalResponse = objectMapper.writeValueAsBytes(dbConnection.AllMeet(request).get(i));
                out.write(finalResponse);
                out.write(", ".getBytes());
                out.flush();
            }

        }

        out.close();

    }
}