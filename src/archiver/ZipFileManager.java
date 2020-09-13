package archiver;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private ZipEntry entry;
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * Создает архив если он не создан,
     * считывает данные из файла и записывает в архив.
     * @param source
     * @throws Exception
     */
    public void createZip(Path source) throws Exception {
        try (OutputStream out = Files.newOutputStream(zipFile);
             InputStream input = Files.newInputStream(source)) {

            ZipOutputStream zos = new ZipOutputStream(out);

            entry = new ZipEntry(source.getFileName().toString());
            zos.putNextEntry(entry);


            for (int c = input.read(); c != -1; c = input.read()) {
                zos.write(c);
            }
            zos.flush();
            zos.close();
//            zos.closeEntry();

        }
    }

}
