package net.cozyvanilla.cozylib.modules.mysql.interfaces;

import net.cozyvanilla.cozylib.Enums;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PlayerKeyValueService {
    void initialize();
    Map<String, Integer> getData(UUID uuid);
    void updateData(UUID uuid, Map<String, Integer> data);
    void setObtainedAt(UUID uuid, String dataName);
    Map<String, Instant> getObtainedAt(UUID uuid);
    List<UUID> getByObtainedAt(String dataName, int count, Enums.Ordering order);
}