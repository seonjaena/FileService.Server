package com.dau.file.entity.enums;

public enum STATUS implements EnumFlag<STATUS> {

    NORMAL('N', "NORMAL"),
    DELETED('D', "DELETED")
    ;

    private final char flag;
    private final String text;

    STATUS(char flag, String text) {
        this.flag = flag;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public Character get() {
        return this.flag;
    }

    public static class Converter extends EnumConverter<STATUS> {
        public Converter() {
            super(STATUS.class);
        }
    }
}