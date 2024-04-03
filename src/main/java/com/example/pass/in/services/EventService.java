package com.example.pass.in.services;

import com.example.pass.in.domain.attendee.Attendee;
import com.example.pass.in.domain.event.exceptions.EventNotFoundException;
import com.example.pass.in.dto.event.EventIdDTO;
import com.example.pass.in.dto.event.EventRequestDTO;
import com.example.pass.in.dto.event.EventResponseDTO;
import com.example.pass.in.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import com.example.pass.in.domain.event.Event;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeeFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }
    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\W\\S]", "")
                .replaceAll("\\S+", "-")
                .toLowerCase();
    }
}
