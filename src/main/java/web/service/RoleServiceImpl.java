package web.service;

import org.springframework.stereotype.Service;
import web.model.Role;
import web.repository.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean findByName(String name) {
        return roleRepository.findByName(name).isPresent();
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
