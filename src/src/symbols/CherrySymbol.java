package symbols;

import javax.swing.*;

public class CherrySymbol implements Symbol {

    @Override
    public String getSymbol() {
        return "Cherry";
    }

    private static final int WIDTH = 55;
    private static final int HEIGHT = 55;

    @Override
    public ImageIcon getSymbolImage() {
        ImageIcon originalIcon = new ImageIcon("src/src/img/cherry.png");
        return resizeIcon(originalIcon, WIDTH, HEIGHT);
    }

}