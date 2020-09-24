package archiver;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Получить список файлов в папке
 */
public class FileManager {
    private Path rootPath;
    private List<Path> fileList;

    FileManager(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        this.fileList = new ArrayList<>();
        collectFileList(rootPath);
    }

    public List<Path> getFileList() {
        return fileList;
    }

    /**
     * Добавляет в список названия найденных файлов
     * @param path
     * @throws IOException
     */
    private void collectFileList(Path path) throws IOException {

        if (Files.isRegularFile(path)) {
            fileList.add(rootPath.relativize(path));    //Вычисляем относительный путь файла
        }

        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    collectFileList(entry);     //Применяем рекурсиооный метод поиска
                }
            }
        }
    }
}
