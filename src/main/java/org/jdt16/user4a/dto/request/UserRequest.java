package org.jdt16.user4a.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRequest {

    @JsonProperty
    private String userEntityDTOName;

    @JsonProperty
    private String userEntityDTOAge;

    @JsonProperty
    private String userEntityDTOEmail;

    @JsonProperty
    private String userEntityDTOStatus;

}
