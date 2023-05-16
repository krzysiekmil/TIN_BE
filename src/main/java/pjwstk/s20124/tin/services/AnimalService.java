package pjwstk.s20124.tin.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.model.AbstractEntity;
import pjwstk.s20124.tin.model.Animal;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.mapper.AnimalMapper;
import pjwstk.s20124.tin.repository.AnimalRepository;
import pjwstk.s20124.tin.repository.EventRepository;
import pjwstk.s20124.tin.repository.PostRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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

    @Transactional
    public Collection<Animal> getList() {return repository.findAll();}

    public Collection<Animal> getMyList() {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return repository.findAnimalsByUserUsername(username);
    }
}
