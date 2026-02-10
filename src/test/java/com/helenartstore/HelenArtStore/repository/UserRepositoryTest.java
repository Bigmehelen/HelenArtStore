package com.helenartstore.HelenArtStore.repository;

import com.helenartstore.HelenArtStore.data.models.User;
import com.helenartstore.HelenArtStore.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest {

    User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("helen");
        user.setEmail("helen@gamil.com");
        user.setPassword("password123");
        userRepository.deleteAll();
    }

    @Test
    public void testThatUserCanBeAdded() {
        User saved = userRepository.save(user);
        assertNotNull(saved);
    }

    @Test
    public void userCanBeFindById() {
        User saved = userRepository.save(user);
        assertNotNull(saved);
        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
    }
}
