package autoservice.ui.actions.garageSpots;

import autoservice.model.AutoService;
import autoservice.model.entities.GarageSpot;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.util.List;

public class GetFreeSpotsAction implements IAction {

    private final AutoService service;

    public GetFreeSpotsAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getGarageSpotsCount() == 0) {
            System.out.println("В авто-сервисе нету гаражных мест.");
            return;
        }
        try {
            List<GarageSpot> freeSpots = service.getFreeSpots();

            System.out.println("Список свободных мест:");
            for (var v : freeSpots) {
                System.out.println(v);
            }
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
