package autoservice.ui.actions.general;

import autoservice.model.AutoService;
import autoservice.model.exceptions.CsvParsingException;
import autoservice.model.exceptions.DBException;
import autoservice.model.exceptions.IllegalGarageSpotSize;
import autoservice.model.exceptions.ImportException;
import autoservice.ui.actions.IAction;

import java.io.IOException;

public class CsvImportGarageSpotsAction implements IAction {
    private final AutoService service;
    public CsvImportGarageSpotsAction(AutoService service) {
        this.service = service;
    }
    @Override
    public void execute() {
        try {
            if (!service.importGarageSpots()) {
                System.out.println("Файл пуст.");
            } else {
                System.out.println("Данные успешно импортированы!");
            }
        } catch (IllegalGarageSpotSize e) {
            System.out.println(e.getMessage());
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Файл не найден. Создайте файл по пути <resources/data/garageSpots.csv>");
        } catch (CsvParsingException e) {
            System.out.println(e.getMessage());
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
