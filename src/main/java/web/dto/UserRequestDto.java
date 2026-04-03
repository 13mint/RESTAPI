package web.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public class UserRequestDto {
    private Long id;
    @NotBlank(message = "Username cannot be empty")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Username can contain only letters and numbers"
    )
    private String username;

    @NotBlank(message = "First name cannot be empty")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "First name must contain only letters"
    )
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "Last name must contain only letters"
    )
    private String lastName;

    @NotNull(message = "Age cannot be empty")
    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email")
    private String email;

    private String password;

    @NotEmpty(message = "Select at least one role")
    private List<Long> roleIds;

    public UserRequestDto(){

    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){this.id = id; }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age){
        this.age = age;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getRoleIds(){
        return roleIds;
    }

    public void setRolesIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
