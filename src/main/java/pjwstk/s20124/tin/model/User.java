package pjwstk.s20124.tin.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;
import org.mapstruct.control.MappingControl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Valid
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Date dateOfBirth;
    @Email
    private String email;
    @NotNull
    private String password;

    private String image;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "user_friends",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends = new LinkedHashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "user_friends",
        joinColumns = @JoinColumn(name = "friend_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> friendOf = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "user_invitation",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "invited_user_id"))
    private Set<User> invitations = new LinkedHashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "user_invitation",
        joinColumns = @JoinColumn(name = "invited_user_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> invitedBy = new LinkedHashSet<>();

    @Builder.Default
    @ColumnDefault(value = "true")
    private boolean accountNonExpired = true;

    @Builder.Default
    @ColumnDefault(value = "true")
    private boolean accountNonLocked = true;

    @Builder.Default
    @ColumnDefault(value = "true")
    private boolean credentialsNonExpired = true;

    @Builder.Default
    @ColumnDefault(value = "true")
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    @OneToMany(orphanRemoval = true, mappedBy = "user")
    @Builder.Default
    private Set<Animal> animals = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(Role::getName)
            .map(ERole::name)
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
