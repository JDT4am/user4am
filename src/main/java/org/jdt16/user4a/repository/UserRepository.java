package org.jdt16.user4a.repository;

import org.jdt16.user4a.dto.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
