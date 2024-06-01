package com.example.travelDiary.application.service.travel.event;

import com.example.travelDiary.application.service.tags.TagsAccessService;
import com.example.travelDiary.application.service.wallet.MonetaryDomainQueryService;
import com.example.travelDiary.domain.model.travel.Event;
import com.example.travelDiary.domain.model.wallet.aggregate.MonetaryEvent;
import com.example.travelDiary.domain.model.wallet.entity.MonetaryEventEntity;
import com.example.travelDiary.presentation.dto.request.travel.event.EventMetaDataUpsertRequest;
import com.example.travelDiary.repository.persistence.travel.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventAccessService {
    private final EventRepository eventRepository;
    private final TagsAccessService tagsAccessService;
    private final MonetaryDomainQueryService monetaryDomainQueryService;

    @Autowired
    public EventAccessService(EventRepository eventRepository, TagsAccessService tagsAccessService, MonetaryDomainQueryService monetaryDomainQueryService) {
        this.eventRepository = eventRepository;
        this.tagsAccessService = tagsAccessService;
        this.monetaryDomainQueryService = monetaryDomainQueryService;
    }


    public Event getEventById(UUID id) {
        return eventRepository.findById(id).orElseThrow();
    }

    public List<MonetaryEvent> getAllMonetaryEvents(UUID eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return monetaryDomainQueryService.convertAllToAggregates(event.getMonetaryEvents());
    }

    public List<MonetaryEvent> getAllMonetaryEventsInEventsByTag(UUID eventId, List<String> tagNames) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<UUID> monetaryEventsId = event.getMonetaryEvents().stream().map(MonetaryEventEntity::getId).toList();
        List<UUID> filteredMonetaryEventsId = tagsAccessService
                .filterEntitiesByTags(monetaryEventsId,tagNames);
        return monetaryDomainQueryService.findAllById(filteredMonetaryEventsId);
    }
}
