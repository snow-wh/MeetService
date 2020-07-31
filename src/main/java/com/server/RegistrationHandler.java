package com.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packRead.ToPacket;

import com.reqest.RegistrationRequest;
import com.response.RegistrationResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

public class RegistrationHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        byte[] finalResponse;

        ObjectMapper objectMapper = new ObjectMapper();

        RegistrationRequest registrationRequest = objectMapper.readValue(httpExchange.getRequestBody(), RegistrationRequest.class);
        DBConnection dbConnection = new DBConnection();
        RegistrationResponse response = new RegistrationResponse();

        if(dbConnection.DBCheck(registrationRequest)==false){
            dbConnection.RegistrationMember(registrationRequest);
            response.setResponse("Регистрация прошла успешно");
        }else {
            response.setResponse("Вы уже зарегистрированы в системе");
        }

        Headers headers = new Headers();
        headers.set("Content-Type","application/json");
        httpExchange.getResponseHeaders().putAll(headers);
        httpExchange.sendResponseHeaders(200,0);

        finalResponse = objectMapper.writeValueAsBytes(response);

        OutputStream out = httpExchange.getResponseBody();
        out.write(finalResponse);
        out.flush();
        out.close();

    }



}
