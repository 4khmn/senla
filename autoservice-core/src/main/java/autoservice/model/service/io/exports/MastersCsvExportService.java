package autoservice.model.service.io.exports;

import autoservice.model.entities.Master;
import autoservice.model.service.MasterService;
import org.springframework.stereotype.Service;

@Service
public class MastersCsvExportService extends CsvExport {

    private final MasterService masterService;
    public MastersCsvExportService(MasterService masterService) {
        super("id,name,salary", "masters.csv");
        this.masterService = masterService;
    }
    @Override
    protected String formatEntity(Object entity) {
        Master master = (Master)entity;
        return String.join(",",
                String.valueOf(master.getId()),
                master.getName(),
                String.valueOf(master.getSalary())
        );
    }

    @Override
    protected Iterable<?> getEntities() {
        return masterService.getMasters();
    }
}
