package com.example.pass.in.services;

import com.example.pass.in.domain.attendee.Attendee;
import com.example.pass.in.domain.checkin.CheckIn;
import com.example.pass.in.dto.attendee.AttendeeDetails;
import com.example.pass.in.dto.attendee.AttendeesListReponseDTO;
import com.example.pass.in.repositories.AttendeeRepository;
import com.example.pass.in.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;

    public List<Attendee> getAllAttendeeFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListReponseDTO getEventAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeeFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();
        return new AttendeesListReponseDTO(attendeeDetailsList);
    }
}
