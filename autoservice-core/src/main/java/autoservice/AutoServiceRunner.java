package autoservice;
import autoservice.model.config.AppConfig;
import autoservice.ui.controller.MenuController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutoServiceRunner {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        MenuController controller = context.getBean(MenuController.class);
        controller.run();
    }
}
