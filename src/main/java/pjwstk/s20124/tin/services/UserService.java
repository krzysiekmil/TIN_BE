package pjwstk.s20124.tin.services;

import io.micrometer.core.instrument.binder.db.MetricsDSLContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.common.CrudApi;
import pjwstk.s20124.tin.model.ERole;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.input.UserPasswordIncomeDto;
import pjwstk.s20124.tin.model.mapper.UserMapper;
import pjwstk.s20124.tin.repository.UserRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements  UserDetailsService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User create(User entity) {

        validateUser(entity);

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return repository.save(entity);
    }

    private boolean validateUser(User user){
        return !repository.existsByUsername(user.getUsername());
    }

    @Transactional
    public User update(Long id, User entity) {
        User currentUser = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));


        userMapper.updateEntity(currentUser, entity);

        if(SecurityUtils.hasCurrentUserThisAuthority(ERole.ROLE_ADMIN.name()))
        {
            currentUser.setRoles(entity.getRoles());
        }


        return currentUser;
    }

    @Transactional
    public User updateMe(User entity) {
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(this::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));


        userMapper.updateEntity(currentUser, entity);


        return currentUser;
    }

    @Transactional
    public User updatePassword(UserPasswordIncomeDto dto){
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(this::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        if(!passwordEncoder.matches(dto.password(), currentUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        currentUser.setPassword(passwordEncoder.encode(dto.newPassword()));

        return currentUser;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<User> getOne(Long id) {
        return repository.findById(id);
    }

    public Optional<User> getMe() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(this::findByUsername);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Collection<User> getList() {
        return repository.findAll();
    }

    public Collection<User> getListBySearchQuery(String searchQuery) {
        return repository.findAllBySearchQuery(searchQuery);
    }
    public Page<User> getListPage() {
        return repository.findAll(Pageable.ofSize(10));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow();
    }

    public Optional<Long> getIdByUsername(String username) {
        return repository.findIdByUsername(username);
    }

    public Collection<User> getFriendsList() {

        String username = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

       return repository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
            .getFriends();
    }

    @Transactional
    public void addToUserFriends(Long id) {
        final User currentUser = SecurityUtils.getCurrentUserLogin().flatMap(repository::findByUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        final User friend = repository.getReferenceById(id);

        currentUser.getFriends().add(friend);
        currentUser.getFriendOf().add(friend);
    }

    @Transactional
    public void removeFromFriends(Long id) {
        final User currentUser = SecurityUtils.getCurrentUserLogin().flatMap(repository::findByUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        final User friend = repository.getReferenceById(id);

        currentUser.getFriends().remove(friend);
        currentUser.getFriendOf().remove(friend);
    }

    @Transactional
    public void inviteToFriends(Long id) {
        final User currentUser = SecurityUtils.getCurrentUserLogin().flatMap(repository::findByUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        final User friend = repository.getReferenceById(id);

        currentUser.getInvitations().add(friend);
    }

    @Transactional
    public void rejectInvitation(Long id) {
        final User currentUser = SecurityUtils.getCurrentUserLogin().flatMap(repository::findByUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        final User friend = repository.getReferenceById(id);

        currentUser.getInvitations().remove(friend);
    }

    @Transactional
    public void acceptUserToFriends(Long id) {
        final User currentUser = SecurityUtils.getCurrentUserLogin().flatMap(repository::findByUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        final User friend = repository.getReferenceById(id);

        if(!currentUser.getInvitedBy().contains(friend)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        currentUser.getInvitedBy().remove(friend);

        currentUser.getFriends().add(friend);
        currentUser.getFriendOf().add(friend);
    }

    public Collection<User> getUserInvitation(){
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(repository::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
            .getInvitedBy();

    }
}
