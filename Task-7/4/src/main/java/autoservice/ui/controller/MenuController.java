package autoservice.ui.controller;

import autoservice.ui.factory.ConsoleMenuFactory;
import autoservice.ui.factory.IMenuFactory;
import autoservice.ui.menu.Navigator;

import java.util.Scanner;

public class MenuController {
    private final IMenuFactory factory = new ConsoleMenuFactory();
    private final Navigator navigator = Navigator.getInstance();

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

        System.out.println("Программа завершена.");
    }



}
