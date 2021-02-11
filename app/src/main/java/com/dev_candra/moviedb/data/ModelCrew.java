package com.dev_candra.moviedb.data;

import java.io.Serializable;

public class ModelCrew implements Serializable {

    private int Id;
    private String name;
    private String profil_path;
    private String job;

    public ModelCrew() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfil_path() {
        return profil_path;
    }

    public void setProfil_path(String profil_path) {
        this.profil_path = profil_path;
    }

    public String getJob() {
        return job;
    }

    public int getId(){
        return Id;
    }

    public void setId(int Id){
        this.Id = Id;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
