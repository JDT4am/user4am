package org.jdt16.user4a.services;

import lombok.RequiredArgsConstructor;
import org.jdt16.user4a.dto.entity.*;
import org.jdt16.user4a.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    @Transactional
    public UserResponse create(UserDTO req) {

        if (repo.existsByNameIgnoreCase(req.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name already exists");
        }
        // validasi gender (sebenarnya sudah dari Bean Validation 1..2)
        if (req.getGender() != 1 && req.getGender() != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "gender must be 1 or 2");
        }

        UserEntity e = UserEntity.builder()
                .name(req.getName())
                .email(req.getEmail())
                .gender(req.getGender())
                .age(req.getAge())
                .status(0) // default 0
                .build();

        e = repo.save(e);

        return UserResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .email(e.getEmail())
                .gender(e.getGender())
                .age(e.getAge())
                .status(e.getStatus())
                .build();
    }
}
