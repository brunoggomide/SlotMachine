import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object request = inputStream.readObject();
                if (request instanceof String && ((String) request).equals("PLAY")) {
                    int[] results = playSlotMachine();
                    System.out.println("Results sent to client: " + java.util.Arrays.toString(results));
                    outputStream.writeObject(results);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int[] playSlotMachine() {
        Random random = new Random();
        int[] results = new int[3];
        for (int i = 0; i < 3; i++) {
            results[i] = random.nextInt(7);
        }
        return results;
    }
}
