package com.example.serverdata;

public class Items_List {
    private String userId;
    private String id;
    private String title;
    private String completed;




    public Items_List(String userId,String id,String title,String completed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public Items_List() {

    }


    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;

    }
    public String getCompleted(){
        return completed;
    }
    public void setCompleted(String completed){
        this.completed = completed;
    }

}
