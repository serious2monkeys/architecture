package ru.doronin.shortener.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Base62 {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encode(long number) {
        StringBuilder sb = new StringBuilder();
        while (number != 0) {
            sb.append(BASE62.charAt((int) (number % 62)));
            number /= 62;
        }
        while (sb.length() < 6) {
            sb.append(0);
        }
        return sb.reverse().toString();
    }

    public long decode(String encoded) {
        long decoded = 0;
        for (int i = 0; i < encoded.length(); i++) {
            decoded = decoded * 62 + BASE62.indexOf(encoded.charAt(i));
        }
        return decoded;
    }
}
