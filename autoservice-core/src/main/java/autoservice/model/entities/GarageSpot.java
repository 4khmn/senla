package autoservice.model.entities;



import java.time.LocalDateTime;
import java.util.TreeSet;
public class GarageSpot {
    private static long global_id=1; // for serial primary key
    private long id;

    private double size;
    private boolean hasLift;
    private boolean hasPit;
    private transient TreeSet<TimeSlot> calendar;


    public long getId() {
        return id;
    }

    public void setCalendar(TreeSet<TimeSlot> calendar) {
        this.calendar = calendar;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GarageSpot() {
    }

    public GarageSpot(double size, boolean hasLift, boolean hasPit) {
        this.id = global_id++;

        this.size = size;
        this.hasLift = hasLift;
        this.hasPit = hasPit;
        this.calendar = new TreeSet<>();
    }
    public GarageSpot(long id, double size, boolean hasLift, boolean hasPit) {
        this.id = id;

        this.size = size;
        this.hasLift = hasLift;
        this.hasPit = hasPit;
        this.calendar = new TreeSet<>();
    }


    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public boolean isHasLift() {
        return hasLift;
    }

    public void setHasLift(boolean hasLift) {
        this.hasLift = hasLift;
    }

    public boolean isHasPit() {
        return hasPit;
    }

    public void setHasPit(boolean hasPit) {
        this.hasPit = hasPit;
    }

    public boolean addBusyTime(LocalDateTime start, LocalDateTime end) {
        TimeSlot newSlot = new TimeSlot(start, end);
        calendar.add(newSlot);
        return true;
    }

    @Override
    public String toString() {
        return "id - " + id + ", size - " + size + ", hasList - " + hasLift + ", hasPit - " + hasPit;
    }

    public void freeAllSchedule(){
        this.calendar = new TreeSet<>();
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

    public static void updateGlobalId(long maxId) {
        if (maxId >= global_id) {
            global_id = maxId + 1;
        }
    }
    public TreeSet<TimeSlot> getCalendar() {
        return calendar;
    }

    public boolean scheduleIsEmpty(){
        return calendar.isEmpty();
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

    @Override
    public GarageSpot clone() {
        GarageSpot copy = new GarageSpot(id, size, hasLift, hasPit);
        copy.calendar = new TreeSet<>(calendar);
        return copy;
    }
}
