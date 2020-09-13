package archiver.command;

import archiver.ConsoleHelper;

/**
 * Класс для команды EXIT
 */
public class ExitCommand implements Command {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("До встречи!");
    }
}
