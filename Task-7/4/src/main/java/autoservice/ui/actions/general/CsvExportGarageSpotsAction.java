package autoservice.ui.actions.general;

import autoservice.model.AutoService;
import autoservice.model.io.CsvExportService;
import autoservice.ui.actions.IAction;

import java.io.IOException;

public class CsvExportGarageSpotsAction implements IAction {
    private final AutoService service;

    public CsvExportGarageSpotsAction(AutoService service) {
        this.service = service;
    }
    @Override
    public void execute() {
        try {
            service.exportGarageSpots();
            System.out.println("Данные успешно экспортированы! Они лежат по пути  <Task-3(6)/4/data/garageSpots.csv>");
        } catch (IOException e) {
            System.out.println("Файл не найден. Создайте файл по пути <Task-3(6)/4/data/...>");
        }
    }
}
