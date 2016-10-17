package com.benoitarsenault.recipebook.model;

/**
 * Created by jhoffman on 2016-10-02.
 */

public enum SortOrder {
    ASC(0, "ASC"),
    DESC(1, "DESC");

    private final int value;
    private final String sqliteValue;

    private SortOrder(int value, String sqliteValue) {
        this.value = value;
        this.sqliteValue = sqliteValue;
    }

    public static SortOrder createFromInt(int value)  {
        SortOrder sortOrder = SortOrder.ASC;
        if (value == 1) {
            sortOrder = SortOrder.DESC;
        }

        return sortOrder;
    }

    public int getValue() {
        return value;
    }

    public String getSqliteValue() {
        return sqliteValue;
    }
}
