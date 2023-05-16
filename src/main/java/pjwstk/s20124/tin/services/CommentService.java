package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjwstk.s20124.tin.model.Comment;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.input.PostCommentIncomeDto;
import pjwstk.s20124.tin.repository.CommentRepository;
import pjwstk.s20124.tin.repository.PostRepository;
import pjwstk.s20124.tin.repository.UserRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.security.Security;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public Comment addComment(Long postId, PostCommentIncomeDto incomeDto){
        Comment comment = Comment.builder()
            .post(postRepository.getReferenceById(postId))
            .content(incomeDto.content())
            .author(userRepository.getReferenceByUsername(SecurityUtils.getCurrentUserLogin().get()))
            .build();


        return commentRepository.save(comment);
    }
}
