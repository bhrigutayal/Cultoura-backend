package com.tourism.Cultoura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ActivitySectionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "sectionId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityItemModel> activities;

    // Constructors
    public ActivitySectionModel() {}

    public ActivitySectionModel(Long id, String title, List<ActivityItemModel> activities) {
        this.id = id;
        this.title = title;
        this.activities = activities;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<ActivityItemModel> getActivities() {
        return activities;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActivities(List<ActivityItemModel> activities) {
        this.activities = activities;
    }
}
