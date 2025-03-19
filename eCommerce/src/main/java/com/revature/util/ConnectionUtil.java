package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    private static Connection conn = null;

    private ConnectionUtil(){

    }

    public static Connection getInstance(){
        try {
            if(conn!=null && !conn.isClosed()){
                return conn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }



        String url ="jdbc:postgresql://project-ecommerce.cveeweuksxh9.us-east-2.rds.amazonaws.com:5432/postgres";
        String username = "postgres";
        String password = "Sora1698..";

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Conexion establecida");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se pudo conectar");
        }
        return conn;
    }
}
