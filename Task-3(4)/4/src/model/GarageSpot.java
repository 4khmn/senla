package model;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

public class GarageSpot {
    private static int global_id=1; // for serial primary key
    private final int id;
    private TreeSet<TimeSlot> calendar;


    public int getId() {
        return id;
    }

    public GarageSpot() {
        this.id = global_id++;
        this.calendar = new TreeSet<>();
    }

    public boolean addBusyTime(LocalDateTime start, LocalDateTime end) {
        TimeSlot newSlot = new TimeSlot(start, end);
        for (TimeSlot slot : calendar) {
            if (slot.overlaps(start, end)) {
                return false;
            }
        }

        calendar.add(newSlot);
        return true;
    }
    public void freeTimeSlot(LocalDateTime start, LocalDateTime end){
        calendar.remove(new TimeSlot(start, end));
    }

    public LocalDateTime getNextAvailableTime(int duration){
        LocalDateTime currentTime = LocalDateTime.now();
        for (var v: calendar){
            if (v.getStart().isAfter(currentTime)){
                if (currentTime.plusHours(duration).isBefore(v.getStart())){
                    return currentTime;
                }
            }
            currentTime = v.getEnd();
        }
        return currentTime;
    }

    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        for (TimeSlot slot : calendar) {
            if (slot.overlaps(start, end)) {
                return false;
            }
        }
        return true;
    }

    public TreeSet<TimeSlot> getCalendar() {
        return calendar;
    }


    public LocalDateTime findNextAvailableSlotInGarageSpotSchedule(LocalDateTime from, int durationInHours) {
        TreeSet<TimeSlot> calendar;

        calendar = getCalendar();

        if (calendar.isEmpty()) {
            return from;
        }
        TimeSlot firstSlot = calendar.first();
        if (from.isBefore(firstSlot.getStart().minusHours(durationInHours)) ||
                from.isEqual(firstSlot.getStart().minusHours(durationInHours))) {
            return from;
        }

        for (TimeSlot slot : calendar) {
            if (slot.getEnd().isBefore(from) || slot.getEnd().isEqual(from)) {
                continue;
            }
            TimeSlot next = calendar.higher(slot);
            LocalDateTime candidateStart = slot.getEnd();
            LocalDateTime candidateEnd = candidateStart.plusHours(durationInHours);

            if (next == null || candidateEnd.isBefore(next.getStart())) {
                if (candidateStart.isAfter(from)){
                    return candidateStart;
                } else{
                    return from;
                }
            }
        }
        if(calendar.last().getEnd().isAfter(from)) {
            return calendar.last().getEnd();
        } else {
            return from;
        }
    }
}
