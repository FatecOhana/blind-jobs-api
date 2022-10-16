package com.blindjobs.restendpoints.config;

import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.dto.types.UserType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class StringUserTypeEnumConverter implements Converter<String, UserType> {

    @Override
    public UserType convert(String source) {
        try {
            return Arrays.stream(UserType.values()).filter(v -> Objects.equals(v.toString(), source)).findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format("not found UserType with value = [%s]", source)));
        } catch (Exception e) {
            return null;
        }
    }
}
