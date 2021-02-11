package com.dev_candra.moviedb.data;

import java.io.Serializable;

public class ModelCastinng implements Serializable {

    private int Id;
    private String profil_path;
    private String name;
    private String character;

    public ModelCastinng() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProfil_path() {
        return profil_path;
    }

    public void setProfil_path(String profil_path) {
        this.profil_path = profil_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
