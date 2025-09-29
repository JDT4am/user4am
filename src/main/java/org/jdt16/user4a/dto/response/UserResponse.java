package org.jdt16.user4a.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {

    @JsonProperty("id")
    private UUID userEntityDTOId;

    @JsonProperty("name")
    private String userEntityDTOName;

    @JsonProperty("age")
    private Integer userEntityDTOAge;

    @JsonProperty("email")
    private String userEntityDTOEmail;

    @JsonProperty("gender")
    private Integer userEntityDTOGender;

    @JsonProperty("status")
    private Integer userEntityDTOStatus;

}
