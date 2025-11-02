package manager;

import model.GarageSpot;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GarageSpotManager {
    private List<GarageSpot> garageSpots;


    public GarageSpotManager(List<GarageSpot> garageSpots) {
        this.garageSpots = garageSpots;
    }


    public long addGarageSpot(){
        GarageSpot garageSpot = new GarageSpot();
        garageSpots.add(garageSpot);
        return garageSpot.getId();
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
