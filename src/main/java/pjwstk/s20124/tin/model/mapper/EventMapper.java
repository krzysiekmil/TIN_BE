package pjwstk.s20124.tin.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.EventMember;
import pjwstk.s20124.tin.model.dto.input.EventIncomeDto;
import pjwstk.s20124.tin.model.dto.output.EventOutputDto;
import pjwstk.s20124.tin.utils.SecurityUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EventMemberMapper.class})
public interface EventMapper {


    @Mapping(target = "members", ignore = true)
    Event mapInputDtoToEntity(EventIncomeDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "host", ignore = true)
    @Mapping(target = "members", ignore = true)
    void updateEntity(@MappingTarget Event entity, Event dto);


    @Mapping(target = "host", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "hostId", source = "host.id")
    @Mapping(target = "owner", source = ".", qualifiedByName = "isOwner")
    EventOutputDto mapToOutputDto(Event event);

    @Named("getFullName")
    default String getFullName(Event event){
        return event.getHost().getFirstName() + " "  +  event.getHost().getLastName();
    }

    @Named("isOwner")
    default boolean isOwner(Event event){
        return SecurityUtils.getCurrentUserLogin().orElse("").equals(event.getHost().getUsername());
    }
}
