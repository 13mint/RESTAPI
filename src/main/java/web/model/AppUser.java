package web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Pattern(
            regexp ="^[a-zA-Z0-9]+$",
            message = "Username can contain only letters and numbers"
    )
    private String username;

    @Column(name = "first_name")
    @NotBlank
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "First name must contain only letters"
    )
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "Last name must contain only letters"
    )
    private String lastName;

    @NotNull(message = "Age cannot be empty")
    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Email
    private String email;

    private String password;

    @NotEmpty(message = "User must have at least one role")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))

    private Set<Role> roles = new HashSet<>();

    public AppUser(Set<Role> roles) {
        this.roles = roles;
    }

    public AppUser(String username, String firstName, String lastName, int age, String email, String password, Set<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public AppUser() {

    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {return password;}
    public void setPassword(String password) { this.password = password;}

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) { this.roles = roles;}

    public String getRoleIds() {
        if (roles == null) return "";

        return roles.stream()
                .map(role -> role.getId().toString())
                .collect(java.util.stream.Collectors.joining(","));
    }

    public String getRolesNames() {
        return roles.stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.joining(" "));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
