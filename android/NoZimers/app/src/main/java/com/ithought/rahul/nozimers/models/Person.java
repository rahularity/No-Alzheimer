package com.ithought.rahul.nozimers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("image_url")
    @Expose
    private String image_url;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lives_in")
    @Expose
    private String lives_in;

    @SerializedName("contact")
    @Expose
    private String contact;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("place_of_meeting")
    @Expose
    private String place_of_meeting;

    @SerializedName("relation")
    @Expose
    private String relation;

    @SerializedName("notes")
    @Expose
    private String notes;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLives_in() {
        return lives_in;
    }

    public void setLives_in(String lives_in) {
        this.lives_in = lives_in;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPlace_of_meeting() {
        return place_of_meeting;
    }

    public void setPlace_of_meeting(String place_of_meeting) {
        this.place_of_meeting = place_of_meeting;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Person{" +
                "image_url='" + image_url + '\'' +
                ", name='" + name + '\'' +
                ", lives_in='" + lives_in + '\'' +
                ", contact='" + contact + '\'' +
                ", age='" + age + '\'' +
                ", place_of_meeting='" + place_of_meeting + '\'' +
                ", relation='" + relation + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
