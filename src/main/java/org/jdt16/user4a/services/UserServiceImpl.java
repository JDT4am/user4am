package org.jdt16.user4a.services;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // 🔹 Update status user by ID
    @Override
    public RestApiResponse<UserResponse> updateStatusUser(UUID userId) {
        Optional<UserDTO> optionalUser = userRepository.findById(userId);
        log.info("ini user: {}", optionalUser);

        if (optionalUser.isEmpty()) {
            return createRestApiResponse(HttpStatus.NOT_FOUND, null, "User not found");
        }

        UserDTO userDTO = optionalUser.get();
        userDTO.setUserEntityDTOStatus(1);

        UserDTO updatedEntity = userRepository.save(userDTO);

        UserResponse result = userEntityDTOToUserResponse(updatedEntity);

        return createRestApiResponse(HttpStatus.OK, result, "User status updated successfully");
    }

    @Override
    public RestApiResponse<List<UserResponse>> findAllUsers() {
        return createRestApiResponse(
                    HttpStatus.OK,
                     "All users retrieved successfully",
                      userRepository
                            .findAll()
                            .stream()
                            .map(UserServiceImpl::userDTOToUserResponse)
                            .toList());
      }
    }

    // 🔹 Mapping entity → response
    private static UserResponse userEntityDTOToUserResponse(UserDTO userDTO) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserEntityDTOName(userDTO.getUserEntityDTOName());
        userResponse.setUserEntityDTOAge(userDTO.getUserEntityDTOAge() + " tahun");
        userResponse.setUserEntityDTOEmail(userDTO.getUserEntityDTOEmail());
        userResponse.setUserEntityDTOGender(userDTO.getUserEntityDTOGender() ? "Laki-Laki" : "Perempuan");
        userResponse.setUserEntityDTOStatus(userDTO.getUserEntityDTOStatus() ? "aktif" : "non-aktif");
        return userResponse;
    }

    // 🔹 Template response wrapper
    private <T> RestApiResponse<T> createRestApiResponse(HttpStatus httpStatus, T result, String message) {
        return RestApiResponse.<T>builder()
                .restApiResponseCode(httpStatus.value())
                .restApiResponseResults(result)
                .restApiResponseMessage(message)
                .build();
    }
}
