package autoservice.model.manager;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.MastersSortEnum;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.abs;

public class MasterManager {
    private List<Master> masters;

    public MasterManager(List<Master> masters) {
        this.masters = masters;
    }


    //4
    public List<Master> mastersSort(MastersSortEnum decision){
        List<Master> sortedMasters = masters;
        switch (decision){
            case BY_NAME:
                //по алфавиту
                sortedMasters = sortedMasters.stream()
                        .sorted(Comparator.comparing(Master::getName))
                        .toList();
                break;
            case BY_EMPLOYMENT:
                //по занятости
                sortedMasters = sortedMasters.stream()
                        .sorted((master1, master2) -> {
                            int freeMaster1HoursToday = getFreeMasterHoursToday(master1);
                            int freeMaster2HoursToday = getFreeMasterHoursToday(master2);


                            // если оба свободны, сравниваем их по имени
                            if (freeMaster1HoursToday == freeMaster2HoursToday) {
                                return master1.getName().compareTo(master2.getName());
                            }
                            //сначала более свободные
                            if (freeMaster1HoursToday > freeMaster2HoursToday) {
                                return -1;
                            }
                            if (freeMaster1HoursToday < freeMaster2HoursToday) {
                                return 1;
                            }
                            return 0;
                        })
                        .toList();
                break;
            default:
                //error
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        return sortedMasters;
    }
    //4
    public Master getMasterByOrder(Order order){
        return order.getMaster();
    }

    public List<Master> getMasters() {
        return masters;
    }

    public long addMaster(String name, BigDecimal salary){
        Master master = new Master(name, salary);
        masters.add(master);
        return master.getId();
    }

    public boolean deleteMaster(long id){
        for(var v: masters){
            if (v.getId()==id){
                masters.remove(v);
                return true;
            }
        }
        return false;
    }

    public Master getMasterById(long id){
        for (var v: masters){
            if (v.getId()==id){
                return v;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return "MasterManager{" +
                "masters=" + masters +
                '}';
    }

    private int getFreeMasterHoursToday(Master master){
        int freeMasterHoursToday = 24;
        int today = LocalDateTime.now().getDayOfMonth();
        for (var v: master.getCalendar()){
            if (v.getStart().getDayOfMonth()==today && v.getEnd().getDayOfMonth() == today){
                long duration = abs(Duration.between(v.getStart(), v.getEnd()).toHours());
                freeMasterHoursToday -= duration;
            }
            else if(v.getStart().getDayOfMonth()==today){
                long duration = abs(Duration.between(v.getStart(),
                        LocalDateTime.of( v.getStart().getYear(),
                                v.getStart().getMonth(),
                                v.getStart().getDayOfMonth()+1,
                                0,
                                0)).toHours());
                freeMasterHoursToday-=duration;
            }
            else if (v.getEnd().getDayOfMonth() == today){
                long duration = abs(Duration.between(LocalDateTime.of( v.getEnd().getYear(),
                                v.getEnd().getMonth(),
                                v.getEnd().getDayOfMonth(),
                                0,
                                0), v.getEnd()).toHours());
                freeMasterHoursToday-=duration;
            }
        }
        return freeMasterHoursToday;
    }
}
