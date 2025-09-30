package org.jdt16.user4a.service;

import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.repository.UserRepository;
import org.jdt16.user4a.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private UserDTO userDTO;

    private static UserResponse userEntityDTOToUserResponse(UserDTO userDTO) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserEntityDTOName(userDTO.getUserEntityDTOName());
        userResponse.setUserEntityDTOAge(userDTO.getUserEntityDTOAge() + " tahun");
        userResponse.setUserEntityDTOEmail(userDTO.getUserEntityDTOEmail());
        userResponse.setUserEntityDTOGender(userDTO.getUserEntityDTOGender() == 1 ? "Laki-Laki" : "Perempuan");
        userResponse.setUserEntityDTOStatus(userDTO.getUserEntityDTOStatus() == 1 ? "aktif" : "non-aktif");
        return userResponse;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        userDTO = new UserDTO();
        userDTO.setUserEntityDTOId(userId);
        userDTO.setUserEntityDTOName("Fahri");
        userDTO.setUserEntityDTOAge(25);
        userDTO.setUserEntityDTOEmail("fahri@example.com");
        userDTO.setUserEntityDTOGender(1);
        userDTO.setUserEntityDTOStatus(0);
    }

    @Test
    void testUpdateStatusUser_UserExists() {
        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDTO));
        when(userRepository.save(any(UserDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call service
        RestApiResponse<UserResponse> response = userService.updateStatusUser(userId);

        // Verify repository save was called
        ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);
        verify(userRepository).save(captor.capture());
        UserDTO savedUser = captor.getValue();

        assertEquals(1, savedUser.getUserEntityDTOStatus());
        assertEquals(HttpStatus.OK.value(), response.getRestApiResponseCode());
        assertEquals("User status updated successfully", response.getRestApiResponseMessage());
        assertEquals(savedUser.getUserEntityDTOName(), response.getRestApiResponseResults().getUserEntityDTOName());
    }

    @Test
    void testUpdateStatusUser_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RestApiResponse<UserResponse> response = userService.updateStatusUser(userId);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getRestApiResponseCode());
        assertNull(response.getRestApiResponseResults());
        assertEquals("User not found", response.getRestApiResponseMessage());
    }

    @Test
    void testFindAllUsers() {
        UserDTO user2 = new UserDTO();
        user2.setUserEntityDTOId(UUID.randomUUID());
        user2.setUserEntityDTOName("Budi");
        user2.setUserEntityDTOAge(30);
        user2.setUserEntityDTOEmail("budi@example.com");
        user2.setUserEntityDTOGender(1);
        user2.setUserEntityDTOStatus(1);

        when(userRepository.findAll()).thenReturn(Arrays.asList(userDTO, user2));

        RestApiResponse<List<UserResponse>> response = userService.findAllUsers();

        assertEquals(HttpStatus.OK.value(), response.getRestApiResponseCode());
        assertEquals(2, response.getRestApiResponseResults().size());
        assertEquals("All users retrieved successfully", response.getRestApiResponseMessage());

        assertEquals(
                List.of(userEntityDTOToUserResponse(userDTO), userEntityDTOToUserResponse(user2)),
                response.getRestApiResponseResults()
        );
    }

    @Test
    void testFindAllUsersEmpty() {
        when(userRepository.findAll()).thenReturn(List.of());

        RestApiResponse<List<UserResponse>> response = userService.findAllUsers();

        assertEquals(HttpStatus.OK.value(), response.getRestApiResponseCode());
        assertEquals("All users retrieved successfully", response.getRestApiResponseMessage());
        assertTrue(response.getRestApiResponseResults().isEmpty());
    }
}
