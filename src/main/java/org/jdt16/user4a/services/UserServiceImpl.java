package org.jdt16.user4a.services;

import lombok.RequiredArgsConstructor;
import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public RestApiResponse<List<UserResponse>> findAll() {
        return createRestApiResponse(
                HttpStatus.OK,
                "Get All User Success",
                userRepository
                        .findAll()
                        .stream()
                        .map(UserServiceImpl::userDTOToUserResponse)
                        .toList());
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
