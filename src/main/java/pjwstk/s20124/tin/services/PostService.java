package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.model.ERole;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.Post;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.input.PostCommentIncomeDto;
import pjwstk.s20124.tin.model.dto.output.PostOutputDto;
import pjwstk.s20124.tin.model.mapper.PostMapper;
import pjwstk.s20124.tin.repository.PostRepository;
import pjwstk.s20124.tin.repository.UserRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    public Page<PostOutputDto> getList(Long authorId, String author, int pageNo, String order, String dir) {

        User user = User.builder()
            .id(authorId)
            .build();

        Post post = Post.builder()
            .author(user)
            .build();

        Example<Post> example = Example.of(post, ExampleMatcher.matchingAny());

        Sort sort = Sort.by(order);

        Pageable pageable = PageRequest.of(pageNo, 20, sort);

        return postRepository.findAll(example, pageable)
            .map(postMapper::mapToOutputDto);
    }

    public Post create(Post post, MultipartFile multipartFile){
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        post.setAuthor(user);
        String imagePath = fileStorageService.store(multipartFile);
        post.setImage(imagePath);

        return postRepository.save(post);
    }

    public Optional<Post> getOne(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public Post update(Long id, Post postDto, MultipartFile multipartFile) {
        Post entity = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        if((Objects.nonNull(postDto.getAuthor()) && !Objects.equals(postDto.getAuthor().getId(), currentUser.getId())) && !SecurityUtils.hasCurrentUserThisAuthority(ERole.ROLE_ADMIN.name())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if(Objects.isNull(postDto.getImage())){
            this.fileStorageService.deleteFile(entity.getImage());
            entity.setImage(null);
        }

        postMapper.updateEntity(entity, postDto);

        if(Objects.nonNull(multipartFile)){
            String imagePath = fileStorageService.store(multipartFile);
            fileStorageService.deleteFile(entity.getImage());
            entity.setImage(imagePath);
        }

        return entity;
    }

    public void delete(Long id) {
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!Objects.equals(post.getAuthor().getId(), currentUser.getId()) && !SecurityUtils.hasCurrentUserThisAuthority(ERole.ROLE_ADMIN.name())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        postRepository.deleteById(id);
    }
}
