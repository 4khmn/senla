package autoservice.model.io.exports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class CsvExport {
    protected final String header;
    protected final String fileName;

    public CsvExport(String header, String fileName) {
        this.header = header;
        this.fileName = fileName;
    }

    protected abstract String formatEntity(Object entity);

    public void export() throws IOException {
        File dataDir = new File("data");
        if (!dataDir.exists()){
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
    }

    protected abstract Iterable<?> getEntities();
}