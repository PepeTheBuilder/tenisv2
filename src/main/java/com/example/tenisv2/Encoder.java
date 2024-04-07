package com.example.tenisv2;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Encoder {

    private Encoder() {
    }
    private static final Encoder INSTANCE = new Encoder();

    public static Encoder getInstance() {
        return INSTANCE;
    }
    //    @Singleton
    public static String encodingPassword(String password) {
        StringBuilder encodedPassword = new StringBuilder();
        for(char c : password.toCharArray()) {
            encodedPassword.append((char) (c + 1));
        }
        return encodedPassword.toString();
    }
    public static String decodingPassword(String password) {
        StringBuilder decodedPassword = new StringBuilder();
        for(char c : password.toCharArray()) {
            decodedPassword.append((char) (c - 1));
        }
        return decodedPassword.toString();
    }
    private static Timestamp convertToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString);
        }
    }

}
