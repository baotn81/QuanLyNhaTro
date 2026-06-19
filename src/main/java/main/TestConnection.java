package main;

import database.ConnectDB;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection con = ConnectDB.getConnection();

        if(con != null){

            System.out.println("TEST THÀNH CÔNG");

            ConnectDB.close(con);

        }else{

            System.out.println("TEST THẤT BẠI");

        }

    }

}