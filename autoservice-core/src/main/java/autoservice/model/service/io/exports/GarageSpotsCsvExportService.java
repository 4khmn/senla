package autoservice.model.service.io.exports;

import autoservice.model.entities.GarageSpot;
import autoservice.model.service.GarageSpotService;
import org.springframework.stereotype.Service;

@Service
public class GarageSpotsCsvExportService extends CsvExport {

    private final GarageSpotService garageSpotService;
    public GarageSpotsCsvExportService(GarageSpotService manager) {
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
