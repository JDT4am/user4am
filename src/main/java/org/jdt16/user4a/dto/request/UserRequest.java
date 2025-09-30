package org.jdt16.user4a.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "userName cannot be blank")
    @JsonProperty("name")
    private String userEntityDTOName;

    @NotNull(message = "userAge cannot be blank")
    @Min(0)
    @JsonProperty("age")
    private Integer userEntityDTOAge;

    @NotBlank(message = "userEmail cannot be blank")
    @JsonProperty("email")
    private String userEntityDTOEmail;

    @NotNull(message = "userGender cannot be null")
    @JsonProperty("gender")
    private Integer userEntityDTOGender;
}
