package web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.AppUser;
import web.repository.RoleRepository;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {

        this.userService = userService;
        this.roleService = roleService;
    }

//    @GetMapping
//    public String adminPage(Model model) {
//        model.addAttribute("users", userService.findAll());
//        model.addAttribute("roles", roleService.findAll());
//
//        model.addAttribute("newUser", new AppUser());
//        model.addAttribute("editUser", new AppUser());
//        return "admin";
//    }
//
//    @PostMapping("/newUser")
//    public String createUser(@Valid @ModelAttribute("newUser") AppUser newUser, BindingResult bindingResult, Model model){
//        if (userService.findByUsername(newUser.getUsername())) {
//            bindingResult.rejectValue("username", "", "Username already exists");
//        }
//
//        if (userService.findByEmail(newUser.getEmail())) {
//            bindingResult.rejectValue("email", "", "Email already exists");
//        }
//
//        if(bindingResult.hasErrors()){
//            model.addAttribute("users", userService.findAll());
//            model.addAttribute("roles", roleService.findAll());
//            model.addAttribute("newUser", newUser);
//            model.addAttribute("editUser", new AppUser());
//            return "admin";
//        }
//
//        userService.save(newUser);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/delete")
//    public String deleteUser(@RequestParam("id") Long id) {
//        userService.delete(id);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/edit")
//    public String updateUser(@Valid @ModelAttribute("editUser") AppUser editUser, BindingResult  bindingResult, Model model) {
//        AppUser existingUser = userService.findById(editUser.getId()).orElseThrow();
//        editUser.setId(existingUser.getId());
//
//        if (editUser.getRoles() == null || editUser.getRoles().isEmpty()) {
//            bindingResult.rejectValue("roles", "error.roles", "User must have at least one role");
//        }
//
//        if (!existingUser.getUsername().equals(editUser.getUsername())
//                && userService.findByUsername(editUser.getUsername())) {
//
//            bindingResult.rejectValue("username", "", "Username already exists");
//        }
//
//        if (!existingUser.getEmail().equals(editUser.getEmail())
//                && userService.findByEmail(editUser.getEmail())) {
//
//            bindingResult.rejectValue("email", "", "Email already exists");
//        }
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("users", userService.findAll());
//            model.addAttribute("roles", roleService.findAll());
//            model.addAttribute("newUser", new AppUser());
//            model.addAttribute("editUser", editUser);
//            model.addAttribute("editError", true);
//            return "admin";
//        }
//        userService.update(editUser);
//        return "redirect:/admin";
//    }
}
