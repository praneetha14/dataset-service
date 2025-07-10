package com.dataset.service.model.enums;

import java.util.Arrays;

public enum SupportedDataSets {
    EMPLOYEE("employee"),
    DEPARTMENT("department");

    private final String value;

    SupportedDataSets(String value) {
        this.value = value;
    }

    public static boolean isSupportedDataSet(String value) {
        return Arrays.stream(values()).anyMatch(v -> v.value.equalsIgnoreCase(value));
    }
}
