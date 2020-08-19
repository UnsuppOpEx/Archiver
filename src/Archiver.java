import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Archiver {

    public static void main(String[] args) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите полный путь к архиву:");
        String line1 = bufferedReader.readLine();
        Path path = Paths.get(line1);
        ZipFileManager zipFileManager = new ZipFileManager(path);

        System.out.println("Введите путь к архивируемому файлу:");
        String line2 = bufferedReader.readLine();
        Path path2 = Paths.get(line2);
        zipFileManager.createZip(path2);
    }
}