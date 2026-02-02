package autoservice.ui.actions.garageSpots;

import autoservice.model.entities.GarageSpot;
import autoservice.model.exceptions.DBException;
import autoservice.model.service.GarageSpotService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class GetFreeSpotsAction implements IAction {


    private final GarageSpotService garageSpotService;


    @Override
    public void execute() {
        if (garageSpotService.getGarageSpotsCount() == 0) {
            System.out.println("В авто-сервисе нету гаражных мест.");
            return;
        }
        try {
            List<GarageSpot> freeSpots = garageSpotService.getFreeSpots();

            System.out.println("Список свободных мест:");
            for (var spot : freeSpots) {
                System.out.println(spot);
            }
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
