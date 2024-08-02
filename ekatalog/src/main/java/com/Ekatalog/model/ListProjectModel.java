package com.Ekatalog.model;


import javax.persistence.*;

@Entity
@Table(name = "list_project")
public class ListProjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long Id;

    @Column(name = "nama_project")
    private String nama_project;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Column(name = "teknologi")
    private String teknologi;

    @Column(name = "developer")
    private String developer;

    @Column(name = "logo")
    private String logo;

    @Column(name = "link")
    private String link;

}
