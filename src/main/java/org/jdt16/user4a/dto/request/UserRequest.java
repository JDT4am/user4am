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
    @JsonProperty("userName")
    private String userEntityDTOName;

    @NotNull(message = "userAge cannot be blank")
    @Min(0)
    @JsonProperty("userAge")
    private Integer userEntityDTOAge;

    @NotBlank(message = "userEmail cannot be blank")
    @JsonProperty("userEmail")
    private String userEntityDTOEmail;

    @NotNull(message = "userGender cannot be null")
    @Min(0)
    @Max(1)
    @JsonProperty("userGender")
    private Integer userEntityDTOGender;
}
