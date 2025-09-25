package org.jdt16.user4a.dto.entity;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value @Builder
public class UserResponse {
    UUID id;
    String name;
    String email;
    Integer gender;
    Integer age;
    Integer status;
}
