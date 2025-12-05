package autoservice.ui.actions.general;

import autoservice.model.AutoService;
import autoservice.ui.actions.IAction;

import java.io.IOException;

public class CsvExportOrdersAction implements IAction {
    private final AutoService service;

    public CsvExportOrdersAction(AutoService service) {
        this.service = service;
    }
    @Override
    public void execute() {
        try {
            service.exportOrders();
            System.out.println("Данные успешно экспортированы! Они лежат по пути  <data/orders.csv>");
        } catch (IOException e) {
            System.out.println("Файл не найден.");
        }
    }
}
