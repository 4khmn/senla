package autoservice.model.service.io.exports;


import autoservice.model.exceptions.ExportException;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public void export() {
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
        } catch (IOException e) {
            throw new ExportException("Не удалось записать данные в файл по адресу: " + file.getAbsolutePath(), e);
        }
    }

    protected abstract Iterable<?> getEntities();
}