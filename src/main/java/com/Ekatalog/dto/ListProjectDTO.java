package com.Ekatalog.dto;

public class ListProjectDTO {

    private String nama_project;

    private String teknologi;

    private String developer;

    private String link;

    private String deskripsi_project;

    public String getNama_project() {
        return nama_project;
    }

    public void setNama_project(String nama_project) {
        this.nama_project = nama_project;
    }

    public String getTeknologi() {
        return teknologi;
    }

    public void setTeknologi(String teknologi) {
        this.teknologi = teknologi;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeskripsi_project() {
        return deskripsi_project;
    }

    public void setDeskripsi_project(String deskripsi_project) {
        this.deskripsi_project = deskripsi_project;
    }
}
