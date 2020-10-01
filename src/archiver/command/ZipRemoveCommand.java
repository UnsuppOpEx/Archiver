package archiver.command;

import archiver.ConsoleHelper;
import archiver.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Команда удаления файла из архива
 */
public class ZipRemoveCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Удаление файла из архива.");

        ZipFileManager zipFileManager = getZipFileManager();

        ConsoleHelper.writeMessage("Введите относительный путь файла в архиве:");
        Path sourcePath = Paths.get(ConsoleHelper.readString());
        zipFileManager.removeFile(sourcePath);

        ConsoleHelper.writeMessage("Удаление из архива завершено.");
    }
}
