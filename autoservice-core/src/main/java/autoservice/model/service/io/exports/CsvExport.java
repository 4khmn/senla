package autoservice.model.service.io.exports;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
@Slf4j
public abstract class CsvExport {
    protected final String header;
    protected final String fileName;

    public CsvExport(String header, String fileName) {
        this.header = header;
        this.fileName = fileName;
    }

    protected abstract String formatEntity(Object entity);

    public void export() throws IOException {
        log.info("Start exporting data to {}", fileName);
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File file = new File(dataDir, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(header);
            writer.newLine();
            for (Object entity : getEntities()) {
                writer.write(formatEntity(entity));
                writer.newLine();
            }
        }
        log.info("Data successfully exported to {}", fileName);
    }

    protected abstract Iterable<?> getEntities();
}