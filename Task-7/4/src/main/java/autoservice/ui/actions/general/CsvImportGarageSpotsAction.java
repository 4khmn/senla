package main.java.autoservice.ui.actions.general;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.model.exceptions.CsvParsingException;
import main.java.autoservice.model.exceptions.IllegalGarageSpotSize;
import main.java.autoservice.model.exceptions.ImportException;
import main.java.autoservice.ui.actions.IAction;

import java.io.IOException;

public class CsvImportGarageSpotsAction implements IAction {
    private final AutoService service;
    public CsvImportGarageSpotsAction(AutoService service) {
        this.service = service;
    }
    @Override
    public void execute() {
        try {
            if (!service.importGarageSpots()){
                System.out.println("Файл пуст.");
            }
            else{
                System.out.println("Данные успешно импортированы!");
            }
        } catch(IllegalGarageSpotSize e){
            System.out.println(e.getMessage());
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        } catch(IOException e){
            System.out.println("Файл не найден. Создайте файл по пути <Task-3(6)/4/data/...>");
        } catch(CsvParsingException e){
            System.out.println(e.getMessage());
        }
    }
}
