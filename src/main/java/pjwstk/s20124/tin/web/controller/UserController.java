package pjwstk.s20124.tin.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.model.ERole;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.UserDto;
import pjwstk.s20124.tin.model.dto.input.PostIncomeDto;
import pjwstk.s20124.tin.model.dto.input.UserFriendsIncomeDto;
import pjwstk.s20124.tin.model.dto.input.UserPasswordIncomeDto;
import pjwstk.s20124.tin.model.dto.output.UserInvitationsOutputDto;
import pjwstk.s20124.tin.model.dto.output.UserOutputDto;
import pjwstk.s20124.tin.model.mapper.UserMapper;
import pjwstk.s20124.tin.services.UserService;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService service;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto dto) {

        User entity = userMapper.dtoToUser(dto);
        User user = service.create(entity);

        return userMapper.userToDto(user);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody UserDto dto) {

        User entity = userMapper.dtoToUser(dto);
        User user = service.update(id, entity);

        return userMapper.userToDto(user);
    }

    @PutMapping("/me")
    public UserDto updateMe(@RequestPart(required = false) MultipartFile file, @RequestPart @Valid UserDto dto) {

        User entity = userMapper.dtoToUser(dto);
        User user = service.updateMe(entity, file);

        return userMapper.userToDto(user);
    }

    @PatchMapping("/me/password")
    public UserDto updatePassword(@Valid @RequestBody UserPasswordIncomeDto dto) {

        if(!Objects.equals(dto.newPassword(), dto.repeatPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User user = service.updatePassword(dto);

        return userMapper.userToDto(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public UserOutputDto getOne(@PathVariable Long id) {
        return service.getOne(id)
            .map(userMapper::userToDtoByRole)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/me")
    public UserOutputDto getMe(){
        return service.getMe()
            .map(userMapper::userToOutputDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/me/friends")
    public void addToUserFriends(@Valid @RequestBody UserFriendsIncomeDto dto) {
        service.addToUserFriends(dto.id());
    }

    @PostMapping("/me/inviteFriends")
    public void inviteToUserFriends(@Valid @RequestBody UserFriendsIncomeDto dto) {
        service.inviteToFriends(dto.id());
    }

    @DeleteMapping("/me/inviteFriends/{id}")
    public void rejectInvitation(@PathVariable Long id) {
        service.rejectInvitation(id);
    }

    @PatchMapping(path = "/me/inviteFriends/{id}")
    public void acceptUserToFriends(@PathVariable Long id) {
        service.acceptUserToFriends(id);
    }

    @DeleteMapping("/me/friends/{id}")
    public void getFriendsList(@PathVariable Long id) {
        service.removeFromFriends(id);
    }

    @GetMapping("/me/friends")
    public Collection<UserOutputDto> getFriendsList() {
        return service.getFriendsList()
            .stream()
            .map(userMapper::userToOutputDto)
            .toList();
    }

    @GetMapping("/me/invitations")
    public Collection<UserInvitationsOutputDto> getInvitations() {
        return service.getUserInvitation()
            .stream()
            .map(userMapper::userToInvitationDto)
            .toList();
    }

    @GetMapping
    public Object getList(@RequestParam(required = false) String query) {

        if (SecurityUtils.hasCurrentUserThisAuthority(ERole.ROLE_ADMIN.name())) {
            if (Objects.nonNull(query) && !query.isEmpty())
                return service.getListBySearchQuery(query)
                    .stream()
                    .map(userMapper::userToDto)
                    .toList();
            else
                return service.getListPage()
                    .map(userMapper::userToDto);
        } else {
            return service.getListBySearchQuery(query)
                .stream()
                .map(userMapper::userToDto)
                .toList();
        }
    }


}
