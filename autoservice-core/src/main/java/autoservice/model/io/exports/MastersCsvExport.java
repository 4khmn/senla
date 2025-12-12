package autoservice.model.io.exports;

import autoservice.model.AutoService;
import autoservice.model.entities.Master;
import autoservice.model.manager.MasterManager;
import config.annotation.Component;
import config.annotation.Inject;
@Component
public class MastersCsvExport extends CsvExport {

    private final MasterManager masterManager;
    @Inject
    public MastersCsvExport(MasterManager masterManager) {
        super("id,name,salary", "masters.csv");
        this.masterManager = masterManager;
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
        return masterManager.getMasters();
    }
}
