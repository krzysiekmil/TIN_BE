package pjwstk.s20124.tin.model.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjwstk.s20124.tin.model.dto.AnimalDto;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class UserOutputDto {
    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private boolean isFriend;
    private String invitationStatus;
    private boolean me;
    private Set<AnimalDto> animals;
    private Set<UserInvitationsOutputDto> friends;
}


