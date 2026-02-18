package autoservice.ui.controller;

import autoservice.model.service.io.serialization.SerializationDTO;
import autoservice.model.service.io.serialization.SerializationService;
import autoservice.ui.factory.IMenuFactory;
import autoservice.ui.menu.Navigator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;
@Component
@RequiredArgsConstructor
public class MenuController {
    private final SerializationDTO dto;
    private final SerializationService serializer;
    private final IMenuFactory factory;
    private final Navigator navigator;


    public void run() {
        navigator.setCurrentMenu(factory.createMainMenu());
        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while (running) {
            navigator.printMenu();
            System.out.print("Ваш выбор: ");
            try {
                int choice = sc.nextInt();
                running = navigator.navigate(choice);
            } catch (Exception e) {
                System.out.println("Глобальная обработка: Неверный выбор!");
                System.out.println(e.getMessage());
                sc.next();
            }
        }

        try {
            serializer.saveStateToFile(dto, "autoservice.json");
        } catch (Exception e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
        System.out.println("Программа завершена.");
    }



}
