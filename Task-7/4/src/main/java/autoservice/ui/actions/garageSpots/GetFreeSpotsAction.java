package main.java.autoservice.ui.actions.garageSpots;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.model.entities.GarageSpot;
import main.java.autoservice.ui.actions.IAction;

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
        List<GarageSpot> freeSpots = service.getFreeSpots();

        System.out.println("Список свободных мест:");
        for (var v : freeSpots) {
            System.out.println(v);
        }
    }
}
