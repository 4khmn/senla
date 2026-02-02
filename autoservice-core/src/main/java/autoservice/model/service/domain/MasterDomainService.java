package autoservice.model.service.domain;

import autoservice.model.entities.Master;
import autoservice.model.entities.TimeSlot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
public class MasterDomainService {

    public static List<Master> getMastersWithCalendar(List<Master> masters, List<Object[]> slots) {
        Map<Long, TreeSet<TimeSlot>> calendarMap = new HashMap<>();
        for (Object[] row : slots) {
            Long masterId = (Long) row[0];
            LocalDateTime start = (LocalDateTime) row[1];
            LocalDateTime end = (LocalDateTime) row[2];

            calendarMap.computeIfAbsent(masterId, k -> new TreeSet<>())
                    .add(new TimeSlot(start, end));
        }
        for (Master m : masters) {
            m.setCalendar(calendarMap.getOrDefault(m.getId(), new TreeSet<>()));
        }
        return masters;
    }
}
