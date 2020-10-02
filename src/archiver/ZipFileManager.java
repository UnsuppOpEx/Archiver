package archiver;

import archiver.exception.PathIsNotFoundException;
import archiver.exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private ZipEntry entry;
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * Создает архив
     * @param source
     * @throws Exception
     */
    public void createZip(Path source) throws Exception {
        // Проверяем, существует ли директория, где будет создаваться архив
        // При необходимости создаем ее
        Path zipDirectory = zipFile.getParent();
        if (Files.notExists(zipDirectory))
            Files.createDirectories(zipDirectory);

        // Создаем zip поток
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {

            if (Files.isDirectory(source)) {
                // Если архивируем директорию, то нужно получить список файлов в ней
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();

                // Добавляем каждый файл в архив
                for (Path fileName : fileNames)
                    addNewZipEntry(zipOutputStream, source, fileName);

            } else if (Files.isRegularFile(source)) {

                // Если архивируем отдельный файл, то нужно получить его директорию и имя
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else {

                // Если переданный source не директория и не файл, бросаем исключение
                throw new PathIsNotFoundException();
            }
        }
    }

    /**
     * Добавляет элемент архива
     * @param zipOutputStream
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        Path fullPath = filePath.resolve(fileName);   //Склеивает 2 пути

        try (InputStream inputStream = Files.newInputStream(fullPath)) {
            ZipEntry entry = new ZipEntry(fileName.toString());

            zipOutputStream.putNextEntry(entry);

            copyData(inputStream, zipOutputStream);

            zipOutputStream.closeEntry();
        }
    }

    /**
     * Читать данные из in и записывать в out, пока не вычитает все.
     * @param in
     * @param out
     * @throws Exception
     */
    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    /**
     * Возвращает список свойств файлов в архиве
     * @return
     * @throws Exception
     */
    public  List<FileProperties> getFilesList() throws Exception {
        // Проверяет существует ли файл
        if(!Files.isRegularFile(zipFile))
            throw new WrongZipFileException();


        List<FileProperties> listProperties = new ArrayList<>();

        try(ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {

            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyData(zis, baos);

                FileProperties fileProperties = new FileProperties(
                        zipEntry.getName(), zipEntry.getSize(),
                        zipEntry.getCompressedSize(), zipEntry.getMethod());

                listProperties.add(fileProperties);
                zipEntry = zis.getNextEntry();
            }

        }
        return listProperties;
    }

    /**
     * Распаковка архива
     * @param outputFolder
     * @throws Exception
     */
    public void extractAll(Path outputFolder) throws Exception {
        // Проверяем существует ли zip файл
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            // Создаем директорию вывода, если она не существует
            if (Files.notExists(outputFolder))
                Files.createDirectories(outputFolder);

            // Проходимся по содержимому zip потока (файла)
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                Path fileFullName = outputFolder.resolve(fileName);

                // Создаем необходимые директории
                Path parent = fileFullName.getParent();
                if (Files.notExists(parent))
                    Files.createDirectories(parent);

                try (OutputStream outputStream = Files.newOutputStream(fileFullName)) {
                    copyData(zipInputStream, outputStream);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(zipFile.resolve(path)));
    }

    /**
     * Удаляет элемент архива
     * @param pathList
     * @throws Exception
     */
    public void removeFiles(List<Path> pathList) throws Exception {
        // Проверяем существует ли zip файл
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        // Создаем временный файл
        Path tempZipFile = Files.createTempFile(null, null);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {

                    Path archivedFile = Paths.get(zipEntry.getName());

                    if (!pathList.contains(archivedFile)) {
                        String fileName = zipEntry.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));

                        copyData(zipInputStream, zipOutputStream);

                        zipOutputStream.closeEntry();
                        zipInputStream.closeEntry();
                    } else {
                        ConsoleHelper.writeMessage(String.format("Файл '%s' удален из архива.", archivedFile.toString()));
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }

        // Перемещаем временный файл на место оригинального
        Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Вызывает метод addFiles и передает путь к файлу как параметр
     * @param absolutePath
     * @throws Exception
     */
    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }

    /**
     * Добавляет новый файл в архив
     * @param absolutePathList
     * @throws Exception
     */
    public void addFiles(List<Path> absolutePathList) throws Exception {
        // Проверяем существует ли zip файл
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        // Создаем временный файл
        Path tempZipFile = Files.createTempFile(null, null);
        List<Path> archiveFiles = new ArrayList<>();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    String fileName = zipEntry.getName();
                    archiveFiles.add(Paths.get(fileName));

                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    copyData(zipInputStream, zipOutputStream);

                    zipInputStream.closeEntry();
                    zipOutputStream.closeEntry();

                    zipEntry = zipInputStream.getNextEntry();
                }
            }

            // Архивируем новые файлы
            for (Path file : absolutePathList) {
                if (Files.isRegularFile(file))
                {
                    if (archiveFiles.contains(file.getFileName()))
                        ConsoleHelper.writeMessage(String.format("Файл '%s' уже существует в архиве.", file.toString()));
                    else {
                        addNewZipEntry(zipOutputStream, file.getParent(), file.getFileName());
                        ConsoleHelper.writeMessage(String.format("Файл '%s' добавлен в архиве.", file.toString()));
                    }
                }
                else
                    throw new PathIsNotFoundException();
            }
        }

        // Перемещаем временный файл на место оригинального
        Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }
}

