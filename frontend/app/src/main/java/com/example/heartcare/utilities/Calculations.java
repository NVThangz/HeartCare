package com.example.heartcare.utilities;

import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Date;

public class Calculations {
    public static int bpm(int cycles, double seconds) {
        return (int) Math.round((double) (cycles / seconds) * 60);
    }


    public static int calculateAge(Date birthDate) {
        OffsetDateTime startOdt = birthDate.toInstant().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOdt = new Date().toInstant().atOffset(ZoneOffset.UTC);
        int years = Period.between(startOdt.toLocalDate(), endOdt.toLocalDate()).getYears();
        return years;
    }

    public static double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }
}
