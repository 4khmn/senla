package main.java.autoservice.ui.menu;

public class Menu {
    private String name;
    private MenuItem[] items;
    private String hint;

    public Menu(String name, String hint, MenuItem[] items) {
        this.name = name;
        this.items = items;
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public String getName() {
        return name;
    }
    public MenuItem[] getItems() {
        return items;
    }
}
