package com.revature.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    public  User(int id,String fName, String lName, String email, String password){
        userID = id;
        firstName = fName;
        lastName = lName;
        this.email=email;
        this.password = password;
        role = Role.USER;
    }
    public  User(String fName, String lName, String email, String password){
        firstName = fName;
        lastName = lName;
        this.email=email;
        this.password = password;
        role = Role.USER;
    }

    public  User(ResultSet rs){
        try {
            userID = rs.getInt("user_id");
            firstName = rs.getString("first_name");
            lastName = rs.getString("last_name");
            email = rs.getString("email");
            password = rs.getString("password");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Algo salio mal al crear con set result");
        }
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString(){
        return "My ID: "+userID+" My firstname: "+firstName+" My lastname: "+lastName+" My email: "+email+" My password: "+password+" My role: "+
                role;
    }
}
