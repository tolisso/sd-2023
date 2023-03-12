package com.github.tolisso.championutstg.util;

public class IdProvider {

    private static long id = 0;

    public static synchronized long getNewId() {
        return id++;
    }
}
