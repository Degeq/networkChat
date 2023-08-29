import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

//  "C:\\Users\\Dim\\IdeaProjects\\medical-test\\localServlet\\settings.txt";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Приветствую! Введите путь к файлу с настройками подключения: ");
        String path = scanner.nextLine();

        String host = "localhost";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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
                    try {
                        while (!clientSocket.isClosed()) {
                            System.out.println(in.readLine());
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
}