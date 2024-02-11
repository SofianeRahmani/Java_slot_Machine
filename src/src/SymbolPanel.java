import symbols.*;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.List;
import java.util.*;

public class SymbolPanel extends JPanel {
    private final Symbol[][] symbols;
    private Symbol[][] shuffledSymbols;
    private final Timer shuffleTimer;
    private final int shuffleDuration = 800;
    private final int symbolSize = 100;
    private final int rows = 3;
    private final int columns = 3;

    boolean isShuffling = false;

    public SymbolPanel() {
        symbols = new Symbol[rows][columns];
        initializeSymbols();
        shuffledSymbols = symbols.clone(); // Initial shuffle
        setPreferredSize(new Dimension(symbolSize * columns + (columns - 1) * 10, symbolSize * rows + (rows - 1) * 10));
        setBackground(Color.WHITE);

        shuffleTimer = new Timer(shuffleDuration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleSymbols();
                repaint();
            }
        });
    }

    private void initializeSymbols() {
        List<Symbol> symbolTypes = new ArrayList<>();
        symbolTypes.add(new BellSymbol());
        symbolTypes.add(new LemonSymbol());
        symbolTypes.add(new PlumSymbol());
        symbolTypes.add(new OrangeSymbol());
        symbolTypes.add(new CherrySymbol());

        Collections.shuffle(symbolTypes);

        Random rand = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int randomIndex = rand.nextInt(symbolTypes.size());
                symbols[i][j] = symbolTypes.get(randomIndex);
            }
        }
    }

    private void shuffleSymbols() {
        List<Symbol> flatSymbols = new ArrayList<>();
        for (Symbol[] row : symbols) {
            flatSymbols.addAll(Arrays.asList(row));
        }

        Collections.shuffle(flatSymbols);

        for (int i = 0, k = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                shuffledSymbols[i][j] = flatSymbols.get(k++);
            }
        }
    }

    public void startShuffle() {
        if (!isShuffling) {
            InputStream inputStream = getClass().getResourceAsStream("/music/play_sound.wav");
            SoundEffect.play(inputStream);
            isShuffling = true;

            Thread shuffleThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int steps = 15;

                    for (int i = 0; i < steps; i++) {
                        try {
                            Thread.sleep((shuffleDuration) / steps);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                        shuffleSymbols();
                        repaint();
                    }

                    shuffleSymbols();
                    repaint();

                    isShuffling = false;

                    if (shuffleCompleteCallback != null) {
                        shuffleCompleteCallback.run();
                    }
                }
            });

            shuffleThread.start();
        }
    }

    private Runnable shuffleCompleteCallback;

    public void setShuffleCompleteCallback(Runnable callback) {
        this.shuffleCompleteCallback = callback;
    }

    public Symbol[][] getShuffledSymbols() {
        return shuffledSymbols;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Clear the background
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Calculate the total gap size
        int totalGapWidth = getWidth() - (symbolSize * columns);
        int totalGapHeight = getHeight() - (symbolSize * rows);

        // Calculate individual gap sizes
        int xGap = totalGapWidth / (columns + 1);
        int yGap = totalGapHeight / (rows + 1);

        // Calculate the starting positions
        int x = xGap;
        int y = yGap;

        // Use a loop to draw the symbols in a 3x3 grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Use shuffledSymbols to get the current symbol
                Symbol symbol = shuffledSymbols[i][j];
                ImageIcon icon = symbol.getSymbolImage();
                if (icon != null) {
                    // Scale the image to the desired symbol size
                    Image scaledImage = icon.getImage().getScaledInstance(symbolSize, symbolSize, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                    // Draw the image at the calculated position
                    icon.paintIcon(this, g, x, y);
                }
                // Move to the next column
                x += symbolSize + xGap;
            }
            // Reset the x position and move to the next row
            x = xGap;
            y += symbolSize + yGap;
        }
    }

}