package com.prac.atlas.api.adapter;


import com.prac.atlas.api.testdto.request.MockDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MockDatabase {
    private static final List<MockDTO> DATA = new ArrayList<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    public static synchronized int insert(MockDTO dto) {
        dto.setId(COUNTER.getAndIncrement());
        DATA.add(dto);
        return dto.getId();
    }

    public static synchronized List<MockDTO> fetchAll() {
        return new ArrayList<>(DATA);
    }

    public static synchronized void clear() {
        DATA.clear();
        COUNTER.set(1);
    }
    public static synchronized boolean update(MockDTO updated) {
        for (int i = 0; i < DATA.size(); i++) {
            if (DATA.get(i).getId().equals(updated.getId())) {
                DATA.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean delete(Integer id) {
        return DATA.removeIf(dto -> dto.getId().equals(id));
    }
}
