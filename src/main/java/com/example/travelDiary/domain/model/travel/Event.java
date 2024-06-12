package com.example.travelDiary.domain.model.travel;

import com.example.travelDiary.domain.model.tags.Tags;
import com.example.travelDiary.domain.model.wallet.entity.MonetaryEventEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public UUID scheduleId;

    public LocalDateTime eventStartTime;

    public LocalDateTime eventEndTime;

    @OneToMany
    @Cascade(CascadeType.ALL)
    public List<MonetaryEventEntity> monetaryEvents;

    @OneToMany
    public List<Tags> tags;
}
