package pjwstk.s20124.tin.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pjwstk.s20124.tin.model.Role;

import java.util.Date;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private Set<Role> roles;
    private String image;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
}
