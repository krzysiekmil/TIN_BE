package pjwstk.s20124.tin.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.ERole;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.UserDto;
import pjwstk.s20124.tin.model.dto.output.UserAdminOutputDto;
import pjwstk.s20124.tin.model.dto.output.UserInvitationsOutputDto;
import pjwstk.s20124.tin.model.dto.output.UserOutputDto;
import pjwstk.s20124.tin.utils.SecurityUtils;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDto userToDto(User user);
    User dtoToUser(UserDto dto);

    @Mapping(target = "isFriend", source = ".", qualifiedByName = "isFriend")
    @Mapping(target = "me", source = ".", qualifiedByName = "isMe")
    @Mapping(target = "invitationStatus", source = ".", qualifiedByName = "isInvitationSend")
    UserOutputDto userToOutputDto(User user);

    @Mapping(target = "isFriend", source = ".", qualifiedByName = "isFriend")
    @Mapping(target = "me", source = ".", qualifiedByName = "isMe")
    @Mapping(target = "invitationStatus", source = ".", qualifiedByName = "isInvitationSend")
    UserAdminOutputDto userToAdminOutputDto(User user);

    default UserOutputDto userToDtoByRole(User user){
        if(!SecurityUtils.hasCurrentUserThisAuthority(ERole.ROLE_ADMIN.name())){
            return userToOutputDto(user);
        }
        else {
            return userToAdminOutputDto(user);
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "friendOf", ignore = true)
    @Mapping(target = "invitations", ignore = true)
    @Mapping(target = "invitedBy", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "animals", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateEntity(@MappingTarget User entity, User dto);


    UserInvitationsOutputDto userToInvitationDto(User user);

    @Named("isFriend")
    default boolean isFriend(User user){
        String username = SecurityUtils.getCurrentUserLogin().orElse("");
        return user.getFriends().stream().map(User::getUsername).anyMatch(username::equals);
    }

    @Named("isMe")
    default boolean isMe(User user){
        String username = SecurityUtils.getCurrentUserLogin().orElse("");
        return user.getUsername().equals(username);
    }

    @Named("isInvitationSend")
    default String isInvitationSend(User user){
        String username = SecurityUtils.getCurrentUserLogin().orElse("");

        if(username.isEmpty()){
            return null;
        }

        if(user.getInvitedBy().stream().map(User::getUsername).anyMatch(username::equals)){
            return "INVITED";
        }

        if(user.getInvitations().stream().map(User::getUsername).anyMatch(username::equals)){
            return "INVITATION_RECEIVED";
        }

        return null;
    }
}
