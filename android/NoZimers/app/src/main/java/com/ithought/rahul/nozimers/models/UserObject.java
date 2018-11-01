package com.ithought.rahul.nozimers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserObject {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("children")
    @Expose
    private List<Person> person;

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> children) {
        this.person = person;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "status='" + status + '\'' +
                ", person=" + person +
                '}';
    }
}
