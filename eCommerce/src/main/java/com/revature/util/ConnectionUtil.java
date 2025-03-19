package com.revature.util;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static Connection conn = null;

    private ConnectionUtil(){

    }

    public static Connection getConnection(){
        try {
            if(conn!=null && !conn.isClosed()){
                return conn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }



        String url;
        String username;
        String password;

        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/resources/application.properties"));
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Conexion establecida");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se pudo conectar");
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("No se pudo leer propiedades");
        }
        return conn;
    }
}
