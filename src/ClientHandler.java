import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private static final int SHIFT = 3;

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
                if (request instanceof String) {
                    String decryptedMessage = decrypt((String) request);
                    if (decryptedMessage.equals("PLAY")) {
                        int[] results = playSlotMachine();
                        String encryptedMessage = encrypt(java.util.Arrays.toString(results));
                        System.out.println("results: " + java.util.Arrays.toString(results));
                        System.out.println("Encrypted results sent to client: " + encryptedMessage);
                        outputStream.writeObject(encryptedMessage);
                    }
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

    private String encrypt(String message) {
        return shift(message, SHIFT);
    }

    private String decrypt(String message) {
        return shift(message, -SHIFT);
    }

    private String shift(String message, int shift) {
        StringBuilder result = new StringBuilder();
        for (char character : message.toCharArray()) {
            if (character >= ' ' && character <= '~') {
                int originalAlphabetPosition = character - ' ';
                int newAlphabetPosition = (originalAlphabetPosition + shift) % ('~' - ' ' + 1);
                if (newAlphabetPosition < 0)
                    newAlphabetPosition += ('~' - ' ' + 1);
                char newCharacter = (char) (' ' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }
}
