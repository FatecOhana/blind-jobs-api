package com.blindjobs.dto.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkModel {
    HYBRID("HIBRIDO"), HOME_OFFICE("HOME OFFICE"), PRESENTIAL("PRESENCIAL");

    @JsonValue // Consider the string instead of the enum name
    private final String name;

    WorkModel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
