package autoservice.ui.actions.masters;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.MasterService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class DeleteMasterAction implements IAction {

    private final MasterService masterService;


    @Override
    public void execute() {
        if (masterService.getMastersCount() == 0) {
            System.out.println("В авто-сервисе нету мастеров.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id мастера, которого вы хотите удалить: ");
        long id = sc.nextLong();
        try {
            masterService.deleteMaster(id);
            System.out.println("Мастер успешно удален!");
        } catch (DBException e) {
            System.out.println("Мастера с данным ID не найдено!");
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
