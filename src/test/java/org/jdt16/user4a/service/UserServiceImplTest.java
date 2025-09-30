package org.jdt16.user4a.service;

import org.jdt16.user4a.dto.entity.UserDTO;
import org.jdt16.user4a.dto.request.UserRequest;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.repository.UserRepository;
import org.jdt16.user4a.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Kelas unit test untuk implementasi {@link UserServiceImpl}.
 * <p>
 * Kelas ini menguji fungsi-fungsi utama pada lapisan service user,
 * mencakup pembuatan user, pembaruan, pembaruan status, dan penanganan error
 * dengan menggunakan mock pada {@link UserRepository}.
 * </p>
 * <p>
 * Menggunakan JUnit 5 dan Mockito untuk mocking dependensi dan verifikasi hasil.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    /**
     * Mock {@link UserRepository} untuk simulasi operasi database selama pengujian.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Instance {@link UserServiceImpl} dengan dependensi yang sudah disuntikkan mock.
     */
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Objek {@link UserRequest} contoh yang digunakan berulang pada beberapa metode test.
     */
    private UserRequest sampleUserRequest;

    /**
     * UUID contoh sebagai identitas unik user yang digunakan dalam pengujian.
     */
    private UUID sampleUserId;

    /**
     * Metode setup yang dijalankan sebelum masing-masing test.
     * <p>
     * Menginisialisasi {@code sampleUserId} dan {@code sampleUserRequest} dengan data uji yang konsisten.
     * </p>
     */
    @BeforeEach
    void setUp() {
        sampleUserId = UUID.randomUUID();
        sampleUserRequest = new UserRequest();
        sampleUserRequest.setUserEntityDTOId(sampleUserId);
        sampleUserRequest.setUserEntityDTOName("I yan");
        sampleUserRequest.setUserEntityDTOAge("21 Tahun");
        sampleUserRequest.setUserEntityDTOEmail("test@gmail.com");
        sampleUserRequest.setUserEntityDTOGender(1);
        sampleUserRequest.setUserEntityDTOStatus(1);
    }

    /**
     * Test untuk metode {@link UserServiceImpl#saveUser(UserRequest)}.
     * <p>
     * Memastikan user baru tersimpan dengan benar dan response memiliki kode HTTP CREATED,
     * pesan yang sesuai, dan data user yang benar.
     * </p>
     */
    @Test
    void saveUserTest_shouldSaveAndReturnCreatedResponse() {
        Mockito.doAnswer(invocationOnMock -> {
            UserDTO userDTO = invocationOnMock.getArgument(0);
            assertNotNull(userDTO.getUserEntityDTOId(), "ID user tidak boleh null");
            assertEquals("I yan", userDTO.getUserEntityDTOName(), "Nama user harus sesuai dengan yang diharapkan");
            return null;
        }).when(userRepository).save(any(UserDTO.class));

        RestApiResponse<UserResponse> response = userService.saveUser(sampleUserRequest);

        assertEquals(HttpStatus.CREATED.value(), response.getRestApiResponseCode());
        assertEquals("User Created!", response.getRestApiResponseMessage());
        assertEquals("I yan", response.getRestApiResponseResults().getUserEntityDTOName());
        verify(userRepository, times(1)).save(any(UserDTO.class));
    }

    /**
     * Test untuk metode {@link UserServiceImpl#updateUser(UserRequest)} ketika user tidak ditemukan.
     * <p>
     * Memastikan RuntimeException dilempar dan tidak ada penyimpanan dilakukan
     * saat ID user tidak ditemukan.
     * </p>
     */
    @Test
    void updateUser_userNotFound_shouldThrowException() {
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () -> userService.updateUser(sampleUserRequest));
        assertTrue(e.getMessage().contains("User not found"), "Pesan exception harus mengandung 'User not found'");

        verify(userRepository, times(1)).findById(sampleUserId);
        verify(userRepository, never()).save(any(UserDTO.class));
    }

    /**
     * Test untuk metode {@link UserServiceImpl#updateStatusUser(UserRequest)} ketika user ada.
     * <p>
     * Memastikan status user diperbarui dengan benar, metode save dipanggil,
     * dan response berisi nilai yang sesuai.
     * </p>
     */
    @Test
    void updateStatusUser_existingUser_shouldUpdateStatusAndReturnOk() {
        UserDTO existingUser = new UserDTO(
                sampleUserId,
                "John Doe",
                "30 Tahun",
                "john.doe@example.com",
                1,
                0
        );
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        RestApiResponse<UserResponse> response = userService.updateStatusUser(sampleUserRequest);

        assertEquals(HttpStatus.OK.value(), response.getRestApiResponseCode());
        assertEquals("User Status Updated!", response.getRestApiResponseMessage());
        assertEquals("1", response.getRestApiResponseResults().getUserEntityDTOStatus());

        verify(userRepository, times(1)).findById(sampleUserId);
        verify(userRepository, times(1)).save(existingUser);
    }
}
