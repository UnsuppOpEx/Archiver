package archiver.command;

import archiver.ConsoleHelper;
import archiver.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Общий класс для работы непосредственно с архивом
 */
public abstract class ZipCommand implements Command {

    /**
     * Считывает с консоли путь к архиву и возвращает для него объект
     * @return
     * @throws Exception
     */
    public ZipFileManager getZipFileManager() throws Exception {
        ConsoleHelper.writeMessage("Введите полный путь файла архива:");
        Path zipPath = Paths.get(ConsoleHelper.readString());
        return new ZipFileManager(zipPath);
    }
}
