package org.jdt16.user4a.dto.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "status", nullable = false)
    private Integer status;

    @PrePersist
    void prePersist() {
        if (status == null) status = 0;
    }
}
