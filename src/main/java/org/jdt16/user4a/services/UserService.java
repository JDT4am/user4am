package org.jdt16.user4a.services;

import org.jdt16.user4a.dto.request.UserRequest;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 🔹 Update status user by ID
    RestApiResponse<UserResponse> updateStatusUser(UUID userId);

    RestApiResponse<List<UserResponse>> findAllUsers();

    RestApiResponse<UserResponse> createUser(UserRequest request);

    RestApiResponse<UserResponse> updateUser(UUID userId, UserRequest userRequest);
}
