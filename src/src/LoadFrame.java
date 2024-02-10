import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadFrame extends JFrame {
    private Font buttonFont;
    private Font textFontOutLine;
    private Font textFont;
    private final List<String[]> saveData = readSaveData();

    public LoadFrame() {
        initFonts();
        BackgroundMusic.getInstance().addReference();
        setupUI();
    }

    private void initFonts() {
        buttonFont = loadFont("ProtestRevolution-Regular.ttf", 60f);
        textFont = loadFont("ProtestRevolution-Regular.ttf", 50f);
    }

    private Font loadFont(String path, float size) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Font file not found: " + path);
            }
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupUI() {
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(28, 155, 140));
        setLayout(null);
        Container con = getContentPane();

        JButton backButton = createButton("Back", 750, 575);
        JTextField saveNameField = createTextField(620, 110);
        JButton loadButton = createButton("Load", 150, 575);
        JTextArea namesTextArea = createTextArea();

        loadButton.addActionListener(e -> loadAction(saveNameField.getText()));
        backButton.addActionListener(e -> backAction());

        JLabel nameLabel = createLabel("Enter your name: ", textFont, 50, 100);
        JLabel savedNames = createLabel("Saved Names:", textFont, 50, 170);

        namesTextArea.setText(String.join("\n", saveData.stream().map(data -> "-" + data[0]).toArray(String[]::new)));

        JScrollPane scrollPane = new JScrollPane(namesTextArea);
        setupScrollPane(scrollPane);

        addComponents(con, backButton, saveNameField, loadButton, nameLabel, savedNames, scrollPane);
        setVisible(true);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBorderPainted(false);
        button.setForeground(new Color(9, 51, 40));
        button.setBackground(new Color(28, 155, 111));
        button.setFocusable(false);
        button.setBounds(x, y, 300, 70);
        return button;
    }

    private JTextField createTextField(int x, int y) {
        JTextField field = new JTextField(10);
        field.setBackground(new Color(240, 202, 153));
        field.setBounds(x, y, 500, 50);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setFont(textFont);
        return field;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(textFontOutLine);
        area.setForeground(Color.WHITE);
        area.setEditable(false);
        area.setBackground(new Color(113, 155, 28));
        area.setBorder(null);
        return area;
    }

    private JLabel createLabel(String text, Font font, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBounds(x, y, 900, 80);
        label.setForeground(new Color(0, 234, 172));
        return label;
    }

    private void setupScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(null);
        scrollPane.setBounds(100, 250, 500, 250);
    }

    private void addComponents(Container con, Component... components) {
        for (Component component : components) {
            con.add(component);
        }
    }

    private void loadAction(String inputName) {
        if (saveData.stream().anyMatch(data -> data[0].equals(inputName))) {
            double loadedBalance = getBalanceForName(inputName);
            System.out.println("Loading game with name: " + inputName + " and balance: " + loadedBalance);
            dispose();
            SwingUtilities.invokeLater(() -> new SlotGameFrame(loadedBalance, inputName));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid name. Please enter a valid name.");
        }
    }

    private void backAction() {
        dispose();
        SwingUtilities.invokeLater(SlotMenuFrame::new);
    }

    private double getBalanceForName(String playerName) {
        return saveData.stream()
                .filter(data -> data[0].equals(playerName))
                .mapToDouble(data -> Double.parseDouble(data[1]))
                .findFirst()
                .orElse(0.0);
    }

    private List<String[]> readSaveData() {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/log/player.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 2) {
                    data.add(new String[]{parts[0], parts[1]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
