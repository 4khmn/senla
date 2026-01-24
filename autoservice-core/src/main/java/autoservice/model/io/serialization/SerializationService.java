package autoservice.model.io.serialization;

import autoservice.model.AutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import config.annotation.Component;

import java.io.File;
import java.io.IOException;

@Component
public class SerializationService {

    private final ObjectMapper mapper;



    private SerializationService() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate5Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    public void saveStateToFile(AutoService service, String filename) throws IOException {
        mapper.writeValue(new File(filename), service);
    }


}
