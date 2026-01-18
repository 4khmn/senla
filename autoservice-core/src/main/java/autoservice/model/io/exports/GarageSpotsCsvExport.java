package autoservice.model.io.exports;

import autoservice.model.entities.GarageSpot;
import autoservice.model.service.GarageSpotService;
import config.annotation.Component;
import config.annotation.Inject;

@Component
public class GarageSpotsCsvExport extends CsvExport {

    private final GarageSpotService garageSpotService;
    @Inject
    public GarageSpotsCsvExport(GarageSpotService manager) {
        super("id,size,hasLift,hasPit", "garageSpots.csv");
        this.garageSpotService = manager;
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
        return garageSpotService.getGarageSpots();
    }
}
