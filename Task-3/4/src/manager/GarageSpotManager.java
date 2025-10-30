package manager;

import model.GarageSpot;
import result.GarageSpotResult;

import java.util.List;

public class GarageSpotManager {
    private List<GarageSpot> garageSpots;

    public GarageSpotManager(List<GarageSpot> garageSpots) {
        this.garageSpots = garageSpots;
    }

    public GarageSpotResult addGarageSpot(GarageSpot garageSpot){
        if (!garageSpots.contains(garageSpot)){
            garageSpots.add(garageSpot);
            return GarageSpotResult.SUCCESS_ADDED;
            //System.out.println("garageSpot #" + garageSpot.getId() + " was successfully added");
        }
        else{
            return GarageSpotResult.ALREADY_EXISTS;
            //System.out.println("This garage spot is already exist in System {garageSpot #" + garageSpot.getId() + "}");
        }
    }

    public GarageSpotResult deleteGarageSpot(GarageSpot garageSpot){
        if (garageSpots.contains(garageSpot)){
            garageSpots.remove(garageSpot);
            return GarageSpotResult.SUCCESS_REMOVED;
            //System.out.println("garageSpot #" + garageSpot.getId() + " was successfully removed");
        }
        else{
            return GarageSpotResult.NOT_FOUND;
            //System.out.println("There is no such garage spot in System {garageSpot #" + garageSpot.getId() + "}");
        }
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

    public void setGarageSpots(List<GarageSpot> garageSpots) {
        this.garageSpots = garageSpots;
    }
}
