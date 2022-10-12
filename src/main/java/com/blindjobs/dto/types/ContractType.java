package com.blindjobs.dto.types;

public enum ContractType {

    INTERN("PESSOA ESTAGIARIA"), CLT("CLT"), PJ("PJ - PESSOA JURIDICA"), FREELANCER("PESSOA FREELANCER");

    private final String name;

    ContractType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
