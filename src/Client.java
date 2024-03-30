import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.SwingUtilities;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;
    private SlotMachine slotMachine;
    private static final int SHIFT = 3;

    public Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            slotMachine = new SlotMachine(this);

            SwingUtilities.invokeLater(() -> slotMachine.setVisible(true));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            String encryptedMessage = encrypt("PLAY");
            outputStream.writeObject(encryptedMessage);
            String encryptedResults = (String) inputStream.readObject();
            String decryptedResults = decrypt(encryptedResults);
            System.out.println("Decrypted Results: " + decryptedResults);

            String[] stringResults = decryptedResults.substring(1, decryptedResults.length() - 1).split(", ");
            int[] intResults = new int[stringResults.length];
            for (int i = 0; i < stringResults.length; i++) {
                intResults[i] = Integer.parseInt(stringResults[i].trim());
            }

            slotMachine.updateResults(intResults[0], intResults[1], intResults[2]);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) {
        new Client();
    }
}
