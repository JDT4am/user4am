package org.jdt16.user4a.services;

import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    RestApiResponse<List<UserResponse>> findAll();
}
