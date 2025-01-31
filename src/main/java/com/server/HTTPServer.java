package com.server;

import com.server.user.registration.RegistrationHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HTTPServer {

    private AuthenticationHandler authenticationHandler = new AuthenticationHandler();

    public static void main(String[] args) {


        try {
            HTTPServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void run() throws IOException {



        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8000), 0);





        server.createContext("/registration", new RegistrationHandler());
        server.createContext("/authentication", new AuthenticationHandler());
        server.createContext("/new_meet", new NewMeetHandler());
        server.createContext("/all_meet", new AllMeetHandler());
        server.createContext("/cancel_meet", new CancelMeetHandler());
        server.createContext("/new_member", new NewMemberHandler());
        server.createContext("/delete_member", new RegistrationHandler());
        /*server.createContext("/all_meet_and_member" , new RegistrationHandler());*/

        server.setExecutor(null);
        server.start();
        System.out.println(" Server started on port 8000");

    }



}
