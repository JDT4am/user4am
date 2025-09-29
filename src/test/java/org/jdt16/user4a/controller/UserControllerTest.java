package org.jdt16.user4a.controller;

import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    private <T> RestApiResponse<T> createRestApiResponse(HttpStatus httpStatus, String message, T result) {
        return RestApiResponse.<T>builder()
                .restApiResponseCode(httpStatus.value())
                .restApiResponseResults(result)
                .restApiResponseMessage(message)
                .build();
    }

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void testListAllUserNoUsers() {
        Mockito.when(userService.findAll()).thenReturn(createRestApiResponse(HttpStatus.OK, "Get All User Success", List.of()));

        RestApiResponse<List<UserResponse>> response = userController.listAllUsers();

        Mockito.verify(userService, Mockito.times(1)).findAll();

        Assertions.assertTrue(response.getRestApiResponseResults().isEmpty());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());

    }

    @Test
    void testListAllUserOneUser() {
        List<UserResponse> users = List.of(new UserResponse());
        Mockito.when(userService.findAll()).thenReturn(
                createRestApiResponse(
                        HttpStatus.OK,
                        "Get All User Success",
                        users));

        RestApiResponse<List<UserResponse>> response = userController.listAllUsers();

        Mockito.verify(userService, Mockito.times(1)).findAll();

        Assertions.assertEquals(users, response.getRestApiResponseResults());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());
    }

    @Test
    void testListAllUserManyUsers() {
        List<UserResponse> users = List.of(
                new UserResponse(),
                new UserResponse(),
                new UserResponse()
                );
        Mockito.when(userService.findAll()).thenReturn(
                createRestApiResponse(
                        HttpStatus.OK,
                        "Get All User Success",
                        users));

        RestApiResponse<List<UserResponse>> response = userController.listAllUsers();

        Mockito.verify(userService, Mockito.times(1)).findAll();

        Assertions.assertEquals(users, response.getRestApiResponseResults());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());
    }
}
