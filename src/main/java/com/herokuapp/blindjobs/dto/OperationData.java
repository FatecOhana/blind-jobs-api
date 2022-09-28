package com.herokuapp.blindjobs.dto;

import com.herokuapp.blindjobs.services.utils.UtilsValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class OperationData<T> {
    private Set<T> data;
    private Long total;
    private String errors;
    private Long totalErrors;

    public OperationData(Set<T> data, String errors) {
        this.data = data;
        this.errors = errors;
        this.total = UtilsValidation.isNullOrEmpty(data) ? 0L : data.size();
        if (UtilsValidation.isNullOrEmpty(errors)) {
            this.totalErrors = 0L;
        } else {
            this.totalErrors = (long) errors.replaceAll("[^\\\\][^n]", "").length() / 2;
        }
    }

    /**
     * Instance class with Unique Item
     */
    public OperationData(T data) {
        this.data = Collections.singleton(data);
    }

    /**
     * Instance class with Exception Item
     */
    public OperationData(Exception ex) {
        this.errors = ex.getLocalizedMessage();
        this.totalErrors = 1L;
    }


}
