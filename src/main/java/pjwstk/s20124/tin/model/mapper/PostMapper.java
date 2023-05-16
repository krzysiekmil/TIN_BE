package pjwstk.s20124.tin.model.mapper;

import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.Post;
import pjwstk.s20124.tin.model.dto.input.PostIncomeDto;
import pjwstk.s20124.tin.model.dto.output.PostOutputDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapper.class})
public interface PostMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "author", source=".", qualifiedByName = "getFullName")
    @Mapping(target = "animalId", source = "animal.id")
    @Mapping(target = "animal", source="animal.name")
    PostOutputDto mapToOutputDto(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateEntity(@MappingTarget Post entity, Post dto);
    Post mapIncomeDtoToEntity(PostIncomeDto dto);

    @Named("getFullName")
    default String getFullName(Post post){
        return post.getAuthor().getFirstName() + " "  +  post.getAuthor().getLastName();
    }

}
