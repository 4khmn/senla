package autoservice.model.io.exports;

import autoservice.model.AutoService;
import autoservice.model.entities.GarageSpot;
import autoservice.model.manager.GarageSpotManager;

public class GarageSpotsCsvExport extends CsvExport {

    public GarageSpotsCsvExport() {
        super("id,size,hasLift,hasPit", "garageSpots.csv");
    }

    @Override
    protected String formatEntity(Object entity) {
        GarageSpot garageSpot = (GarageSpot)entity;
        return String.join(",",
                String.valueOf(garageSpot.getId()),
                String.valueOf(garageSpot.getSize()),
                String.valueOf(garageSpot.isHasLift()),
                String.valueOf(garageSpot.isHasPit())
        );
    }

    @Override
    protected Iterable<?> getEntities() {
        AutoService autoService = AutoService.getInstance();
        return autoService.getGarageManager().getGarageSpots();
    }
}
