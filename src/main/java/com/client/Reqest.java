package com.client;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.packRead.ToPacket;

import com.reqest.NewMeetRequest;
import com.reqest.RegistrationRequest;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class Reqest {


    private static String responseFinal;

        private final String USER__AGENT = "Mozilla/5.0";

        public static void main(String[]args) throws Exception {

            Reqest http = new Reqest();

            ObjectMapper objectMapper = new ObjectMapper();

            int esc = 9999;
            Scanner in = new Scanner(System.in);
            System.out.print("Input a number: ");
            String login="";
            while (esc!=0){
                System.out.println("Наберите для родолжения ");
                System.out.println("1-Регистрация");
                System.out.println("2-Аутентификация");
                System.out.println("3-Назначение встречи");
                System.out.println("4-Удаление встречи");
                System.out.println("0-Выход");
                switch (in.nextInt()){
                    case 0 : esc=0;
                        break;
                    case 1 : in.nextLine();
                        System.out.println("Введите логин и пароль");
                        http.sendPost("/registration",objectMapper.writeValueAsBytes(registration(in.nextLine(),in.nextLine())));
                        System.out.println(responseFinal);
                        break;
                    case 2: in.nextLine();
                        System.out.println("Введите логин и пароль");
                        login=in.nextLine();
                        http.sendPost("/authentication",objectMapper.writeValueAsBytes(registration(login,in.nextLine())));
                        System.out.println(responseFinal);
                        break;
                    case 3:
                        if (!login.equals("")){
                        System.out.println("Введите Имя того, с кем вы назначили встречу, год, месяц(числом) и день ");
                        in.nextLine();
                        http.sendPost("/new_meet",objectMapper.writeValueAsBytes(newMeet(in.nextLine(),in.nextInt(),in.nextInt(),in.nextInt(),login)));
                        System.out.println(responseFinal);
                        break;
                        } else {
                            System.out.println("Авторизируйтесь");
                        break;
                    }
                    case 4: System.out.println("Впишите порядковый номер встречи");
                        http.sendPost("/all_meet",objectMapper.writeValueAsBytes(registration(login,"")));

                        ArrayList<NewMeetRequest> meetRequests = new ArrayList<>();


                        for (int i = 0; i <responseFinal.split(", ").length ; i++) {
                            meetRequests.add(i,objectMapper.readValue(responseFinal.split(", ")[i], NewMeetRequest.class));
                        }
                        for (int i = 0; i < meetRequests.size(); i++) {
                            System.out.println(i +" - "+ "Имя: " + meetRequests.get(i).getName()+" Год: "+meetRequests.get(i).getYear()+" Месяц: "+meetRequests.get(i).getMonth()+" День: "+meetRequests.get(i).getDay());
                        }
                        int num = in.nextInt();
                        http.sendPost("/cancel_meet",objectMapper.writeValueAsBytes(newMeet(meetRequests.get(num).getName(),meetRequests.get(num).getYear(),meetRequests.get(num).getMonth(),meetRequests.get(num).getDay(),login)));
                        break;

                }
            }






        }

        private static NewMeetRequest newMeet(String name, int year, int month, int day, String lead){
            NewMeetRequest meet = new NewMeetRequest();
            meet.setName(name);
            meet.setYear(year);
            meet.setMonth(month);
            meet.setDay(day);
            meet.setLead(lead);
            return meet;
        }

        private static RegistrationRequest registration(String login, String password){
            RegistrationRequest request = new RegistrationRequest();
            request.setLogin(login);
            request.setPassword(password);
            return request;
        }

    //HTTP POST request
    private void sendPost(String qrl, byte[] request) throws Exception {

        String query_url = "http://localhost:8000" + qrl;

        try {
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(request);
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = in.readLine();
            System.out.println(result);
            responseFinal = result;


            in.close();
            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
