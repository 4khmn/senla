package autoservice.model.io.serialization;

import autoservice.model.AutoService;
import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.manager.GarageSpotManager;
import autoservice.model.manager.MasterManager;
import autoservice.model.manager.OrderManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import config.annotation.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Component
public class SerializationService {

    private final ObjectMapper mapper;



    private SerializationService() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    public void saveStateToFile(AutoService service, String filename) throws IOException {
        mapper.writeValue(new File(filename), service);
    }

    public void loadStateFromFile(AutoService target, String filename) throws IOException {
        mapper.readerForUpdating(target).readValue(new File(filename));
        target.getGarageManager().updateGlobalId();
        target.getMasterManager().updateGlobalId();
        target.getOrderManager().updateGlobalId();

    }

}
