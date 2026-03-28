package web.service;

import web.model.Role;

import java.util.List;

public interface RoleService {
    boolean findByName(String name);
    List<Role> findAll();
}
