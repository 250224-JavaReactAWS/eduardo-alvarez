package com.revature.services;

import com.revature.models.User;
import com.revature.repos.UserDAO;
import java.util.regex.Pattern;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public boolean validateEmail(String email){
        boolean isValid = true;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern p = Pattern.compile(emailRegex);
        isValid = email != null && p.matcher(email).matches();
        return isValid;
    }

    public boolean validatePassword(String password) {
        boolean isValid = true;
        int minimunCharsInPassword = 8;
        if (password.length() < minimunCharsInPassword) {
            System.out.println("Contraseña muy corta");
            isValid = false;
        }

        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        if (isValid) {
            isValid = false;
            char[] passwordInChars = password.toCharArray();
            for (char c : passwordInChars) {
                if (Character.isUpperCase(c)) {
                    hasUpperCase = true;
                }
                if (Character.isLowerCase(c)) {
                    hasLowerCase = true;
                }
                if (hasUpperCase && hasLowerCase) {
                    //Contraseña válida
                    isValid = true;
                    break;
                }
            }
        }
        if (!hasLowerCase) {
            System.out.println("La contraseña debe contener una letra minuscula por lo menos.");
        } else if (!hasUpperCase) {
            System.out.println("La contraseña debe contener una letra mayuscula por lo menos.");
        }
        return isValid;
    }

    public boolean isEmailAvailable(String email){
        return userDAO.getUserByEmail(email) == null;
    }

    //TODO register
    public User registerNewUser(String firstName, String lastName, String email, String password){

        // Validar contraseña y correo antes que nada

        User newUser = new User(firstName,lastName,email,password);
        return userDAO.create(newUser);
    }

    //TODO login
    public User loginUser(String email, String password){
        // Obtener al usuario por el email
        User desiredUser = userDAO.getUserByEmail(email);
        if (desiredUser==null){
            //No se encontro
            return  null;
        }
        if(desiredUser.getPassword().equals(password)){
            return desiredUser;
        }
        return null;
    }

    //TODO update
}
