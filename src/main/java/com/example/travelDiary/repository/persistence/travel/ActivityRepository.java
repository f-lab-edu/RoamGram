package com.example.travelDiary.repository.persistence.travel;

import com.example.travelDiary.domain.model.travel.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
