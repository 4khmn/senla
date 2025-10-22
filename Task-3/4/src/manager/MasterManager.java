package manager;

import model.Master;
import result.MasterResult;

import java.util.List;

public class MasterManager {
    private List<Master> masters;

    public MasterManager(List<Master> masters) {
        this.masters = masters;
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
