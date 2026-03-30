package web.controller;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.AppUser;
import web.service.RoleService;
import web.service.UserService;

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


    @PostMapping("/newUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody AppUser newUser, BindingResult bindingResult, Model model){
        if (userService.findByUsername(newUser.getUsername())){
            return ResponseEntity.badRequest().body(Map.of("username", "Username already exists"));
        }

        if (userService.findByEmail(newUser.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("email", "Email already exists");
        }

        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult);
        }
        userService.save(newUser);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("id") Long id){
        userService.delete(id);
        return ResponseEntity.ok().body("User was deleted");
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateUser(@Valid @RequestBody AppUser editUser, BindingResult bindingResult, Model model){
        AppUser existingUser = userService.findById(editUser.getId()).orElseThrow();
        editUser.setId(existingUser.getId());

        if (editUser.getRoles() == null || editUser.getRoles().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("roles", "User must have at least one role");
        }

        if (!existingUser.getUsername().equals(editUser.getUsername())
                && userService.findByUsername(editUser.getUsername())) {

            return ResponseEntity.badRequest().body(Map.of("username","Username already exists"));
        }

        if (!existingUser.getEmail().equals(editUser.getEmail())
                && userService.findByEmail(editUser.getEmail())) {

            return ResponseEntity.badRequest().body(Map.of("email","Email already exists"));
        }

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult);
        }

        userService.update(editUser);
        return ResponseEntity.ok(model);

    }
}
