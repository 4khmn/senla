package autoservice.ui.actions.general;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.io.exports.MastersCsvExportService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
@RequiredArgsConstructor
public class CsvExportMasterAction implements IAction {
    private final MastersCsvExportService mastersCsvExportService;

    @Override
    public void execute() {
        try {
            mastersCsvExportService.export();
            System.out.println("Данные успешно экспортированы! Они лежат по пути  <data/masters.csv>");
        } catch (IOException e) {
            System.out.println("Файл не найден.");
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
