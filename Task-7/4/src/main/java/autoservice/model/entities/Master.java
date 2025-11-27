package main.java.autoservice.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class Master {
    private static long global_id=1; // for serial primary key
    private final long id;
    private String name;
    private BigDecimal salary;
    private TreeSet<TimeSlot> calendar;


    public Master(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
        this.id = global_id++;
        this.calendar = new TreeSet<>();
    }

    public Master(long id, String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
        this.id = id;
        this.calendar = new TreeSet<>();
    }
    public static void updateGlobalId(long maxId) {
        if (maxId >= global_id) {
            global_id = maxId + 1;
        }
    }
    @Override
    public Master clone() {
        Master copy = new Master(id, name, salary);
        copy.calendar = new TreeSet<>(calendar);
        return copy;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id - " + id + ", name - " + name + ", salary - " + salary;
    }

    public boolean addBusyTime(LocalDateTime start, LocalDateTime end) {
        TimeSlot newSlot = new TimeSlot(start, end);
        calendar.add(newSlot);
        return true;
    }

    public void freeAllSchedule(){
        this.calendar = new TreeSet<>();
    }
    public void freeTimeSlot(LocalDateTime start, LocalDateTime end){
        calendar.remove(new TimeSlot(start, end));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


//    public LocalDateTime findNextAvailableSlotInMasterSchedule(LocalDateTime from, int durationInHours) {
//        TreeSet<TimeSlot> calendar;
//
//        calendar = getCalendar();
//
//        if (calendar.isEmpty()) {
//            return from;
//        }
//        TimeSlot firstSlot = calendar.first();
//        if (from.isBefore(firstSlot.getStart().minusHours(durationInHours)) ||
//        from.isEqual(firstSlot.getStart().minusHours(durationInHours))) {
//            return from;
//        }
//
//        for (TimeSlot slot : calendar) {
//            if (slot.getEnd().isBefore(from) || slot.getEnd().isEqual(from)) {
//                continue;
//            }
//            TimeSlot next = calendar.higher(slot);
//            LocalDateTime candidateStart = slot.getEnd();
//            LocalDateTime candidateEnd = candidateStart.plusHours(durationInHours);
//
//            if (next == null || candidateEnd.isBefore(next.getStart())) {
//                if (candidateStart.isAfter(from)){
//                    return candidateStart;
//                } else{
//                    return from;
//                }
//            }
//        }
//        if(calendar.last().getEnd().isAfter(from)) {
//            return calendar.last().getEnd();
//        } else {
//            return from;
//        }
//    }

}
