package pjwstk.s20124.tin.model.dto.output;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CommentOutputDto {
    private Long id;
    private String author;
    private Long authorId;
    private Long post;
    private String content;
}
