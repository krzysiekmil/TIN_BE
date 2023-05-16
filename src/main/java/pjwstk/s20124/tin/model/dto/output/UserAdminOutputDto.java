package pjwstk.s20124.tin.model.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pjwstk.s20124.tin.model.ERole;
import pjwstk.s20124.tin.model.Role;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class UserAdminOutputDto extends UserOutputDto{
    private Set<Role> roles;

}
