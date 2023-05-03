package com.example.heartcare.utilities;

public class Calculations {
    public static int bpm(int cycles, double seconds) {
        return (int) Math.round((double) (cycles / seconds) * 60);
    }
}
