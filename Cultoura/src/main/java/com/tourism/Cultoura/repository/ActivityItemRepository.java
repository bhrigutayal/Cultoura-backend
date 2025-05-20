package com.tourism.Cultoura.repository;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tourism.Cultoura.model.ActivityItemModel;

@Repository
public interface ActivityItemRepository extends JpaRepository<ActivityItemModel, Long> {
	List<ActivityItemModel> findByCityAndType(String city, String type);

}