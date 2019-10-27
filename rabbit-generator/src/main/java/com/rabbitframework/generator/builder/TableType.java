package com.rabbitframework.generator.builder;

public enum TableType {
    ALL("all"),
    ASSIGN("assign");
    private final String type;

    private TableType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TableType getTableType(String type) {
        if (ALL.getType().equalsIgnoreCase(type)) {
            return ALL;
        } else if (ASSIGN.getType().equalsIgnoreCase(type)) {
            return ASSIGN;
        } else {
            throw new RuntimeException("tableType " + type + " error");
        }
    }
}
