package autoservice.ui.actions.general;

import autoservice.model.exceptions.CsvParsingException;
import autoservice.model.exceptions.DBException;
import autoservice.model.exceptions.IllegalGarageSpotSize;
import autoservice.model.exceptions.ImportException;
import autoservice.model.service.io.imports.CsvImportService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CsvImportGarageSpotsAction implements IAction {
    private final CsvImportService csvImportService;

    @Override
    public void execute() {
        try {
            if (!csvImportService.importGarageSpots()) {
                System.out.println("Файл пуст.");
            } else {
                System.out.println("Данные успешно импортированы!");
            }
        } catch (IllegalGarageSpotSize e) {
            System.out.println(e.getMessage());
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        } catch (CsvParsingException e) {
            System.out.println(e.getMessage());
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
