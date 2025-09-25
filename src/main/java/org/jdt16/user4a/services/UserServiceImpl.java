package org.jdt16.user4a.services;

import lombok.RequiredArgsConstructor;
import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public RestApiResponse<Page<UserResponse>> findAll(Integer page, Integer size, String sortBy, Boolean desc) {
        Pageable pageable = PageRequest.of(page, size, desc ? Sort.by(sortBy).descending() : Sort.by(sortBy));
        Page<UserResponse> userResponsePage = userRepository
                .findAll(pageable)
                .map(UserServiceImpl::userDTOToUserResponse);
        return createRestApiResponse(HttpStatus.OK, "List all users success", userResponsePage);
    }


    private <T> RestApiResponse<T> createRestApiResponse(HttpStatus httpStatus, String message, T result) {
        return RestApiResponse.<T>builder()
                .restApiResponseCode(httpStatus.value())
                .restApiResponseResults(result)
                .restApiResponseMessage(message)
                .build();
    }



    private static UserResponse userDTOToUserResponse(UserDTO userDTO) {
        return new UserResponse();
    }

}
