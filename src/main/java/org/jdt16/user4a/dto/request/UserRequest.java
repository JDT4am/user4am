package org.jdt16.user4a.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRequest {

    @JsonProperty("userId")
    private UUID userEntityDTOId;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(min = 2, max = 25, message = "Panjang minimal 2 dan maksimal 25")
    @JsonProperty("userName")
    private String userEntityDTOName;

    @NotBlank(message = "Umur tidak boleh kosong")
    @JsonProperty("userAge")
    private String userEntityDTOAge;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Email tidak valid")
    @JsonProperty("userEmail")
    private String userEntityDTOEmail;

    @NotNull(message = "Gender tidak boleh kosong")
    @JsonProperty("userGender")
    private Integer userEntityDTOGender;

    @JsonProperty("userStatus")
    private Integer userEntityDTOStatus = 0;

}
