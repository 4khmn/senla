package main.java.autoservice.ui.menu;

public class Navigator {
    private static Navigator instance;
    private Menu currentMenu;
    private Menu mainMenu;
    private Navigator() {}

    public static Navigator getInstance() {
        if (instance == null)
            instance = new Navigator();
        return instance;
    }
    public void setCurrentMenu(Menu menu) {
        if (mainMenu == null) mainMenu = menu;
        this.currentMenu = menu;
    }
    public void goToMainMenu() {
        this.currentMenu = mainMenu;
    }


    public void printMenu() {
        System.out.println("\n=== " + currentMenu.getName() + " ===");
        if (currentMenu.getHint()!=null) {
            System.out.println(currentMenu.getHint());
        }

        MenuItem[] items = currentMenu.getItems();
        for (int i = 0; i < items.length; i++)
            System.out.println((i + 1) + ". " + items[i].getTitle());
        System.out.println("0. Выход");
    }

    public boolean navigate(int choice) {
        if (choice == 0) {
            return false;
        }

        MenuItem[] items = currentMenu.getItems();
        if (choice > 0 && choice <= items.length) {
            MenuItem item = items[choice - 1];
            if (item.getAction()!=null){
                item.doAction();
            }
            else if (item.getNextMenu() != null) {
                currentMenu = item.getNextMenu();
            }
            else{
                System.out.println("Ошибка");
            }
        }
        else {
            System.out.println("Неверный выбор!");
        }
        return true;
    }
}
