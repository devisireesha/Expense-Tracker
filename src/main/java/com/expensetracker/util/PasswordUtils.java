package com.expensetracker.util;


import java.util.Base64;

public class PasswordUtils {

    public static String encryptPassword(String plainPassword) {
        return Base64.getEncoder().encodeToString(plainPassword.getBytes());
    }

    public static String decryptPassword(String encryptedPassword) {
        return new String(Base64.getDecoder().decode(encryptedPassword));
    }
}
