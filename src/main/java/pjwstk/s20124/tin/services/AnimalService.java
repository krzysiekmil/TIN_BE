package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.exception.BadRequestException;
import pjwstk.s20124.tin.model.Animal;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.mapper.AnimalMapper;
import pjwstk.s20124.tin.repository.AnimalRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService  {

    private final AnimalRepository repository;
    private final AnimalMapper animalMapper;
    private final UserService userService;


    public Animal create(Animal entity) {
        User owner = SecurityUtils.getCurrentUserLogin()
            .map(userService::loadUserByUsername)
            .map(User.class::cast)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        entity.setUser(owner);
        return repository.save(entity);
    }

    public Animal update(Animal data) throws ResponseStatusException {
        Animal entity = repository.findById(data.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        boolean owner = Optional.of(entity)
                .map(Animal::getUser)
                .map(User::getUsername)
                .equals(SecurityUtils.getCurrentUserLogin());

        if(!owner){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        animalMapper.updateAnimal(entity, data);

        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Animal> getOne(Long id) {
        return repository.findById(id);
    }

    public Collection<Animal> getList() {
        return repository.findAll();
    }

    public Collection<Animal> getMyList() {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return repository.findAnimalsByUserUsername(username);
    }
}
