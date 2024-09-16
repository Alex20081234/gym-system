package com.epam.task.gymsystem.dao;

import java.util.Map;
import java.util.Optional;

public interface Selectable<T> {
    Optional<T> select(Long id);
    Optional<Map<Long, T>> selectAll();
}
