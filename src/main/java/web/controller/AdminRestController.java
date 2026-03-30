package web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.dto.UserRequestDto;
import web.dto.UserResponseDto;
import web.model.AppUser;
import web.model.Role;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        return userService.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private UserResponseDto convertToDto(AppUser user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .toList()
        );
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUserById(@PathVariable Long id){
        AppUser user = userService.findById(id).orElseThrow();
        return convertToDto(user);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto dto, BindingResult bindingResult){
        if (userService.findByUsername(dto.getUsername())){
            return ResponseEntity.badRequest().body(Map.of("username", "Username already exists"));
        }

        if (userService.findByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("email", "Email already exists"));
        }

        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult);
        }
        userService.saveFromDto(dto);
        return ResponseEntity.ok(Map.of("message", "User created successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id){
        userService.delete(id);
        return ResponseEntity.ok(Map.of("message","User was deleted"));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto dto, BindingResult bindingResult){
        AppUser existingUser = userService.findById(id).orElseThrow();

        if (!existingUser.getUsername().equals(dto.getUsername())
                && userService.findByUsername(dto.getUsername())) {

            return ResponseEntity.badRequest().body(Map.of("username","Username already exists"));
        }

        if (!existingUser.getEmail().equals(dto.getEmail())
                && userService.findByEmail(dto.getEmail())) {

            return ResponseEntity.badRequest().body(Map.of("email","Email already exists"));
        }

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult);
        }

        userService.updateFromDto(id, dto);
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));

    }
}
