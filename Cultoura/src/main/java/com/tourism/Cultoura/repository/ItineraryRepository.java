package com.tourism.Cultoura.repository;

import com.tourism.Cultoura.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByCreatedBy(String createdBy);
}

