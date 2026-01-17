package autoservice.ui.actions.general;

import autoservice.model.AutoService;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.io.IOException;

public class CsvExportMasterAction implements IAction {
    private final AutoService service;

    public CsvExportMasterAction(AutoService service) {
        this.service = service;
    }
    @Override
    public void execute() {
        try {
            service.exportMasters();
            System.out.println("Данные успешно экспортированы! Они лежат по пути  <data/masters.csv>");
        } catch (IOException e) {
            System.out.println("Файл не найден.");
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }
    }
}
