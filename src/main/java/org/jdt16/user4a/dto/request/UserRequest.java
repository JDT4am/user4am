package org.jdt16.user4a.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRequest {
    @JsonProperty("Name")
    private String userDTOName;

    @JsonProperty("Email")
    private String userDTOEmail;

    @JsonProperty("Umur")
    private String userDTOAge;

    @JsonProperty("Gender")
    private String userDTOGender;



}
