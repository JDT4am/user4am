package org.jdt16.user4a.services;

import org.jdt16.user4a.dto.request.UserRequest;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;

public interface UserService {
    RestApiResponse<UserResponse> saveUser(UserRequest userRequest);

    RestApiResponse<UserResponse> updateUser(UserRequest userRequest);

    RestApiResponse<UserResponse> updateStatusUser(UserRequest userRequest);
}
