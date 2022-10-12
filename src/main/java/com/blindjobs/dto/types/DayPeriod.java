package com.blindjobs.dto.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DayPeriod {

    MORNING("MANHÃ"), AFTERNOON("TARDE"), NIGHT("NOITE"), FLEXIBLE("FLEXIVEL");

    @JsonValue // Consider the string instead of the enum name
    private final String name;

    DayPeriod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
