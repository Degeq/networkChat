import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        MyServer server = new MyServer();
        server.runServer();
    }

    public static File creationFile(String path) {
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                System.out.println("Файл" + file.getName() + " создан");
            } else {
                file.delete();
                file.createNewFile();
                System.out.println("Файл" + file.getName() + " создан");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return file;
    }

}


