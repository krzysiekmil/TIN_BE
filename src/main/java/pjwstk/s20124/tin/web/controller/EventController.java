package pjwstk.s20124.tin.web.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s20124.tin.common.CrudApi;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.dto.input.EventIncomeDto;
import pjwstk.s20124.tin.model.dto.input.EventMemberIncomeDto;
import pjwstk.s20124.tin.model.dto.output.EventOutputDto;
import pjwstk.s20124.tin.model.mapper.EventMapper;
import pjwstk.s20124.tin.services.EventService;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventOutputDto create(@RequestBody  EventIncomeDto dto) {
        Event event = eventService.create(eventMapper.mapInputDtoToEntity(dto), dto.members());

        return eventMapper.mapToOutputDto(event);
    }

    @PutMapping("/{id}")
    public EventOutputDto update(@PathVariable Long id,@RequestBody EventIncomeDto dto) throws Exception {
        Event event =  eventService.update(id, eventMapper.mapInputDtoToEntity(dto), dto.members());

        return eventMapper.mapToOutputDto(event);
    }

    @PatchMapping("/{id}/eventMembers")
    public void changeEventMember (@PathVariable Long id, @Valid @RequestBody EventMemberIncomeDto dto){
        eventService.changeEventMemberStatus(id, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }
    @GetMapping("/{id}")
    public Optional<EventOutputDto> getOne(@PathVariable Long id) {

        return eventService.getOne(id).map(eventMapper::mapToOutputDto);
    }

    @GetMapping
    public Page<EventOutputDto> getList(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) LocalDateTime startDateTime,
        @RequestParam(required = false) LocalDateTime endDateTime,
        @RequestParam(required = false, defaultValue = "0") int pageNo,
        @RequestParam(required = false, defaultValue = "createDate") String sort,
        @RequestParam(required = false, defaultValue = "DESC") String dir
    ) {
        return eventService.getList(title, startDateTime, endDateTime, pageNo, sort, dir)
            .map(eventMapper::mapToOutputDto);
    }
}
