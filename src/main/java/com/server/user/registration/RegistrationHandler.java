package com.server.user.registration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.server.Constants;
import com.server.DBConnection;
import com.server.EntityResponse;
import com.server.StatusCode;
import com.server.user.registration.RegistrationRequest;
import com.server.user.registration.RegistrationResponse;
import com.server.user.MainHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.server.Constants.REGISTRATION_FALSE_RESPONSE;
import static com.server.Constants.REGISTRATION_TRUE_RESPONSE;

public class RegistrationHandler extends MainHandler {


    public RegistrationHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }


    @Override
    public void execute(HttpExchange exchange) {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())){

        }
    }

    private EntityResponse<RegistrationResponse> makeResponse(InputStream is) throws IOException {
            RegistrationRequest registrationRequest = super.readRequest(is,RegistrationRequest.class);
            RegistrationResponse registrationResponse = new RegistrationResponse();
            if (DBConnection.getInstance().DBCheck(registrationRequest)==false){
                DBConnection.getInstance().RegistrationMember(registrationRequest);
                registrationResponse.setResponse(registrationRequest.getLogin()+ REGISTRATION_TRUE_RESPONSE);
            }else {
                registrationResponse.setResponse(REGISTRATION_FALSE_RESPONSE);
            }
        return new EntityResponse<>(registrationResponse,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    public void handle(HttpExchange httpExchange) throws IOException {


        byte[] finalResponse;

        ObjectMapper objectMapper = new ObjectMapper();

        RegistrationRequest registrationRequest = objectMapper.readValue(httpExchange.getRequestBody(), RegistrationRequest.class);
       // DBConnection dbConnection = new DBConnection();
        RegistrationResponse response = new RegistrationResponse();
        if (DBConnection.getInstance().DBCheck())
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
