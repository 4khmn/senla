package main.java.autoservice.ui.actions.general;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.ui.actions.IAction;

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
            System.out.println("Данные успешно экспортированы! Они лежат по пути  <Task-3(6)/4/data/orders.csv>");
        } catch (IOException e) {
            System.out.println("Файл не найден. Создайте файл по пути <Task-3(6)/4/data/...>");
        }
    }
}
