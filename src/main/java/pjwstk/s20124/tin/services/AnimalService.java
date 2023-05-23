package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.model.Animal;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.mapper.AnimalMapper;
import pjwstk.s20124.tin.repository.AnimalRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService  {

    private final AnimalRepository repository;
    private final AnimalMapper animalMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;


    public Animal create(Animal entity, MultipartFile multipartFile) {
        User owner = SecurityUtils.getCurrentUserLogin()
            .map(userService::loadUserByUsername)
            .map(User.class::cast)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        entity.setUser(owner);
        String imagePath = fileStorageService.store(multipartFile);
        entity.setImage(imagePath);

        return repository.save(entity);
    }

    @Transactional
    public Animal update(Long id, Animal dto, MultipartFile multipartFile) throws ResponseStatusException {
        Animal entity = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        boolean owner = Optional.of(entity)
                .map(Animal::getUser)
                .map(User::getUsername)
                .equals(SecurityUtils.getCurrentUserLogin());

        if(!owner){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(Objects.isNull(dto.getImage())){
            this.fileStorageService.deleteFile(entity.getImage());
            entity.setImage(null);
        }

        animalMapper.updateAnimal(entity, dto);

        if(Objects.nonNull(multipartFile)){
            String imagePath = fileStorageService.store(multipartFile);
            fileStorageService.deleteFile(entity.getImage());
            entity.setImage(imagePath);
        }

        return entity;
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
