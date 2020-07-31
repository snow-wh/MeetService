package com.server;

import com.reqest.NewMeetRequest;
import com.reqest.RegistrationRequest;


import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;


public class DBConnection {

        private final String URL = "jdbc:mysql://localhost:3306/meet_service";
        private final String USERNAME = "root";
        private final String PASSWORD = "admin";




        private Connection connection;
        private PreparedStatement preparedStatement;

        public DBConnection(){
            try {

                Driver driver = new com.mysql.cj.jdbc.Driver();
                DriverManager.registerDriver(driver);
                connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        public void RegistrationMember(RegistrationRequest registrationRequest){

            try {
            Statement statement = connection.createStatement();
            statement.execute("insert into meet_service.members (login, password)values ('"+registrationRequest.getLogin()+"','"+registrationRequest.getPassword()+"')");
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            }
        }

        public boolean DBCheck(RegistrationRequest registrationRequest){

            boolean check=false;
            try {

                String request = "select login from meet_service.members";

                preparedStatement = connection.prepareStatement(request);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    if (resultSet.getString("login").equals(registrationRequest.getLogin())){
                        check=true;
                    }else {
                        check=false;
                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return check;

        }

        public void NewMeet(NewMeetRequest newMeetRequest){
            try {
                Statement statement = connection.createStatement();
                statement.execute("insert into meet_service.meet (name,year,month,day,time,leadTeam)values ('"
                        +newMeetRequest.getName()+"','"+newMeetRequest.getYear()+"','"
                        +newMeetRequest.getMonth()+"','"+newMeetRequest.getDay()+"','"+newMeetRequest.getTime()+"','"+newMeetRequest.getLead()+"')");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    public boolean DBMeetCheck(NewMeetRequest newMeetRequest){

        boolean check=false;
        try {

            String request = "select *from meet_service.meet";

            preparedStatement = connection.prepareStatement(request);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if((resultSet.getString("name").equals(newMeetRequest.getName())) ||
                        (resultSet.getString("year").equals(newMeetRequest.getYear())) ||
                        (resultSet.getString("month").equals(newMeetRequest.getMonth())) ||
                        (resultSet.getString("date").equals(newMeetRequest.getDay()))){
                    check = false;
                }else {
                    check = true;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;

    }

    public ArrayList<NewMeetRequest> AllMeet(RegistrationRequest registrationRequest){
        String request = "select *from meet_service.meet where leadTeam='"+registrationRequest.getLogin() +"'";

        ArrayList<NewMeetRequest> newMeetList = new ArrayList<>();
        int i=0;
        try {
            preparedStatement = connection.prepareStatement(request);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                NewMeetRequest newMeet = new NewMeetRequest();
                newMeet.setName(resultSet.getString("name"));
                newMeet.setYear(resultSet.getInt("year"));
                newMeet.setMonth(resultSet.getInt("month"));
                newMeet.setDay(resultSet.getInt("day"));
                newMeet.setLead(resultSet.getString("leadTeam"));
                newMeetList.add(i,newMeet);
                i++;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return newMeetList;
    }

    public void CancelMeet(NewMeetRequest newMeetRequest){
        try {
            Statement statement = connection.createStatement();
            statement.execute("delete from meet_service.meet where leadTeam='"+newMeetRequest.getLead()+"' and year="+
                    newMeetRequest.getYear()+" and month="+newMeetRequest.getMonth()+" and day="+newMeetRequest.getDay());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void NewMember(NewMeetRequest newMeetRequest){
        try {
            Statement statement = connection.createStatement();
            statement.execute("update meet_service.meet set name=name+','+"+newMeetRequest.getName()+" where " +
                    "leadTeam='"+newMeetRequest.getLead()+"' and year="+newMeetRequest.getYear()+" and month="+newMeetRequest.getMonth()+
                    " and day="+newMeetRequest.getDay());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void DeleteMember(NewMeetRequest newMeetRequest){
        String request = "select name and id from meet_service.meet where leadTeam="+newMeetRequest.getLead()+
                " and year="+newMeetRequest.getYear()+" and month="+newMeetRequest.getMonth()+
                " and day="+newMeetRequest.getDay();
        String newName = null;
        int id = 0;

        try {
            preparedStatement = connection.prepareStatement(request);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                for (int i = 0; i < resultSet.getString("name").split(",").length; i++) {
                    if(!resultSet.getString("name").split(",")[i].equals(newMeetRequest.getName())){
                        newName+= resultSet.getString("name").split(",")[i] + ",";
                    }

                }
                id = resultSet.getInt("id");
            }
            Statement statement = connection.createStatement();
            statement.execute("update meet_service.meet set name="+ newName + " where " +
                    "id=" + id);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}



