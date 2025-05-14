package com.tourism.Cultoura.repository;

import com.tourism.Cultoura.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCityIgnoreCase(String city);
    List<Event> findByOrganisedBy(String organisedBy);
}