package autoservice.ui.actions.masters;

import autoservice.model.entities.Master;
import autoservice.model.enums.MastersSortEnum;
import autoservice.model.exceptions.DBException;
import autoservice.model.service.MasterService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Scanner;
@RequiredArgsConstructor
public class MastersSortAction implements IAction {
    private final MasterService masterService;


    @Override
    public void execute() {
        if (masterService.getMastersCount() == 0) {
            System.out.println("В авто-сервисе нету мастеров.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите критерий сортировки:\n" +
                "1 - по алфавиту\n" +
                "2 - по занятости");
        System.out.print("Ваш выбор: ");
        MastersSortEnum sortType = null;
        while (sortType == null) {
            int decision = sc.nextInt();
            sortType = switch (decision) {
                case 1 -> MastersSortEnum.BY_NAME;
                case 2 -> MastersSortEnum.BY_EMPLOYMENT;
                default -> {
                    System.out.println("Попробуйте еще раз!");
                    yield null;
                }
            };
        }
        try {
            List<Master> masters = masterService.mastersSort(sortType);

            System.out.println("Списко заказов: ");
            for (var v : masters) {
                System.out.println(v);
            }
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
