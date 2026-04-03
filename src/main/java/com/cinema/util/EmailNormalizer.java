package com.cinema.util;

public class EmailNormalizer {

    public static String normalize(String email) {
        if(email == null) return null;

        String[] parts = email.split("@");
        if(parts.length!=2) return email;

        String local = parts[0];
        String domain = parts[1];

        int plusIndex = local.indexOf('+');
        if(plusIndex!=-1) {
            local = local.substring(0, plusIndex);
        }
        return local + "@" + domain;
    }
}
