package autoservice.ui.actions.general;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.io.exports.OrdersCsvExportService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
@RequiredArgsConstructor
public class CsvExportOrdersAction implements IAction {
    private final OrdersCsvExportService ordersCsvExportService;

    @Override
    public void execute() {
        try {
            ordersCsvExportService.export();
            System.out.println("Данные успешно экспортированы! Они лежат по пути  <data/orders.csv>");
        } catch (IOException e) {
            System.out.println("Файл не найден.");
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
