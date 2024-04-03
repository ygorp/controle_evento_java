package com.example.pass.in.controllers;

import com.example.pass.in.dto.attendee.AttendeesListReponseDTO;
import com.example.pass.in.dto.event.EventIdDTO;
import com.example.pass.in.dto.event.EventRequestDTO;
import com.example.pass.in.dto.event.EventResponseDTO;
import com.example.pass.in.services.AttendeeService;
import com.example.pass.in.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
        EventResponseDTO event = this.eventService.getEventDetail(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListReponseDTO> getEventAttendees(@PathVariable String id) {
        AttendeesListReponseDTO attendeesListReponse = this.attendeeService.getEventAttendee(id);
        return ResponseEntity.ok(attendeesListReponse);
    }
}
