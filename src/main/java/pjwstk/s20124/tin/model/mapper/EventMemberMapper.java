package pjwstk.s20124.tin.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.EventMember;
import pjwstk.s20124.tin.model.Post;
import pjwstk.s20124.tin.model.dto.output.EventMemberOutputDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMemberMapper {

    @Mapping(target = "user", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "userId", source = "eventMemberId.userId")
    EventMemberOutputDto mapToOutputDto(EventMember eventMember);

    @Named("getFullName")
    default String getFullName(EventMember eventMember){
        return eventMember.getUser().getFirstName() + " "  +  eventMember.getUser().getLastName();
    }
}
