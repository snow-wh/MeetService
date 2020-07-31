package com.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqest.NewMeetRequest;
import com.reqest.RegistrationRequest;
import com.response.RegistrationResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class NewMeetHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        byte[] finalResponse;

        ObjectMapper objectMapper = new ObjectMapper();
        Headers headers = new Headers();
        NewMeetRequest request = objectMapper.readValue(httpExchange.getRequestBody(), NewMeetRequest.class);
        DBConnection dbConnection = new DBConnection();
        RegistrationResponse response = new RegistrationResponse();

        if (dbConnection.DBMeetCheck(request) == true) {
            response.setResponse(request.getName() + " нельзя назначить встречу на этот день");
        } else {
            dbConnection.NewMeet(request);
            response.setResponse("Встреча с " + request.getName() + " назначена на " + request.getDay() + "." + request.getMonth() + "." + request.getYear());
        }


        headers.set("Content-Type", "application/json");
        httpExchange.getResponseHeaders().putAll(headers);
        httpExchange.sendResponseHeaders(200, 0);

        finalResponse = objectMapper.writeValueAsBytes(response);

        OutputStream out = httpExchange.getResponseBody();
        out.write(finalResponse);
        out.flush();
        out.close();

    }

}
