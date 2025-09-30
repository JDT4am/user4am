package org.jdt16.user4a.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.request.UserRequest;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public RestApiResponse<UserResponse> saveUser(UserRequest userRequest) {
        UserDTO userDto = new UserDTO(
                UUID.randomUUID(),
                userRequest.getUserEntityDTOName(),
                userRequest.getUserEntityDTOAge(),
                userRequest.getUserEntityDTOEmail(),
                userRequest.getUserEntityDTOGender(),
                userRequest.getUserEntityDTOStatus()
        );
        userRepository.save(userDto);

        UserResponse userResponse = userEntityDTOToUserResponse(userDto);
        return RestApiResponse.<UserResponse>builder()
                .restApiResponseCode(HttpStatus.CREATED.value())
                .restApiResponseResults(userResponse)
                .restApiResponseMessage("User Created!")
                .build();
    }

    @Override
    public RestApiResponse<UserResponse> updateUser(UserRequest userRequest) {
        UUID userId = userRequest.getUserEntityDTOId();
        UserDTO existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existingUser.setUserEntityDTOName(userRequest.getUserEntityDTOName());
        existingUser.setUserEntityDTOAge(userRequest.getUserEntityDTOAge());
        existingUser.setUserEntityDTOEmail(userRequest.getUserEntityDTOEmail());
        existingUser.setUserEntityDTOGender(userRequest.getUserEntityDTOGender());
        existingUser.setUserEntityDTOStatus(userRequest.getUserEntityDTOStatus());

        userRepository.save(existingUser);

        UserResponse userResponse = userEntityDTOToUserResponse(existingUser);
        return RestApiResponse.<UserResponse>builder()
                .restApiResponseCode(HttpStatus.OK.value())
                .restApiResponseResults(userResponse)
                .restApiResponseMessage("User Updated!")
                .build();
    }

    @Override
    public RestApiResponse<UserResponse> updateStatusUser(UserRequest userRequest) {
        UUID userId = userRequest.getUserEntityDTOId();
        UserDTO existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        existingUser.setUserEntityDTOStatus(1);

        userRepository.save(existingUser);

        UserResponse userResponse = userEntityDTOToUserResponse(existingUser);
        return RestApiResponse.<UserResponse>builder()
                .restApiResponseCode(HttpStatus.OK.value())
                .restApiResponseResults(userResponse)
                .restApiResponseMessage("User Status Updated!")
                .build();
    }
    
    private static UserResponse userEntityDTOToUserResponse(UserDTO userDTO) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserEntityDTOName(userDTO.getUserEntityDTOName());
        userResponse.setUserEntityDTOAge(userDTO.getUserEntityDTOAge());
        userResponse.setUserEntityDTOEmail(userDTO.getUserEntityDTOEmail());
        userResponse.setUserEntityDTOGender(String.valueOf(userDTO.getUserEntityDTOGender()));
        userResponse.setUserEntityDTOStatus(String.valueOf(userDTO.getUserEntityDTOStatus()));
        return userResponse;
    }

}