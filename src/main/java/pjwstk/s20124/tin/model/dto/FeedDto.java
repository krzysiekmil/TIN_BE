package pjwstk.s20124.tin.model.dto;

import lombok.Builder;
import lombok.Data;
import pjwstk.s20124.tin.model.Comment;
import pjwstk.s20124.tin.model.dto.output.CommentOutputDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
public class FeedDto {
    private String type;
    private String title;
    private String content;
    private Long contentId;
    private String author;
    private Long authorId;
    private LocalDateTime lastModifiedDate;
    private List<CommentOutputDto> comments;
    private boolean owner;
    private String image;
}
