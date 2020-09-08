import command.Command;

/**
 * Класс для команды EXIT
 */
public class ExitCommand implements Command {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("До встречи!");
    }
}
