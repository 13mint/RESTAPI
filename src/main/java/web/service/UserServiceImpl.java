package web.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import web.dto.UserRequestDto;
import web.model.Role;
import web.repository.RoleRepository;
import web.repository.UserRepository;
import org.springframework.stereotype.Service;
import web.model.AppUser;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {

        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(AppUser user) {
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow();

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(roleUser));
        }

        if(repo.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        if(repo.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }

    @Transactional
    @Override
    public void update(AppUser updatedUser) {

        AppUser user = repo.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getId() == null) {
            throw new RuntimeException("ID is null");
        }

        if (updatedUser.getRoles() == null || updatedUser.getRoles().isEmpty()) {
            throw new RuntimeException("User must have at least one role");
        }

        user.setUsername(updatedUser.getUsername());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());
        user.setRoles(updatedUser.getRoles());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        repo.save(user);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<AppUser> findAll() {
        return repo.findAll();
    }

    @Override
    public boolean findByUsername(String username) {
        return repo.findByUsername(username).isPresent();
    }

    @Override
    public boolean findByEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }

    @Override
    public void saveFromDto(UserRequestDto dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRoles(new HashSet<>(roleRepository.findAllById(dto.getRolesIds())));

        save(user);
    }

    @Override
    public void updateFromDto(Long id, UserRequestDto dto) {
        AppUser user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setRoles(new HashSet<>(roleRepository.findAllById(dto.getRolesIds())));
        repo.save(user);
    }


}
