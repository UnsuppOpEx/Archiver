package archiver;

/**
 * Команды для работы с архиватором
 */
public enum Operation {

    CREATE("упаковать файлы в архив"),
    ADD("добавить файл в архив"),
    REMOVE("удалить файл из архива"),
    EXTRACT("распаковать архив"),
    CONTENT("просмотреть содержимое архива"),
    EXIT("выход");

    private String line;

    Operation(String s) {
        this.line = s;
    }

    @Override
    public String toString() {
        return line;
    }

}