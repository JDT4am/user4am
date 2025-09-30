package org.jdt16.user4a.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RestApiResponseError {
    @JsonProperty("errors")
    private Map<String, Serializable> restApiResponseRequestError;
}
