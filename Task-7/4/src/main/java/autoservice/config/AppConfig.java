package autoservice.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final boolean allowGarageSpotAddRemove;
    private final boolean allowOrderShiftTime;
    private final boolean allowOrderDelete;

    public AppConfig() {
        Properties p = new Properties();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("autoservice.properties")) {
            if (is == null) {
                throw new RuntimeException("Не найден autoservice.properties");
            }
            p.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения properties", e);
        }

        allowGarageSpotAddRemove = Boolean.parseBoolean(p.getProperty("garage.allowToAddRemove", "true"));
        allowOrderShiftTime = Boolean.parseBoolean(p.getProperty("orders.allowToShiftTime", "false"));
        allowOrderDelete = Boolean.parseBoolean(p.getProperty("orders.allowToDelete", "true"));
    }

    public boolean isGarageSpotAllowToAddRemove() { return allowGarageSpotAddRemove; }
    public boolean isOrderAllowToShiftTime() { return allowOrderShiftTime; }
    public boolean isOrderAllowToDelete() { return allowOrderDelete; }
}
