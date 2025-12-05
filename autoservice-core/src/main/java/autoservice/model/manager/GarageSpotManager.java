package autoservice.model.manager;

import autoservice.model.entities.GarageSpot;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class GarageSpotManager {
    private List<GarageSpot> garageSpots;


    ////////////
    public GarageSpotManager(List<GarageSpot> garageSpots) {
        this.garageSpots = garageSpots;
    }
    public GarageSpotManager() {}
    //////////////


    public long addGarageSpot(double size, boolean hasLift, boolean hasPit){
        if (size<8){
            return -1;
        }
        GarageSpot garageSpot = new GarageSpot(size, hasLift, hasPit);
        garageSpots.add(garageSpot);
        return garageSpot.getId();
    }
    public long addGarageSpot(long id, double size, boolean hasLift, boolean hasPit){
        GarageSpot garageSpot = new GarageSpot(id, size, hasLift, hasPit);
        garageSpots.add(garageSpot);
        return garageSpot.getId();
    }
    //метод для транзакции в импорте csv
    public GarageSpotManager cloneManager() throws CloneNotSupportedException {
        List<GarageSpot> copy = new ArrayList<>();
        for (GarageSpot spot : garageSpots) {
            copy.add((GarageSpot)spot.clone());
        }
        return new GarageSpotManager(copy);
    }
    public void replaceData(GarageSpotManager other) {
        this.garageSpots = other.garageSpots;
    }

    public boolean deleteGarageSpot(long id){
        for (var v: garageSpots){
            if (v.getId()==id){
                garageSpots.remove(v);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "GarageSpotManager{" +
                "garageSpots=" + garageSpots +
                '}';
    }

    public List<GarageSpot> getGarageSpots() {
        return garageSpots;
    }
    //4
    public GarageSpot getGarageSpotById(long id){
        for (var v: garageSpots){
            if (v.getId()==id){
                return v;
            }
        }
        return null;
    }

    //4 список свободных мест в сервисных гаражах
    @JsonIgnore
    public List<GarageSpot> getFreeSpots(){
        List<GarageSpot> freeGarageSpots = new ArrayList<>();
        for (var v: garageSpots){
            if (v.isAvailable(LocalDateTime.now(), LocalDateTime.now().plusMinutes(1))){
                freeGarageSpots.add(v);
            }
        }
        return freeGarageSpots;
    }
}
