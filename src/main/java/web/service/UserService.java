package web.service;

import web.dto.UserRequestDto;
import web.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

     void save(AppUser user);

     void delete(Long id);

     void update(AppUser user);

     Optional<AppUser> findById(Long id);

     List<AppUser> findAll();

    boolean findByUsername(String username);

    boolean findByEmail(String email);

    void saveFromDto(UserRequestDto dto);
    void updateFromDto(Long id, UserRequestDto dto);
}
