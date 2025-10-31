package model;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

public class GarageSpot {
    private static int global_id=1; // for serial primary key
    private final int id;
    private SortedSet<TimeSlot> calendar;


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

    public SortedSet<TimeSlot> getCalendar() {
        return calendar;
    }
}
