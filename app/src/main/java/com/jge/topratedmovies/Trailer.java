package com.jge.topratedmovies;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
