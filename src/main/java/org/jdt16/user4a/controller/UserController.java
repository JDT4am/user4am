package org.jdt16.user4a.controller;

import lombok.RequiredArgsConstructor;
import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.entity.UserResponse;
import org.jdt16.user4a.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody UserDTO req) {
        return service.create(req);
    }
}
