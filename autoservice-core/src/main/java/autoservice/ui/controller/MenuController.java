package autoservice.ui.controller;

import autoservice.model.AutoService;
import autoservice.model.io.serialization.SerializationService;
import autoservice.ui.factory.ConsoleMenuFactory;
import autoservice.ui.factory.IMenuFactory;
import autoservice.ui.menu.Navigator;
import config.annotation.Component;
import config.annotation.Inject;

import java.io.IOException;
import java.util.Scanner;
@Component
public class MenuController {
    private AutoService service;
    private final SerializationService serializer;
    private final IMenuFactory factory;
    private final Navigator navigator;

    @Inject
    public MenuController(AutoService service,  SerializationService serializer, Navigator navigator, ConsoleMenuFactory menuFactory) {
        this.serializer = serializer;
        this.service = service;
        this.navigator = navigator;
        this.factory = menuFactory;
    }

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
            }
            catch(Exception e){
                System.out.println("Неверный выбор!");
                sc.next();
            }
        }

        try {
            serializer.saveStateToFile(service, "autoservice.json");
        } catch (Exception e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }

        System.out.println("Программа завершена.");
    }



}
