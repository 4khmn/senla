package autoservice.model.io.exports;

import autoservice.model.entities.Master;
import autoservice.model.service.MasterService;
import config.annotation.Component;
import config.annotation.Inject;
@Component
public class MastersCsvExport extends CsvExport {

    private final MasterService masterService;
    @Inject
    public MastersCsvExport(MasterService masterService) {
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
