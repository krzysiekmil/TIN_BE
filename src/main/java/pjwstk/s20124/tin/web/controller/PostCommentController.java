package pjwstk.s20124.tin.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s20124.tin.model.Comment;
import pjwstk.s20124.tin.model.dto.input.PostCommentIncomeDto;
import pjwstk.s20124.tin.model.dto.output.CommentOutputDto;
import pjwstk.s20124.tin.model.mapper.CommentMapper;
import pjwstk.s20124.tin.services.CommentService;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentOutputDto addComments(@PathVariable Long postId, @RequestBody PostCommentIncomeDto incomeDto){
        Comment comment = commentService.addComment(postId, incomeDto);

        return commentMapper.mapToOutputDto(comment);
    }
}
