package com.blindjobs.dto.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    ENTERPRISE("EMPRESA"), STUDENT("ESTUDANTE");

    @JsonValue // Consider the string instead of the enum name
    private final String type;

    UserType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
