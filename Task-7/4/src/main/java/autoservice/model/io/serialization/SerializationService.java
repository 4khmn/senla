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

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SerializationService {

    private final ObjectMapper mapper;

    private static SerializationService instance;


    private SerializationService() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

    }
    public static SerializationService getInstance() {
        if (instance == null) {
            instance = new SerializationService();
        }
        return instance;
    }


    public void saveStateToFile(AutoService service, String filename) throws IOException {
        mapper.writeValue(new File(filename), service);
    }

    public AutoService loadStateFromFile(String filename) throws IOException {

        return mapper.readValue(new File(filename), AutoService.class);

    }

}
