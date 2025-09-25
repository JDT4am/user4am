package org.jdt16.user4a.controller;

import lombok.RequiredArgsConstructor;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public RestApiResponse<?> listAllUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") Boolean desc) {
        return userService.findAll(page, size, sortBy, desc);
    }


}
