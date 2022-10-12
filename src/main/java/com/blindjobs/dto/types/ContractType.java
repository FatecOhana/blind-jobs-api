package com.blindjobs.dto.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ContractType {

    INTERN("PESSOA ESTAGIARIA"), CLT("CLT"), PJ("PJ - PESSOA JURIDICA"), FREELANCER("PESSOA FREELANCER");

    @JsonValue // Consider the string instead of the enum name
    private final String name;

    ContractType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
