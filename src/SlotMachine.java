import javax.swing.*;
import java.awt.*;

public class SlotMachine extends JFrame {
    private Client client; // Referência ao cliente para comunicação com o servidor
    private JLabel n1, n2, n3, lblCredit, jackpotMessage;
    private JButton btnPlay, btnAddMoney, btnConfirmAdd, btnEnd;
    private JTextField txtAddMoney;
    private int credit = 0; // Crédito do usuário

    public SlotMachine(Client client) {
        this.client = client;
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 90, 556, 577);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(panel);
        panel.setLayout(null);

        // Configurações dos elementos de interface
        n1 = new JLabel("0");
        n1.setBounds(55, 230, 150, 200);
        n1.setFont(new Font("Calibri", Font.BOLD, 200));
        panel.add(n1);

        n2 = new JLabel("0");
        n2.setBounds(230, 230, 150, 200);
        n2.setFont(new Font("Calibri", Font.BOLD, 200));
        panel.add(n2);

        n3 = new JLabel("0");
        n3.setBounds(405, 230, 150, 200);
        n3.setFont(new Font("Calibri", Font.BOLD, 200));
        panel.add(n3);

        jackpotMessage = new JLabel("JACKPOT");
        jackpotMessage.setFont(new Font("Calibri", Font.BOLD, 50));
        jackpotMessage.setForeground(Color.RED);
        jackpotMessage.setBounds(150, 100, 300, 50);
        jackpotMessage.setHorizontalAlignment(SwingConstants.CENTER);
        jackpotMessage.setVisible(false);
        panel.add(jackpotMessage);

        btnAddMoney = new JButton("Adicionar Dinheiro");
        btnAddMoney.setBounds(20, 10, 150, 30);
        panel.add(btnAddMoney);

        txtAddMoney = new JTextField();
        txtAddMoney.setBounds(175, 10, 115, 30);
        txtAddMoney.setVisible(false);
        panel.add(txtAddMoney);

        btnConfirmAdd = new JButton("Confirmar");
        btnConfirmAdd.setBounds(295, 10, 100, 30);
        btnConfirmAdd.setVisible(false);
        panel.add(btnConfirmAdd);

        lblCredit = new JLabel("Saldo: R$ " + credit);
        lblCredit.setBounds(400, 10, 150, 30);
        panel.add(lblCredit);

        btnPlay = new JButton("JOGAR");
        btnPlay.setBounds(145, 455, 250, 65);
        btnPlay.setFont(new Font("Calibri", Font.BOLD, 30));
        btnPlay.setBackground(Color.WHITE);
        panel.add(btnPlay);

        btnEnd = new JButton("ENCERRAR JOGO");
        btnEnd.setBounds(20, 50, 150, 30);
        panel.add(btnEnd);

        // Adicionar ação para o botão de adicionar dinheiro
        btnAddMoney.addActionListener(e -> {
            txtAddMoney.setVisible(true);
            btnConfirmAdd.setVisible(true);
            btnAddMoney.setEnabled(false);
        });

        // Confirmar adição de dinheiro
        btnConfirmAdd.addActionListener(e -> {
            try {
                int valueToAdd = Integer.parseInt(txtAddMoney.getText());
                credit += valueToAdd;
                lblCredit.setText("Saldo: R$ " + credit);
                txtAddMoney.setText("");
                txtAddMoney.setVisible(false);
                btnConfirmAdd.setVisible(false);
                btnAddMoney.setEnabled(true);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Adicione um valor valido.");
                btnAddMoney.setEnabled(true);
            }
        });

        // Ação para o botão PLAY
        btnPlay.addActionListener(e -> {
            if (credit >= 1) {
                credit -= 1;
                lblCredit.setText("Saldo: R$ " + credit);
                client.play(); // Chama o método play do Client para jogar
            } else {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
            }
        });

        // Finalizar o jogo
        btnEnd.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Seu jogo foi encerrado com R$ " + credit);
            System.exit(0);
        });
    }

    public void updateResults(int result1, int result2, int result3) {
        jackpotMessage.setVisible(false);
        n1.setText(Integer.toString(result1));
        n2.setText(Integer.toString(result2));
        n3.setText(Integer.toString(result3));

        if (result1 == 7 && result2 == 7 && result3 == 7) {
            credit += 500;
            jackpotMessage.setVisible(true);
        } else if ((result1 == 0 && result2 == 0 && result3 == 0) ||
                (result1 == 1 && result2 == 1 && result3 == 1) ||
                (result1 == 2 && result2 == 2 && result3 == 2) ||
                (result1 == 3 && result2 == 3 && result3 == 3) ||
                (result1 == 4 && result2 == 4 && result3 == 4) ||
                (result1 == 5 && result2 == 5 && result3 == 5) ||
                (result1 == 6 && result2 == 6 && result3 == 6)) {
            credit += 25;
            jackpotMessage.setVisible(true);
        }

        // Atualizar o texto do crédito
        lblCredit.setText("Saldo: R$ " + credit);
    }

}
