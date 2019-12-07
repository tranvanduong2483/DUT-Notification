package com.duong.anyquestion.classes;

public class Education {
    int education_id;
    String name;

    public Education(int education_id, String name) {
        this.education_id = education_id;
        this.name = name;
    }

    public Education() {
    }

    public int getEducation_id() {
        return education_id;
    }

    public void setEducation_id(int education_id) {
        this.education_id = education_id;
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
