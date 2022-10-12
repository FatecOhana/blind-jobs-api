package com.blindjobs.dto;

import lombok.Data;

import java.util.Set;

@Data
public class Payload<T> {
    private Set<T> data;
}
