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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    void postCreate_invalidGender_returns400() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(createRestApiResponse(HttpStatus.BAD_REQUEST, "Gender only 1 or 2", null));

        String invalidJson = """
                {
                    "name" : "bye",
                    "age" : 34,
                    "email" : "bye@gmail.com",
                    "gender": 3
                }
                """;

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", containsString("Gender only 1 or 2")))
                .andExpect(jsonPath("$.results").doesNotExist());
    }

    @Test
    void putUpdateUser_success_returns200_andBody() throws Exception {
        UUID id = UUID.randomUUID();

        UserResponse body = new UserResponse();
        body.setUserEntityDTOName("Updated Budi");
        body.setUserEntityDTOAge("30 tahun");
        body.setUserEntityDTOEmail("updated@example.com");
        body.setUserEntityDTOGender("Laki-Laki");
        body.setUserEntityDTOStatus("aktif");

        when(userService.updateUser(eq(id), any()))
                .thenReturn(createRestApiResponse(HttpStatus.OK, "User Updated!", body));

        mockMvc.perform(put("/user/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.results.name", is("Updated Budi")));
    }

    @Test
    void putUpdateUser_duplicateName_returns409() throws Exception {
        UUID id = UUID.randomUUID();

        when(userService.updateUser(eq(id), any()))
                .thenReturn(createRestApiResponse(HttpStatus.CONFLICT, "User name already exists", null));

        mockMvc.perform(put("/user/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.results").doesNotExist());
    }

    @Test
    void putUpdateUser_invalidGender_returns400() throws Exception {
        UUID id = UUID.randomUUID();

        when(userService.updateUser(eq(id), any()))
                .thenReturn(createRestApiResponse(HttpStatus.BAD_REQUEST, "Gender only 1 or 2", null));

        mockMvc.perform(put("/user/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson().replace("1", "0")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", containsString("Gender only 1 or 2")))
                .andExpect(jsonPath("$.results").doesNotExist());
    }

    @Test
    void putUpdateUser_notFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();

        when(userService.updateUser(eq(id), any()))
                .thenReturn(createRestApiResponse(HttpStatus.NOT_FOUND, "User not found", null));

        mockMvc.perform(put("/user/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.results").doesNotExist());
    }


    @Test
    void testListAllUserNoUsers() throws Exception {
        when(userService.findAllUsers())
                .thenReturn(createRestApiResponse(HttpStatus.OK, "Get All User Success", List.of()));

        mockMvc.perform(get("/user/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Get All User Success")))
                .andExpect(jsonPath("$.results", hasSize(0)));
    }

    @Test
    void testListAllUserManyUsers() throws Exception {
        when(userService.findAllUsers())
                .thenReturn(createRestApiResponse(HttpStatus.OK, "Get All User Success", userResponses));

        mockMvc.perform(get("/user/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Get All User Success")))
                .andExpect(jsonPath("$.results", hasSize(userResponses.size())))
                .andExpect(jsonPath("$.results[0].name", is(user1.getUserEntityDTOName())));
    }

    @Test
    void testUpdateUserStatusPositive() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.updateStatusUser(userId))
                .thenReturn(createRestApiResponse(HttpStatus.OK, "User status updated successfully", user1));

        mockMvc.perform(patch("/user/update/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("User status updated successfully")))
                .andExpect(jsonPath("$.results.name", is(user1.getUserEntityDTOName())));
    }

    @Test
    void testUpdateUserStatusNegative() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.updateStatusUser(userId))
                .thenReturn(createRestApiResponse(HttpStatus.NOT_FOUND, "User not found", null));

        mockMvc.perform(patch("/user/update/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("User not found")))
                .andExpect(jsonPath("$.results").doesNotExist());
    }
}
