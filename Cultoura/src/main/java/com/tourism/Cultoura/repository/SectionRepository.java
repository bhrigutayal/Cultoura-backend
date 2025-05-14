package com.tourism.Cultoura.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tourism.Cultoura.model.ActivitySectionModel;

@Repository
public interface SectionRepository extends JpaRepository<ActivitySectionModel, Long>{


}
