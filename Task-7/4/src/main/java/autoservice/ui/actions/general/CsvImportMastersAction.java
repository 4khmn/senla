package autoservice.ui.actions.general;

import autoservice.model.AutoService;
import autoservice.model.exceptions.CsvParsingException;
import autoservice.model.exceptions.ImportException;
import autoservice.ui.actions.IAction;

import java.io.IOException;

public class CsvImportMastersAction implements IAction {
    private final AutoService service;
    public CsvImportMastersAction(AutoService service) {
        this.service = service;
    }
    @Override
    public void execute() {
        try {
            if (!service.importMasters()){
                System.out.println("Файл пуст.");
            }
            else{
                System.out.println("Данные успешно импортированы!");
            }
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        } catch(IOException e){
            System.out.println("Файл не найден. Создайте файл по пути <Task-3(6)/4/data/...>");
        } catch(CsvParsingException e){
            System.out.println(e.getMessage());
        }
    }
}
