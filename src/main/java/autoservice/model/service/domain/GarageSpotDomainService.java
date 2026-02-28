package autoservice.model.service.domain;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.TimeSlot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
public class GarageSpotDomainService {

    public static List<GarageSpot> getGarageSpotsWithCalendar(List<GarageSpot> spots, List<Object[]> slots) {
        Map<Long, TreeSet<TimeSlot>> calendarMap = new HashMap<>();
        for (Object[] row : slots) {
            Long garageSpotId = (Long) row[0];
            LocalDateTime start = (LocalDateTime) row[1];
            LocalDateTime end = (LocalDateTime) row[2];

            calendarMap.computeIfAbsent(garageSpotId, k -> new TreeSet<>())
                    .add(new TimeSlot(start, end));
        }

        for (GarageSpot gs : spots) {
            gs.setCalendar(calendarMap.getOrDefault(gs.getId(), new TreeSet<>()));
        }
        return spots;
    }
}
