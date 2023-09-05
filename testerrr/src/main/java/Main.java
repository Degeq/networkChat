import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

//  "C:\\Users\\Dim\\IdeaProjects\\medical-test\\localServlet\\settings.txt";

    public static void main(String[] args) throws IOException {
        ClientMirror client = new ClientMirror();
        client.runClient();
    }
}