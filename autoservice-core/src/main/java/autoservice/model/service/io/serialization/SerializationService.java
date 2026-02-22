package autoservice.model.service.io.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SerializationService {

    private final ObjectMapper mapper;



    private SerializationService() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate6Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    public void saveStateToFile(SerializationDTO dto, String filename) throws IOException {
        mapper.writeValue(new File(filename), dto);
    }


}
