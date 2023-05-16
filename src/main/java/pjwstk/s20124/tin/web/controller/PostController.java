package pjwstk.s20124.tin.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s20124.tin.model.Post;
import pjwstk.s20124.tin.model.dto.input.PostIncomeDto;
import pjwstk.s20124.tin.model.dto.output.PostOutputDto;
import pjwstk.s20124.tin.model.mapper.PostMapper;
import pjwstk.s20124.tin.services.PostService;

import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping
    public PostOutputDto create(@Valid @RequestBody PostIncomeDto dto) {
        Post postDto = postMapper.mapIncomeDtoToEntity(dto);

        return postMapper.mapToOutputDto(postService.create(postDto));
    }


    @PutMapping("/{id}")
    public PostOutputDto update(@PathVariable Long id, @Valid @RequestBody PostIncomeDto dto) throws Exception {
        Post postDto = postMapper.mapIncomeDtoToEntity(dto);

        return postMapper.mapToOutputDto( postService.update(id, postDto));
    }

    @DeleteMapping
    public void delete(Long id) {
        postService.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<PostOutputDto> getOne(@PathVariable Long id) {
        return postService.getOne(id)
            .map(postMapper::mapToOutputDto);
    }

    @GetMapping
    public Page<PostOutputDto> getList(
        @RequestParam(required = false) Long authorId,
        @RequestParam(required = false) String author,
        @RequestParam(required = false, defaultValue = "0") int pageNo,
        @RequestParam(required = false, defaultValue = "createDate") String sort,
        @RequestParam(required = false, defaultValue = "DESC") String dir
        ) {
        return postService.getList(authorId, author, pageNo, sort, dir);
    }
}
