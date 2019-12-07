package com.duong.anyquestion.classes;

public class Field {
    int field_id;
    String name;

    public Field(int field_id, String name) {
        this.field_id = field_id;
        this.name = name;
    }

    public Field() {
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}