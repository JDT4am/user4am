package org.jdt16.user4a.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdt16.user4a.controller.handler.RestControllerAdviceHandler;
import org.jdt16.user4a.dto.request.UserRequest;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Import(RestControllerAdviceHandler.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<UserResponse> userResponses;

    private String validJson() throws Exception {
        UserRequest req = new UserRequest();
        req.setUserEntityDTOName("Budi");
        req.setUserEntityDTOAge(25);
        req.setUserEntityDTOEmail("budi@example.com");
        req.setUserEntityDTOGender(1);
        return objectMapper.writeValueAsString(req);
    }
    private <T> RestApiResponse<T> createRestApiResponse(HttpStatus httpStatus, String message, T result) {
        return RestApiResponse.<T>builder()
                .restApiResponseCode(httpStatus.value())
                .restApiResponseResults(result)
                .restApiResponseMessage(message)
                .build();
    }

    UserResponse user1 = new UserResponse();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        userService = Mockito.mock(UserService.class);

        userController = new UserController(userService);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setValidator(validator)
                .build();

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
    void postCreate_success_returns201_andBody() throws Exception {
        // Stub service -> 201
        UserResponse body = new UserResponse();
        body.setUserEntityDTOName("Budi");
        body.setUserEntityDTOAge("25 tahun");
        body.setUserEntityDTOEmail("budi@example.com");
        body.setUserEntityDTOGender("Laki-Laki");
        body.setUserEntityDTOStatus("non-aktif");

        when(userService.createUser(any()))
                .thenReturn(createRestApiResponse(HttpStatus.CREATED, "User created successfully", body));

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(201)))
                .andExpect(jsonPath("$.message", containsString("created")))
                .andExpect(jsonPath("$.results.name", is("Budi")))
                .andExpect(jsonPath("$.results.statusDesc", is("non-aktif")));
    }

    @Test
    void postCreate_duplicateName_returns409() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(createRestApiResponse(HttpStatus.CONFLICT, "User name already exists", null));

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(409)));
    }

//    @Test
//    void postCreate_validationError_returns400() throws Exception {
//        when(userService.createUser(any()))
//                .thenReturn(createRestApiResponse(HttpStatus.BAD_REQUEST, "Validation error", null));
//
//        String invalidJson = """
//                {
//                  "userName": "Budi",
//                  "userAge": 25,
//                  "userEmail": "budi@example.com"
//                }
//                """;
//
//        mockMvc.perform(post("/user/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(invalidJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code", is(400)));
//    }

    @Test
    void testListAllUserNoUsers() {
        Mockito.when(userService.findAllUsers()).thenReturn(
                createRestApiResponse(HttpStatus.OK,
                        "Get All User Success",
                        List.of()));

        ResponseEntity<RestApiResponse<List<UserResponse>>> response = userController.getAllUsers();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
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

        ResponseEntity<RestApiResponse<List<UserResponse>>> response = userController.getAllUsers();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(users, response.getBody().getRestApiResponseResults());
        Assertions.assertEquals(200, response.getBody().getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getBody().getRestApiResponseMessage());
    }

    @Test
    void testListAllUserManyUsers() {
        Mockito.when(userService.findAllUsers()).thenReturn(
                createRestApiResponse(
                        HttpStatus.OK,
                        "Get All User Success",
                        userResponses));

        ResponseEntity<RestApiResponse<List<UserResponse>>> response = userController.getAllUsers();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(userResponses, response.getBody().getRestApiResponseResults());
        Assertions.assertEquals(200, response.getBody().getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getBody().getRestApiResponseMessage());
    }

    @Test
    void testUpdateUserStatusPositive() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(userService.updateStatusUser(userId)).thenReturn(
                createRestApiResponse(HttpStatus.OK,"User status updated successfully", user1));
        ResponseEntity<RestApiResponse<UserResponse>> response = userController.updateUserStatus(userId);

        Mockito.verify(userService, Mockito.times(1)).updateStatusUser(userId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(user1, response.getBody().getRestApiResponseResults());
        Assertions.assertEquals(200, response.getBody().getRestApiResponseCode());
        Assertions.assertEquals("User status updated successfully", response.getBody().getRestApiResponseMessage());
    }

    @Test
    void testUpdateUserStatusNegative() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(userService.updateStatusUser(userId)).thenReturn(
                createRestApiResponse(HttpStatus.NOT_FOUND, "User not found", null));
        ResponseEntity<RestApiResponse<UserResponse>> response = userController.updateUserStatus(userId);

        Mockito.verify(userService, Mockito.times(1)).updateStatusUser(userId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(null, response.getBody().getRestApiResponseResults());
        Assertions.assertEquals(404, response.getBody().getRestApiResponseCode());
        Assertions.assertEquals("User not found", response.getBody().getRestApiResponseMessage());
    }
}
