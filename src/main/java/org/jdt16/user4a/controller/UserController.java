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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(RestApiPathUtility.API_PATH + RestApiPathUtility.API_VERSION + RestApiPathUtility.API_PATH_USER)
public class UserController {

    private final UserService userService;

    @PostMapping(RestApiPathUtility.API_PATH_CREATE)
    public ResponseEntity<RestApiResponse<?>> insertUser(
            @Valid @RequestBody UserRequest userRequest
    ) {
        log.info("userRequest.name = {}", userRequest.getUserEntityDTOName());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRequest));
    }

    @PutMapping(RestApiPathUtility.API_PATH_UPDATE + RestApiPathUtility.API_PATH_BY_USER_ID)
    public ResponseEntity<RestApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @RequestBody UserRequest userRequest) {
        userRequest.setUserEntityDTOId(id);
        log.info("Update User -- userRequest.id = {}", userRequest.getUserEntityDTOId());
        RestApiResponse<UserResponse> response = userService.updateUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getRestApiResponseCode()));
    }

    @PatchMapping(RestApiPathUtility.API_PATH_UPDATE + RestApiPathUtility.API_PATH_BY_USER_ID)
    public ResponseEntity<RestApiResponse<UserResponse>> updateStatusUser(
            @PathVariable UUID id,
            @RequestBody UserRequest userRequest) {
        userRequest.setUserEntityDTOId(id);
        log.info("Update Status User -- userRequest.id = {}", userRequest.getUserEntityDTOId());
        RestApiResponse<UserResponse> response = userService.updateStatusUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getRestApiResponseCode()));
    }
}
