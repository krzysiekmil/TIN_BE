package pjwstk.s20124.tin.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s20124.tin.web.exception.BadRequestException;
import pjwstk.s20124.tin.model.Animal;
import pjwstk.s20124.tin.model.dto.AnimalDto;
import pjwstk.s20124.tin.model.mapper.AnimalMapper;
import pjwstk.s20124.tin.services.AnimalService;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class AnimalController  {

    private final AnimalService service;
    private final AnimalMapper animalMapper;

    @PostMapping
    public AnimalDto create(@RequestBody AnimalDto dto) {
        Animal entity = animalMapper.dtoToAnimal(dto);
        Animal animal = service.create(entity);
        return animalMapper.animalToDto(animal);
    }
    @PutMapping
    public AnimalDto update(@RequestBody AnimalDto dto) {
        if(dto.getId() == null){
            throw new BadRequestException();
        }

        Animal entity = animalMapper.dtoToAnimal(dto);
        Animal user = service.update(entity);

        return animalMapper.animalToDto(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<AnimalDto> getOne(@PathVariable Long id) {
        return service.getOne(id).map(animalMapper::animalToDto);
    }

    @GetMapping
    public Collection<AnimalDto> getList() {
        return service.getList()
            .stream()
            .map(animalMapper::animalToDto)
            .toList();
    }

    @GetMapping("/me")
    public Collection<AnimalDto> getMyList(){
        return service.getMyList()
            .stream()
            .map(animalMapper::animalToDto)
            .toList();
    }
}
