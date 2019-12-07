package com.duong.anyquestion.classes;

public class History {


    int conversation_id;
    String title;
    String name;
    float star;
    String id_user;
    String id_expert;

    public History(int conversation_id, String title, String name, float star, String id_user, String id_expert) {
        this.conversation_id = conversation_id;
        this.title = title;
        this.name = name;
        this.star = star;
        this.id_user = id_user;
        this.id_expert = id_expert;
    }


    public History() {

    }
}
