package manager;

import model.Master;
import model.Order;
import result.MasterResult;

import java.lang.classfile.Opcode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MasterManager {
    private List<Master> masters;

    public MasterManager(List<Master> masters) {
        this.masters = masters;
    }


    //4
    public List<Master> mastersSort(int decision){
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
                        .sorted(Comparator.comparing(Master::isAvailable))
                        .toList();
                break;
            default:
                //error
        }
        return sortedMasters;
    }

    public List<Master> getMasters() {
        return masters;
    }

    //4
    public Master getMasterByOrder(Order order){
        return order.getMaster();
    }
    public MasterResult addMaster(Master master){
        if (!masters.contains(master)){
            masters.add(master);
            return MasterResult.SUCCESS_ADDED;
            //System.out.println("Master #" + master.getId() + " was successfully added");
        }
        else{
            return MasterResult.ALREADY_EXISTS;
            //System.out.println("This master is already exist in System {master #" + master.getId() + "}");
        }
    }

    public MasterResult deleteMaster(Master master){
        if (masters.contains(master)){
            masters.remove(master);
            return MasterResult.SUCCESS_REMOVED;
            //System.out.println("Master #" + master.getId() + " was successfully removed");
        }
        else{
            return MasterResult.NOT_FOUND;
            //System.out.println("There is no such master in System {master #" + master.getId() + "}");
        }
    }
    @Override
    public String toString() {
        return "MasterManager{" +
                "masters=" + masters +
                '}';
    }
}
