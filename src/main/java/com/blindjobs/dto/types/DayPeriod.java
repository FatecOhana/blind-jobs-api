package com.blindjobs.dto.types;

public enum DayPeriod {

    MORNING("MANHÃ"), AFTERNOON("TARDE"), NIGHT("NOITE"), FLEXIBLE("FLEXIVEL");

    private final String name;

    DayPeriod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
