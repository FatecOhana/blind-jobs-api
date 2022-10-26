package com.blindjobs.dto;

import com.blindjobs.utils.UtilsValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
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
        this.total = UtilsValidation.isNull(data) ? null : 1L;
    }

    /**
     * Instance class with Exception Item
     */
    public OperationData(Exception ex) {
        this.errors = ex.getLocalizedMessage();
        this.totalErrors = 1L;
    }

    /**
     * Add one item in data
     *
     * @param item to be added into data
     */
    public void addInData(T item) {
        this.data.add(item);
    }

    /**
     * Add many items in data
     *
     * @param items to be added into data
     */
    public void addInData(Set<T> items) {
        this.data.addAll(items);
    }

    /**
     * Set erros and your quantity
     *
     * @param errors existent in operation
     */
    public void setErrors(String errors) {
        this.errors = errors;
        this.totalErrors = UtilsValidation.isNullOrEmpty(errors) ? null : (long) errors.length();
    }
}
