package org.jdt16.user4a.services;

import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.request.UserRequest;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private UserDTO userDTO;
    private UserRequest sampleUserRequest;
    private UUID sampleUserId;

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
        userId = UUID.randomUUID();
        userDTO = new UserDTO();
        userDTO.setUserEntityDTOId(userId);
        userDTO.setUserEntityDTOName("Fahri");
        userDTO.setUserEntityDTOAge(25);
        userDTO.setUserEntityDTOEmail("fahri@example.com");
        userDTO.setUserEntityDTOGender(1);
        userDTO.setUserEntityDTOStatus(0);

        sampleUserId = UUID.randomUUID();
        sampleUserRequest = new UserRequest();
        sampleUserRequest.setUserEntityDTOName("I yan");
        sampleUserRequest.setUserEntityDTOAge(21);
        sampleUserRequest.setUserEntityDTOEmail("test@gmail.com");
        sampleUserRequest.setUserEntityDTOGender(1);
    }
    @Test
    void createUser_success_returns201_andMapsResponse() {
        when(userRepository.existsByUserEntityDTOName("I yan")).thenReturn(false);

        ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);

        UserDTO saved = new UserDTO();
        saved.setUserEntityDTOName("I yan");
        saved.setUserEntityDTOAge(21);
        saved.setUserEntityDTOEmail("test@gmail.com");
        saved.setUserEntityDTOGender(1);
        saved.setUserEntityDTOStatus(0); // default
        when(userRepository.save(any(UserDTO.class))).thenReturn(saved);

        RestApiResponse<UserResponse> resp = userService.createUser(sampleUserRequest);

        verify(userRepository).existsByUserEntityDTOName("I yan");
        verify(userRepository).save(captor.capture());

        UserDTO toSave = captor.getValue();
        assertThat(toSave.getUserEntityDTOStatus()).isEqualTo(0);
        assertThat(toSave.getUserEntityDTOName()).isEqualTo("I yan");

        assertThat(resp.getRestApiResponseCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(resp.getRestApiResponseResults()).isNotNull();

        assertThat(resp.getRestApiResponseResults().getUserEntityDTOStatus()).isEqualTo("non-aktif");
        assertThat(resp.getRestApiResponseResults().getUserEntityDTOGender()).isEqualTo("Laki-Laki");
    }

    @Test
    void createUser_duplicateName_returns409_andNotSaved() {
        when(userRepository.existsByUserEntityDTOName("I yan")).thenReturn(true);
        RestApiResponse<UserResponse> resp = userService.createUser(sampleUserRequest);

        assertThat(resp.getRestApiResponseCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(resp.getRestApiResponseResults()).isNull();
        verify(userRepository, never()).save(any(UserDTO.class));
    }

    @Test
    void createUser_trimNameBeforeDuplicateCheck() {
        UserRequest req = new UserRequest();
        req.setUserEntityDTOName("  Budi  ");
        req.setUserEntityDTOAge(20);
        req.setUserEntityDTOEmail("budi@example.com");
        req.setUserEntityDTOGender(1);

        when(userRepository.existsByUserEntityDTOName("Budi")).thenReturn(false);
        when(userRepository.save(any(UserDTO.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.createUser(req);

        verify(userRepository).existsByUserEntityDTOName("Budi");
        verify(userRepository).save(any(UserDTO.class));
    }

    @Test
    void createUser_invalidGender_returns400() {
        // Arrange
        UserRequest req = new UserRequest();
        req.setUserEntityDTOName("Invalid Gender");
        req.setUserEntityDTOAge(22);
        req.setUserEntityDTOEmail("invalid@example.com");
        req.setUserEntityDTOGender(3); // ❌ invalid

        // Act
        RestApiResponse<UserResponse> resp = userService.createUser(req);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), resp.getRestApiResponseCode());
        assertEquals("Gender only 1 or 2", resp.getRestApiResponseMessage());
        assertNull(resp.getRestApiResponseResults());

        verify(userRepository, never()).save(any(UserDTO.class));
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
    void updateUser_userNotFound_shouldThrowException() {
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());

        RestApiResponse<UserResponse> response = userService.updateUser(sampleUserId, sampleUserRequest);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getRestApiResponseCode());
        assertNull(response.getRestApiResponseResults());
        assertEquals("User not found", response.getRestApiResponseMessage());

        verify(userRepository, times(1)).findById(sampleUserId);
        verify(userRepository, never()).save(any(UserDTO.class));
    }

    @Test
    void updateUser_success_returns200_andUpdatesFields() {
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(userDTO));
        when(userRepository.save(any(UserDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestApiResponse<UserResponse> response = userService.updateUser(sampleUserId, sampleUserRequest);

        verify(userRepository, times(1)).findById(sampleUserId);
        ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);
        verify(userRepository, times(1)).save(captor.capture());

        UserDTO savedUser = captor.getValue();
        assertEquals("I yan", savedUser.getUserEntityDTOName());
        assertEquals(21, savedUser.getUserEntityDTOAge());
        assertEquals("test@gmail.com", savedUser.getUserEntityDTOEmail());
        assertEquals(1, savedUser.getUserEntityDTOGender());

        assertEquals(HttpStatus.OK.value(), response.getRestApiResponseCode());
        assertEquals("User Updated!", response.getRestApiResponseMessage());
        assertNotNull(response.getRestApiResponseResults());
        assertEquals("I yan", response.getRestApiResponseResults().getUserEntityDTOName());
        assertEquals("non-aktif", response.getRestApiResponseResults().getUserEntityDTOStatus());
    }

    @Test
    void updateUser_duplicateName_returns409_andNotSaved() {
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(userDTO));
        when(userRepository.existsByUserEntityDTOName("I yan")).thenReturn(true);

        RestApiResponse<UserResponse> response = userService.updateUser(sampleUserId, sampleUserRequest);

        verify(userRepository, times(1)).findById(sampleUserId);
        verify(userRepository, times(1)).existsByUserEntityDTOName("I yan");
        verify(userRepository, never()).save(any(UserDTO.class));

        assertEquals(HttpStatus.CONFLICT.value(), response.getRestApiResponseCode());
        assertEquals("User name already exists", response.getRestApiResponseMessage());
        assertNull(response.getRestApiResponseResults());
    }

    @Test
    void updateUser_invalidGender_returns400() {
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(userDTO));

        UserRequest req = new UserRequest();
        req.setUserEntityDTOName("Still Valid Name");
        req.setUserEntityDTOAge(30);
        req.setUserEntityDTOEmail("stillvalid@example.com");
        req.setUserEntityDTOGender(0);

        RestApiResponse<UserResponse> resp = userService.updateUser(sampleUserId, req);

        assertEquals(HttpStatus.BAD_REQUEST.value(), resp.getRestApiResponseCode());
        assertEquals("Gender only 1 or 2", resp.getRestApiResponseMessage());
        assertNull(resp.getRestApiResponseResults());

        verify(userRepository, never()).save(any(UserDTO.class));
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
