package org.jdt16.user4a.controller;

import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<UserResponse> userResponses;

    private <T> RestApiResponse<T> createRestApiResponse(HttpStatus httpStatus, String message, T result) {
        return RestApiResponse.<T>builder()
                .restApiResponseCode(httpStatus.value())
                .restApiResponseResults(result)
                .restApiResponseMessage(message)
                .build();
    }

    @BeforeEach
    void setUp() {
        UserResponse user1 = new UserResponse();
        user1.setUserEntityDTOName("Nathaniel Audrian");
        user1.setUserEntityDTOAge("23 tahun");
        user1.setUserEntityDTOEmail("nathaniel@example.com");
        user1.setUserEntityDTOGender("Laki-Laki");
        user1.setUserEntityDTOStatus("aktif");

        UserResponse user2 = new UserResponse();
        user2.setUserEntityDTOName("Alice Johnson");
        user2.setUserEntityDTOAge("30 tahun");
        user2.setUserEntityDTOEmail("alice@example.com");
        user2.setUserEntityDTOGender("Perempuan");
        user2.setUserEntityDTOStatus("non-aktif");

        UserResponse user3 = new UserResponse();
        user3.setUserEntityDTOName("Bob Smith");
        user3.setUserEntityDTOAge("28 tahun");
        user3.setUserEntityDTOEmail("bob@example.com");
        user3.setUserEntityDTOGender("Laki-Laki");
        user3.setUserEntityDTOStatus("aktif");

        userResponses = List.of(user1, user2, user3);
    }

    @Test
    void testListAllUserNoUsers() {
        Mockito.when(userService.findAllUsers()).thenReturn(
                createRestApiResponse(HttpStatus.OK,
                        "Get All User Success",
                        List.of()));

        ResponseEntity<RestApiResponse<List<UserResponse>>> response = userController.getAllUsers();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertTrue(response.getBody().getRestApiResponseResults().isEmpty());
        Assertions.assertEquals(200, response.getBody().getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getBody().getRestApiResponseMessage());

    }

    @Test
    void testListAllUserOneUser() {
        List<UserResponse> users = userResponses.subList(0, 0);
        Mockito.when(userService.findAllUsers()).thenReturn(
                createRestApiResponse(
                        HttpStatus.OK,
                        "Get All User Success",
                        users));

        RestApiResponse<List<UserResponse>> response = userController.getAllUsers().getBody();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertEquals(users, response.getRestApiResponseResults());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());
    }

    @Test
    void testListAllUserManyUsers() {
        Mockito.when(userService.findAllUsers()).thenReturn(
                createRestApiResponse(
                        HttpStatus.OK,
                        "Get All User Success",
                        userResponses));

        RestApiResponse<List<UserResponse>> response = userController.getAllUsers().getBody();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertEquals(userResponses, response.getRestApiResponseResults());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());
    }
}
