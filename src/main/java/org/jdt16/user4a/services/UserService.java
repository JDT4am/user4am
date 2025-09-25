package org.jdt16.user4a.services;

import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    RestApiResponse<Page<UserResponse>> findAll(Integer page, Integer size, String sortBy, Boolean desc);
}
