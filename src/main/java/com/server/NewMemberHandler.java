package com.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqest.NewMeetRequest;
import com.server.user.registration.RegistrationResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class NewMemberHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        byte[] finalResponse;

        ObjectMapper objectMapper = new ObjectMapper();

        NewMeetRequest request = objectMapper.readValue(httpExchange.getRequestBody(), NewMeetRequest.class);
        DBConnection dbConnection = new DBConnection();
        RegistrationResponse response = new RegistrationResponse();

        dbConnection.NewMember(request);
        response.setResponse("Участник "+request.getName()+" успешно удален");


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
