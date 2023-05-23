package pjwstk.s20124.tin.model.dto.output;


import lombok.Builder;
import lombok.Data;
import pjwstk.s20124.tin.model.Comment;

import java.util.Set;


@Data
@Builder
public class PostOutputDto {
    private Long id;

    private String title;
    private String content;

    private String author;
    private Long authorId;

    private String animal;
    private Long animalId;

    private String image;

    private Set<CommentOutputDto> comments;
}
