package manager;

import model.Master;
import model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MasterManager {
    private List<Master> masters;

    public MasterManager(List<Master> masters) {
        this.masters = masters;
    }


    //4
    public List<Master> mastersSort(int decision, LocalDateTime checkTime, int durationInHours){
        List<Master> sortedMasters = masters;
        switch (decision){
            case 1:
                //по алфавиту
                sortedMasters = sortedMasters.stream()
                        .sorted(Comparator.comparing(Master::getName))
                        .toList();
                break;
            case 2:
                //по занятости
                sortedMasters = sortedMasters.stream()
                        .sorted((master1, master2) -> {
                            boolean isMaster1Free = master1.isAvailable(checkTime, checkTime.plusHours(durationInHours));
                            boolean isMaster2Free = master2.isAvailable(checkTime, checkTime.plusHours(durationInHours));

                            // если оба свободны, сравниваем их по имени
                            if (isMaster1Free && isMaster2Free) {
                                return master1.getName().compareTo(master2.getName());
                            }
                            if (isMaster1Free) {
                                return -1;
                            }
                            if (isMaster2Free) {
                                return 1;
                            }
                            return 0;
                        })
                        .toList();
                break;
            default:
                //error
                return null;
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
}
