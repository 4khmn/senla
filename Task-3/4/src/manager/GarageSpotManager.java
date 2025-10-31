package manager;

import model.GarageSpot;
import model.TimeSlot;

import java.time.LocalDateTime;
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
    public boolean addTimeSlot(LocalDateTime start, LocalDateTime end){
        return true;
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
}
