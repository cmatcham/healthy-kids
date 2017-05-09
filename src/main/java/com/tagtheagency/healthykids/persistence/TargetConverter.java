package com.tagtheagency.healthykids.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.tagtheagency.healthykids.model.Target;

@Converter
public class TargetConverter implements AttributeConverter<Target, String> {

    @Override
    public String convertToDatabaseColumn(Target attribute) {
        switch (attribute) {
            case MOVEMENT:
                return "M";
            case NUTRITION:
                return "N";
            case SLEEP:
                return "S";
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public Target convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "S":
                return Target.SLEEP;
            case "N":
                return Target.NUTRITION;
            case "M":
                return Target.MOVEMENT;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}