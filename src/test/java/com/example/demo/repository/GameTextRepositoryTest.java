package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.repository.GameTextRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJdbcTest
public class GameTextRepositoryTest {

    @Autowired
    private GameTextRepository gameTextRepository;

    @Test
    public void testFindById() {
        // Test repository method
        assertNotNull(gameTextRepository.findById(1L));
    }
}
