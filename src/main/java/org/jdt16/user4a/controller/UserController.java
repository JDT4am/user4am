package org.jdt16.user4a.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdt16.user4a.dto.request.UserRequest;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.services.UserService;
import org.jdt16.user4a.utility.RestApiPathUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RestApiPathUtility.API_PATH_USER)
public class UserController {
    private final UserService userService;

    @PostMapping(RestApiPathUtility.API_PATH_CREATE)
    public ResponseEntity<RestApiResponse<UserResponse>> createUser(
             @Valid @RequestBody UserRequest request
    ) {
        RestApiResponse<UserResponse> response = userService.createUser(request);
        return ResponseEntity.status(response.getRestApiResponseCode()).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<RestApiResponse<List<UserResponse>>> getAllUsers() {
        RestApiResponse<List<UserResponse>> response = userService.findAllUsers();
        return ResponseEntity.status(response.getRestApiResponseCode()).body(response);
    }

    @PatchMapping(RestApiPathUtility.API_PATH_UPDATE + RestApiPathUtility.API_PATH_BY_USER_ID)
    public ResponseEntity<RestApiResponse<UserResponse>> updateUserStatus(
            @PathVariable("id") UUID userId
    ) throws Exception {
        RestApiResponse<UserResponse> response = userService.updateStatusUser(userId);
        return ResponseEntity.status(response.getRestApiResponseCode()).body(response);
    }

    @PutMapping(RestApiPathUtility.API_PATH_UPDATE + RestApiPathUtility.API_PATH_BY_USER_ID)
    public ResponseEntity<RestApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @RequestBody UserRequest userRequest) {
        log.info("Update User -- userRequest.id = {}", id);
        RestApiResponse<UserResponse> response = userService.updateUser(id, userRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getRestApiResponseCode()));
    }
}
