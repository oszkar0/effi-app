package com.effi.EffiApp.utils.passwords;

import org.springframework.security.core.parameters.P;

import java.util.Random;

public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]};:',<.>/?";
    public static String generatePassword(int length){
        StringBuilder password = new StringBuilder();

        Random random = new Random();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
