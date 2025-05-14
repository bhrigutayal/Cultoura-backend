package com.tourism.Cultoura.model;


import jakarta.persistence.*;

@Entity
public class ActivityItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(name = "description", length = 3500)
    private String description;
    
    @Column(name = "image_url")
    private String imageUrl;
    private String startHour;
    private String endHour;

    private String date;
    private double cost;
    private float rating;
    private String type;

    @Column(name = "section_id")
    private Integer sectionId;

    private String location;

    public ActivityItemModel() {}

    public ActivityItemModel(Long id, String title, String description, String imageUrl, String startHour,String endHour,String date,
                             double cost, float rating,String type, Integer sectionId, String location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.startHour = startHour;
        this.endHour = endHour;
        this.date = date;
        this.cost = cost;
        this.rating = rating;
        this.type = type;
        this.sectionId = sectionId;
        this.location = location;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }


    public Integer getSectionId() { return sectionId; }
    public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ActivityItemModel [id=" + id + ", title=" + title + ", description=" + description + ", imageUrl="
				+ imageUrl + ", startHour=" + startHour+ ", endHour" + endHour+ ", date=" + date + ", cost=" + cost + ", rating=" + rating
				+ ", type=" + type + ", sectionId=" + sectionId + ", location=" + location + "]";
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
}
