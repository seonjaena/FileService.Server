package com.dau.file.entity.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EnumConverter<T extends Enum<T> & EnumFlag<T>> implements AttributeConverter<T, Character> {

    // EnumFlag를 상속하는 ENUM 타입
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    // ENUM 타입을 DB에 저장할 때 자동으로 변환하기 위해 사용
    @Override
    public Character convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : attribute.get();
    }

    // DB에서 쿼리한 정보를 ENUM 타입으로 가져올 때 자동으로 변환하기 위해 사용
    @Override
    public T convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return Enum.valueOf(clazz, "NONE");
        }
        T[] enums = clazz.getEnumConstants();
        for (T anEnum : enums) {
            if (anEnum.get().equals(dbData)) {
                return anEnum;
            }
        }
        return null;
    }
}