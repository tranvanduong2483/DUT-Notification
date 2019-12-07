package com.duong.anyquestion.classes;

public class SecurityQuestion {
    int security_question_id;
    String content;

    public int getSecurity_question_id() {
        return security_question_id;
    }

    public void setSecurity_question_id(int security_question_id) {
        this.security_question_id = security_question_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SecurityQuestion(int security_question_id, String content) {
        this.security_question_id = security_question_id;
        this.content = content;
    }

    public SecurityQuestion(){}

    @Override
    public String toString() {
        return content;
    }
}
