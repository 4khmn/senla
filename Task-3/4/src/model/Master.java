package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Master {
    private static int global_id=1; // for serial primary key
    private final int id;
    private String name;
    private BigDecimal salary;
    private TreeSet<TimeSlot> calendar;

    public Master(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
        id = global_id++;
        this.calendar = new TreeSet<>();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Master{" +
                "WorkId=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
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

    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        for (TimeSlot slot : calendar) {
            if (slot.overlaps(start, end)) {
                return false;
            }
        }
        return true;
    }
    private LocalDateTime findNextAvailableSlot(Object obj, LocalDateTime from, int durationInHours) {
        SortedSet<TimeSlot> calendar;

        if (obj instanceof Master) {
            calendar = ((Master) obj).getCalendar();
        } else if (obj instanceof GarageSpot) {
            calendar = ((GarageSpot) obj).getCalendar();
        } else {
            throw new IllegalArgumentException("Unsupported type");
        }

        // Если нет записей — можно сразу с "from"
        if (calendar.isEmpty()) {
            return from;
        }

        for (TimeSlot slot : calendar) {
            // Если слот уже в прошлом — пропускаем
            if (slot.getEnd().isBefore(from)) continue;

            // Ищем "окно" между текущим и следующим интервалом
            TimeSlot next = calendar.higher(slot); // null если последний
            LocalDateTime candidateStart = slot.getEnd();
            LocalDateTime candidateEnd = candidateStart.plusHours(durationInHours);

            // Если между слотами есть место
            if (next == null || candidateEnd.isBefore(next.getStart())) {
                return candidateStart.isAfter(from) ? candidateStart : from;
            }
        }

        // Если до конца расписания не нашли — можно сразу после последнего
        return calendar.last().getEnd().isAfter(from) ? calendar.last().getEnd() : from;
    }


    public SortedSet<TimeSlot> getCalendar() {
        return calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private LocalDateTime findNextAvailableSlot(Master master, LocalDateTime from, int durationInHours) {
        NavigableSet<TimeSlot> calendar;

        calendar = master.getCalendar();

        if (calendar.isEmpty()) {
            return from;
        }

        for (TimeSlot slot : calendar) {
            if (slot.getEnd().isBefore(from)) {
                continue;
            }

            TimeSlot next = calendar.higher(slot);
            LocalDateTime candidateStart = slot.getEnd();
            LocalDateTime candidateEnd = candidateStart.plusHours(durationInHours);

            // Если между слотами есть место
            if (next == null || candidateEnd.isBefore(next.getStart())) {
                return candidateStart.isAfter(from) ? candidateStart : from;
            }
        }

        // Если до конца расписания не нашли — можно сразу после последнего
        return calendar.last().getEnd().isAfter(from) ? calendar.last().getEnd() : from;
    }

}
