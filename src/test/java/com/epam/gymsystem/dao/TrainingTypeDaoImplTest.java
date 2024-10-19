package com.epam.gymsystem.dao;

import com.epam.gymsystem.configuration.GymSystemConfiguration;
import com.epam.gymsystem.domain.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GymSystemConfiguration.class})
@WebAppConfiguration
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
