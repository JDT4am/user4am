package org.jdt16.user4a.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "userName cannot be blank")
    @JsonProperty("userName")
    private String userEntityDTOName;

    @NotBlank(message = "userAge cannot be blank")
    @JsonProperty("userAge")
    private Integer userEntityDTOAge;

    @NotBlank(message = "userEmail cannot be blank")
    @JsonProperty("userEmail")
    private String userEntityDTOEmail;

    @NotBlank(message = "userGender cannot be null")
    @JsonProperty("userGender")
    private Integer userEntityDTOGender;
}
