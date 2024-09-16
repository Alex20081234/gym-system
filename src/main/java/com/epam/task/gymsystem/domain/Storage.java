package com.epam.task.gymsystem.domain;

import java.util.HashMap;
import java.util.Map;

public class Storage<T> {
    private final Map<Long, T> map = new HashMap<>();

    public Map<Long, T> getMap() {
        return map;
    }
}
