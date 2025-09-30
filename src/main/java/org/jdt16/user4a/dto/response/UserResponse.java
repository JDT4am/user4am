package org.jdt16.user4a.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {
    @JsonProperty("userName")
    private String userEntityDTOName;

    @JsonProperty("userAge")
    private String userEntityDTOAge;

    @JsonProperty("userEmail")
    private String userEntityDTOEmail;

    @JsonProperty("userGender")
    private String userEntityDTOGender;

    @JsonProperty("userStatus")
    private String userEntityDTOStatus;
}
