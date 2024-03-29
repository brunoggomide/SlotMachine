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
    private SlotMachine slotMachine; // Adicione uma referência a SlotMachine

    public Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Crie apenas uma instância de SlotMachine
            slotMachine = new SlotMachine(this);

            SwingUtilities.invokeLater(() -> {
                slotMachine.setVisible(true); // Exiba a instância existente de SlotMachine
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            outputStream.writeObject("PLAY");
            int[] results = (int[]) inputStream.readObject();
            System.out.println("Results: " + java.util.Arrays.toString(results));
            // Atualize os resultados na interface existente de SlotMachine
            slotMachine.updateResults(results[0], results[1], results[2]);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
