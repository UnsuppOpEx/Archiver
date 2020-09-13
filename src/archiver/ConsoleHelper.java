package archiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс для работы с консолью
 */
public class  ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Выводит message в консоль
     * @param message
     */
    public static void writeMessage(String message) {
        System.out.println(message);
    }

    /**
     * Считывает строку с консоли
     * @return
     */
    public static String readString() {
        String line = null;

        try {
            line = reader.readLine();
        } catch (IOException e) {
            System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
        }
        return line;
    }

    /**
     * Считывает число с консоли
     * @return
     */
    public static int readInt() {
        int value = 0;

        try {
            value = Integer.parseInt(reader.readLine().trim());
        } catch (IOException e) {
            System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
        }
        return value;
    }

}
