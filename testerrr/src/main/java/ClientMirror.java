import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientMirror {

    private Scanner scanner = new Scanner(System.in);
    private String configurationFilePath;
    private String host = "localhost";
    private File clientLogger = creationFile("clientLogger.txt");

    public ClientMirror() {
        System.out.println("Приветствую! Введите путь к файлу с настройками подключения: ");
        configurationFilePath = scanner.nextLine();
    }

    public void runClient() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(configurationFilePath))) {
            int port = Integer.parseInt(br.readLine());

            try (Socket clientSocket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                System.out.println(in.readLine());
                out.println(scanner.nextLine());

                while (in.readLine().equals("false")) {
                    System.out.println(in.readLine());
                    out.println(scanner.nextLine());
                }

                System.out.println(in.readLine());

                Thread threadPrinter = new Thread(() -> {

                    try (BufferedWriter bos = new BufferedWriter(new FileWriter(clientLogger.getPath(), true))) {
                        String message;
                        while (!clientSocket.isClosed()) {
                            message = in.readLine();
                            System.out.println(message);
                            synchronized (clientLogger) {
                                writer(bos, message);
                            }
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                Thread messageSender = new Thread(() -> {
                    String message;

                    while (true) {
                        message = scanner.nextLine();

                        if (message.equals("/exit")) {
                            out.println(message);
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        } else {
                            out.println(message);
                        }
                    }
                });

                messageSender.start();
                threadPrinter.start();

                messageSender.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    public static void writer(BufferedWriter bos, String message) throws IOException {
        bos.write(message);
        bos.append("\n");
        bos.flush();

    }
}
