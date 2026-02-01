package autoservice.ui.actions.general;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.io.exports.GarageSpotsCsvExportService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
@RequiredArgsConstructor
public class CsvExportGarageSpotsAction implements IAction {
    private final GarageSpotsCsvExportService garageSpotsCsvExportService;

    @Override
    public void execute() {
        try {
            garageSpotsCsvExportService.export();
            System.out.println("Данные успешно экспортированы! Они лежат по пути  <data/garageSpots.csv>");
        } catch (IOException e) {
            System.out.println("Файл не найден.");
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
