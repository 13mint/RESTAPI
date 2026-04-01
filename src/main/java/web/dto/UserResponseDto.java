package web.dto;

import java.util.List;

public class UserResponseDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private List<String> roles;

    public UserResponseDto(Long id, String username,String firstName, String lastName,int age, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.roles = roles;
    }


    public Long getId(){
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }

    public int getAge(){
        return age;
    }

    public List<String> getRoles(){
        return roles;
    }
}
