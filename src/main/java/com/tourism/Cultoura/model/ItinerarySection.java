package com.tourism.Cultoura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "itinerary_sections")
public class ItinerarySection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
    
    private String category; // e.g., Adventure, Sightseeing, Food, Culture

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActivityOption> options;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public Itinerary getItinerary() {
		return itinerary;
	}

	public void setItinerary(Itinerary itinerary) {
		this.itinerary = itinerary;
	}

	public List<ActivityOption> getOptions() {
		return options;
	}

	public void setOptions(List<ActivityOption> options) {
		this.options = options;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

    
}
