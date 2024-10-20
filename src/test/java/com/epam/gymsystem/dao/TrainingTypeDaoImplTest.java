package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TrainingTypeDaoImplTest {
    @Autowired
    private TrainingTypeDaoImpl dao;

    @Test
    void selectTypeShouldReturnTrainingType() {
        TrainingType type = dao.selectType("Yoga").orElse(null);
        assertNotNull(type);
        assertEquals("Yoga", type.getName());
        assertEquals(1, type.getId());
    }

    @Test
    void selectAllTypesShouldReturnAllTrainingTypes() {
        List<TrainingType> types = dao.selectAllTypes();
        assertNotNull(types);
        assertFalse(types.isEmpty());
        types.forEach(type -> assertNotNull(type.getName()));
        assertEquals(10, types.size());
    }

    @Test
    void selectTypesShouldReturnEmptyWhenTypeNonExistent() {
        assertEquals(Optional.empty(), dao.selectType("Non Existent"));
    }
}
