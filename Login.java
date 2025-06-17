/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practice_poe;

/**
 *
 * @author RC_Student_lab
 */
class Login {

    public boolean isValidUsername(String username) {
        return username != null && username.contains("_") && username.length() >= 5;
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasSpecial;
    }

    public boolean checkMessage(String message) {
        return message != null && message.length() <= 250;
    }

    public String verifyDetails(String username, String password, String phone) {
        return "Username: " + username + "\nPassword: " + password + "\nPhone: " + phone;
    }
}



