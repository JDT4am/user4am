package org.jdt16.user4a.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdt16.user4a.dto.request.UserRequest;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.UserResponse;
import org.jdt16.user4a.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        userService = Mockito.mock(UserService.class);

        userController = new UserController(userService);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setValidator(validator)
                .build();
    }


    private String validJson() throws Exception {
        UserRequest req = new UserRequest();
        req.setUserEntityDTOName("Budi");
        req.setUserEntityDTOAge(25);
        req.setUserEntityDTOEmail("budi@example.com");
        req.setUserEntityDTOGender(1);
        return objectMapper.writeValueAsString(req);
    }

    private <T> RestApiResponse<T> wrap(HttpStatus status, String msg, T result) {
        return RestApiResponse.<T>builder()
                .restApiResponseCode(status.value())
                .restApiResponseResults(result)
                .restApiResponseMessage(msg)
                .build();
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
                .thenReturn(wrap(HttpStatus.CREATED, "User created successfully", body));

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
                .thenReturn(wrap(HttpStatus.CONFLICT, "User name already exists", null));

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(409)));
    }

    @Test
    void postCreate_validationError_returns400() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(wrap(HttpStatus.BAD_REQUEST, "Validation error", null));

        String invalidJson = """
                {
                  "userName": "Budi",
                  "userAge": 25,
                  "userEmail": "budi@example.com"
                }
                """;

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)));
    }

    @Test
    void testListAllUserNoUsers() {
        Mockito.when(userService.findAllUsers())
                .thenReturn(wrap(HttpStatus.OK, "Get All User Success", List.of()));

        RestApiResponse<List<UserResponse>> response = userController.getAllUsers().getBody();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getRestApiResponseResults().isEmpty());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());
    }

    @Test
    void testListAllUserOneUser() {
        List<UserResponse> users = List.of(new UserResponse());
        Mockito.when(userService.findAllUsers())
                .thenReturn(wrap(HttpStatus.OK, "Get All User Success", users));

        RestApiResponse<List<UserResponse>> response = userController.getAllUsers().getBody();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(users, response.getRestApiResponseResults());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());
    }

    @Test
    void testListAllUserManyUsers() {
        List<UserResponse> users = List.of(new UserResponse(), new UserResponse(), new UserResponse());
        Mockito.when(userService.findAllUsers())
                .thenReturn(wrap(HttpStatus.OK, "Get All User Success", users));

        RestApiResponse<List<UserResponse>> response = userController.getAllUsers().getBody();

        Mockito.verify(userService, Mockito.times(1)).findAllUsers();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(users, response.getRestApiResponseResults());
        Assertions.assertEquals(200, response.getRestApiResponseCode());
        Assertions.assertEquals("Get All User Success", response.getRestApiResponseMessage());
    }
}
