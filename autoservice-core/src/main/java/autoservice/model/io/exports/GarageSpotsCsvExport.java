package autoservice.model.io.exports;

import autoservice.model.AutoService;
import autoservice.model.entities.GarageSpot;
import autoservice.model.manager.GarageSpotManager;
import config.annotation.Component;
import config.annotation.Inject;

@Component
public class GarageSpotsCsvExport extends CsvExport {

    private final GarageSpotManager garageSpotManager;
    @Inject
    public GarageSpotsCsvExport(GarageSpotManager manager) {
        super("id,size,hasLift,hasPit", "garageSpots.csv");
        this.garageSpotManager = manager;
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
        return garageSpotManager.getGarageSpots();
    }
}
