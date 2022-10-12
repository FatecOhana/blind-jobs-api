package com.blindjobs.dto.types;

public enum WorkModel {
    HYBRID("HIBRIDO"), HOME_OFFICE("HOME_OFFICE"), PRESENTIAL("PRESENCIAL");

    private final String name;

    WorkModel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
