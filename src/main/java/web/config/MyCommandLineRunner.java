package web.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import web.model.AppUser;
import web.model.Role;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MyCommandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        Set<Role> roles = new HashSet<>();

        if (userRepository.findByUsername("test").isEmpty()) {
            AppUser user = new AppUser();
            user.setUsername("test");
            user.setFirstName("test");
            user.setLastName("test");
            user.setAge(20);
            user.setEmail("test1@mail.com");
            user.setPassword(passwordEncoder.encode("test"));

            roles.add(userRole);
            user.setRoles(roles);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setAge(21);
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("admin"));

            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
        }

    }
}
