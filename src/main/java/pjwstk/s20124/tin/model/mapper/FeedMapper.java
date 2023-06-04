package pjwstk.s20124.tin.model.mapper;

import org.hibernate.dialect.function.AggregateWindowEmulationQueryTransformer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.AbstractEntity;
import pjwstk.s20124.tin.model.Animal;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.InfoChange;
import pjwstk.s20124.tin.model.Post;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.FeedDto;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.Objects;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {CommentMapper.class})
public interface FeedMapper {

    @Mapping(target = "author", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "authorId", source = "user.id")
    @Mapping(target = "contentId", source = "id")
    @Mapping(target = "userRelated", source = ".", qualifiedByName = "getUserRelatedName")
    @Mapping(target = "userRelatedId", source = ".", qualifiedByName = "getUserRelatedId")
    @Mapping(target = "eventRelated", source = ".", qualifiedByName = "getEventRelatedName")
    @Mapping(target = "eventRelatedId", source = ".", qualifiedByName = "getEventRelatedId")
    @Mapping(target = "animalRelated", source = ".", qualifiedByName = "getAnimalRelatedName")
    @Mapping(target = "animalRelatedId", source = ".", qualifiedByName = "getAnimalRelatedId")
    FeedDto mapToFeed(InfoChange infoChange);

    @Mapping(target = "author", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "contentId", source = "id")
    @Mapping(target = "type", constant = "POST")
    @Mapping(target = "owner", source = ".", qualifiedByName = "isOwner")
    FeedDto mapToFeed(Post post);

    @Named("isOwner")
    default boolean isOwner(Post post) {
        String authorUsername = post.getAuthor().getUsername();

        return SecurityUtils.getCurrentUserLogin()
            .map(authorUsername::equals)
            .orElse(false);
    }

    @Named("getFullName")
    default String getFullName(AbstractEntity abstractEntity) {
        return switch (abstractEntity) {
            case InfoChange infoChange -> infoChange.getUser().getFirstName() + " " + infoChange.getUser().getLastName();
            case Post post -> post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName();
            default -> throw new IllegalStateException("Unexpected value: " + abstractEntity);
        };
    }

    @Named("getUserRelatedName")
    default String getUserRelatedName(InfoChange infoChange){
        return Optional.of(infoChange)
            .map(InfoChange::getUserRelatedTo)
            .map(User::getFullName)
            .orElse(null);
    }

    @Named("getUserRelatedId")
    default Long getUserRelatedId(InfoChange infoChange){
        return Optional.of(infoChange)
            .map(InfoChange::getUserRelatedTo)
            .map(User::getId)
            .orElse(null);
    }

    @Named("getEventRelatedName")
    default String getEventRelatedName(InfoChange infoChange){
        return Optional.of(infoChange)
            .map(InfoChange::getEventRelatedTo)
            .map(Event::getTitle)
            .orElse(null);
    }

    @Named("getEventRelatedId")
    default Long getEventRelatedId(InfoChange infoChange){
        return Optional.of(infoChange)
            .map(InfoChange::getEventRelatedTo)
            .map(Event::getId)
            .orElse(null);
    }

    @Named("getAnimalRelatedName")
    default String getAnimalRelatedName(InfoChange infoChange){
        return Optional.of(infoChange)
            .map(InfoChange::getAnimalRelatedTo)
            .map(Animal::getName)
            .orElse(null);
    }

    @Named("getAnimalRelatedId")
    default Long getAnimalRelatedId(InfoChange infoChange){
        return Optional.of(infoChange)
            .map(InfoChange::getAnimalRelatedTo)
            .map(Animal::getId)
            .orElse(null);
    }

    default FeedDto mapToFeed(AbstractEntity entity) {
        return switch (entity) {
            case InfoChange infoChange -> mapToFeed(infoChange);
            case Post post -> mapToFeed(post);
            default -> throw new IllegalStateException("Unexpected value: " + entity);
        };
    }
}
