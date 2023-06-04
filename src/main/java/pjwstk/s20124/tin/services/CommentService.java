package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjwstk.s20124.tin.model.Comment;
import pjwstk.s20124.tin.model.dto.input.CommentIncomeDto;
import pjwstk.s20124.tin.model.dto.input.CommentType;
import pjwstk.s20124.tin.repository.CommentRepository;
import pjwstk.s20124.tin.repository.InfoChangeRepository;
import pjwstk.s20124.tin.repository.PostRepository;
import pjwstk.s20124.tin.repository.UserRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final InfoChangeRepository infoChangeRepository;

    public Comment addComment(CommentIncomeDto incomeDto){
        Comment.CommentBuilder commentBuilder = Comment.builder()
            .content(incomeDto.content())
            .author(userRepository.getReferenceByUsername(SecurityUtils.getCurrentUserLogin().get()));

         if(incomeDto.type().equals(CommentType.POST)){
             commentBuilder.post(postRepository.getReferenceById(incomeDto.id()));
         }
         else {
             commentBuilder.infoChange(infoChangeRepository.getReferenceById(incomeDto.id()));
         }

         Comment comment = commentBuilder.build();

        return commentRepository.save(comment);
    }
}
