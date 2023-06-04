package pjwstk.s20124.tin.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s20124.tin.model.Comment;
import pjwstk.s20124.tin.model.dto.input.CommentIncomeDto;
import pjwstk.s20124.tin.model.dto.output.CommentOutputDto;
import pjwstk.s20124.tin.model.mapper.CommentMapper;
import pjwstk.s20124.tin.services.CommentService;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentOutputDto addComments(@RequestBody CommentIncomeDto incomeDto){
        Comment comment = commentService.addComment(incomeDto);

        return commentMapper.mapToOutputDto(comment);
    }
}
