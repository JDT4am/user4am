package org.jdt16.user4a.dto.response;

import lombok.Data;

@Data
public class UserResponse {
=======
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
  
    @JsonProperty("name")
    private String userEntityDTOName;

    @JsonProperty("ageDesc")
    private String userEntityDTOAge;

    @JsonProperty("email")
    private String userEntityDTOEmail;

    @JsonProperty("genderDesc")
    private String userEntityDTOGender;

    @JsonProperty("statusDesc")
    private String userEntityDTOStatus;

}
