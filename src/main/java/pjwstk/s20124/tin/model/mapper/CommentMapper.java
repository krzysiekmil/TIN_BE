package pjwstk.s20124.tin.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.Comment;
import pjwstk.s20124.tin.model.Post;
import pjwstk.s20124.tin.model.dto.output.CommentOutputDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "author", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "post", source = "post.id")
    CommentOutputDto mapToOutputDto(Comment comment);

    @Named("getFullName")
    default String getFullName(Comment comment){
        return comment.getAuthor().getFirstName() + " "  +  comment.getAuthor().getLastName();
    }
}
