package org.hh99.tmomi.domain.event.dto.event;

import java.time.LocalDate;
import java.util.List;

import org.hh99.tmomi.domain.event.dto.eventtimes.EventTimesResponseDto;
import org.hh99.tmomi.domain.event.entity.Event;

import lombok.Getter;

@Getter
public class EventResponseDto {

	private final String eventName;
	private final LocalDate eventStartDate;
	private final LocalDate eventEndDate;
	private final String eventImage;
	private final String eventDescription;
	private final String stageAddress;
	private List<EventTimesResponseDto> timeList;

	public EventResponseDto(Event event, String stageAddress) {
		this.eventName = event.getEventName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
		this.eventImage = event.getEventImage();
		this.eventDescription = event.getEventDescription();
		this.stageAddress = stageAddress;
	}

	public EventResponseDto(Event event) {
		this.eventName = event.getEventName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
		this.eventImage = event.getEventImage();
		this.eventDescription = event.getEventDescription();
		this.stageAddress = event.getStage().getAddress();
	}

	public EventResponseDto(Event event, String stageAddress, List<EventTimesResponseDto> timeList) {
		this.eventName = event.getEventName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
		this.eventImage = event.getEventImage();
		this.eventDescription = event.getEventDescription();
		this.stageAddress = stageAddress;
		this.timeList = timeList;
	}
}
