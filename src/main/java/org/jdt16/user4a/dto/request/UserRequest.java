package org.jdt16.user4a.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "{error.not_blank.user_name}")
    @Size(min = 2, max = 15, message = "{error.size.user_name}")
    @JsonProperty("userName")
    private String userEntityDTOName;

    @NotBlank(message = "{error.not_blank.password}")
    @Size(min = 8, message = "{error.size.password}")
    @JsonProperty("userPassword")
    private String userEntityDTOPassword;

    @NotBlank(message = "{error.not_blank.email}")
    @Email(message = "{error.email_format}")
    @JsonProperty("userEmail")
    private String userEntityDTOEmail;
}
