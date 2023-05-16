package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.model.AbstractEntity;
import pjwstk.s20124.tin.model.ERole;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.EventMember;
import pjwstk.s20124.tin.model.EventMemberAttendingStatus;
import pjwstk.s20124.tin.model.EventMemberId;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.input.EventMemberIncomeDto;
import pjwstk.s20124.tin.model.mapper.EventMapper;
import pjwstk.s20124.tin.repository.EventMemberRepository;
import pjwstk.s20124.tin.repository.EventRepository;
import pjwstk.s20124.tin.repository.UserRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public Event create(Event entity, List<Long> members) {

        Set<User> membersReference = members.stream()
            .map(userRepository::getReferenceById)
            .collect(Collectors.toSet());

        User host = SecurityUtils.getCurrentUserLogin()
            .map(userRepository::getReferenceByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        entity.setHost(host);
        membersReference.add(host);

        List<EventMember> eventMembers = membersReference.stream().map(ref -> {
            EventMember eventMember = new EventMember();
            eventMember.setUser(ref);
            eventMember.setEvent(entity);
            eventMember.setAttendingStatus(EventMemberAttendingStatus.INVITED);
            eventMember.setEventMemberId(new EventMemberId(entity.getId(), ref.getId()));
            return eventMember;
        }).toList();

        entity.getMembers().addAll(eventMembers);

        return eventRepository.save(entity);
    }

    @Transactional
    public Event update(Long id, Event dto, List<Long> members) throws Exception {

        Event entity = eventRepository.findEventById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Set<User> membersReference = members.stream()
            .map(userRepository::getReferenceById)
            .collect(Collectors.toSet());

        eventMapper.updateEntity(entity, dto);

        entity.getMembers().removeIf(eventMember -> !members.contains(eventMember.getUser().getId()) && !eventMember.getUser().getId().equals(entity.getHost().getId()));


        List<EventMember> eventMembers = membersReference.stream()
            .filter(ref -> entity.getMembers().stream().map(EventMember::getUser).noneMatch(ref::equals))
            .map(ref -> {
            EventMember eventMember = new EventMember();
            eventMember.setUser(ref);
            eventMember.setEvent(entity);
            eventMember.setAttendingStatus(EventMemberAttendingStatus.INVITED);
            eventMember.setEventMemberId(new EventMemberId(entity.getId(), ref.getId()));
            return eventMember;
        }).toList();


        entity.getMembers().addAll(eventMembers);


        return entity;
    }

    @Transactional
    public void delete(Long id) {
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Event event = eventRepository.findEventById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!Objects.equals(event.getHost().getId(), currentUser.getId()) && !SecurityUtils.hasCurrentUserThisAuthority(ERole.ROLE_ADMIN.name())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        eventRepository.deleteById(id);

    }

    @Transactional
    public Optional<Event> getOne(Long id) {
        return eventRepository.findEventById(id);
    }

    public Page<Event> getList(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, int pageNo, String order, String dir) {
        User user = null;


        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            user = userRepository.getReferenceByUsername(SecurityUtils.getCurrentUserLogin().get());
        }

        Sort sort = Sort.by(order);

        Pageable pageable = PageRequest.of(pageNo, 20, sort);

        return eventRepository.findEventByTitleAndStartDateTimeAndEndDateTimeAndUserId(startDateTime,endDateTime,user,title, pageable);
    }

    @Transactional
    public void changeEventMemberStatus(Long id, EventMemberIncomeDto dto) {
        Event event = eventRepository.findEventById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        event.getMembers().stream()
            .filter(eventMember -> eventMember.getUser().getUsername().equals(username))
            .findFirst()
            .ifPresent(eventMember -> eventMember.setAttendingStatus(dto.status()));
    }
}
