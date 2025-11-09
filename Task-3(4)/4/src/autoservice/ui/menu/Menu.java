package autoservice.ui.menu;

public class Menu {
    private String name;
    private MenuItem[] items;

    public Menu(String name, MenuItem[] items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }
    public MenuItem[] getItems() {
        return items;
    }
}
