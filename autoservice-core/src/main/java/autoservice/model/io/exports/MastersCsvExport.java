package autoservice.model.io.exports;

import autoservice.model.AutoService;
import autoservice.model.entities.Master;
import autoservice.model.manager.MasterManager;

public class MastersCsvExport extends CsvExport {


    public MastersCsvExport() {
        super("id,name,salary", "masters.csv");
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
        return AutoService.getInstance().getMasterManager().getMasters();
    }
}
