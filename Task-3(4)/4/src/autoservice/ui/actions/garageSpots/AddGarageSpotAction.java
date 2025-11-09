package autoservice.ui.actions.garageSpots;

import autoservice.model.AutoService;
import autoservice.ui.actions.IAction;

public class AddGarageSpotAction implements IAction {
    private final AutoService service;

    public AddGarageSpotAction(AutoService autoService) {
        this.service = autoService;
    }
    @Override
    public void execute() {
        service.addGarageSpot();
        System.out.println("Гаражное место добавлено!");
        System.out.println("Всего доступных гаражных мест: " + service.getGarageSpotsCount());
    }
}
