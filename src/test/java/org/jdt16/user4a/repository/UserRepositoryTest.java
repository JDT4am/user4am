package org.jdt16.user4a.repository;
import static org.assertj.core.api.Assertions.assertThat;

import org.jdt16.user4a.dto.entity.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByUserEntityDTOName_shouldReturnTrueWhenNameExists() {
        UserDTO user = new UserDTO();
        user.setUserEntityDTOName("Alice");
        user.setUserEntityDTOAge(25);
        user.setUserEntityDTOEmail("alice@example.com");
        user.setUserEntityDTOGender(1);
        user.setUserEntityDTOStatus(0);

        userRepository.save(user);

        boolean exists = userRepository.existsByUserEntityDTOName("Alice");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUserEntityDTOName_shouldReturnFalseWhenNameDoesNotExist() {
        boolean exists = userRepository.existsByUserEntityDTOName("Bob");
        assertThat(exists).isFalse();
    }

    @Test
    void saveAndFindById_shouldPersistAndRetrieveUser() {
        UUID id = UUID.randomUUID();

        UserDTO user = new UserDTO();
        user.setUserEntityDTOName("Charlie");
        user.setUserEntityDTOAge(30);
        user.setUserEntityDTOEmail("charlie@example.com");
        user.setUserEntityDTOGender(1);
        user.setUserEntityDTOStatus(1);

        UserDTO savedUser = userRepository.save(user);

        Optional<UserDTO> found = userRepository.findById(savedUser.getUserEntityDTOId());

        assertThat(found).isPresent();
        assertThat(found.get().getUserEntityDTOName()).isEqualTo("Charlie");
    }

    @Test
    void findAll_shouldReturnAllSavedUsers() {
        UserDTO user1 = new UserDTO();
        user1.setUserEntityDTOName("Dewi");
        user1.setUserEntityDTOAge(22);
        user1.setUserEntityDTOEmail("dewi@example.com");
        user1.setUserEntityDTOGender(2);
        user1.setUserEntityDTOStatus(0);

        UserDTO user2 = new UserDTO();
        user2.setUserEntityDTOName("Eko");
        user2.setUserEntityDTOAge(28);
        user2.setUserEntityDTOEmail("eko@example.com");
        user2.setUserEntityDTOGender(1);
        user2.setUserEntityDTOStatus(1);

        userRepository.saveAll(List.of(user1, user2));

        List<UserDTO> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(2)
                .extracting(UserDTO::getUserEntityDTOName)
                .containsExactlyInAnyOrder("Dewi", "Eko");
    }
}
