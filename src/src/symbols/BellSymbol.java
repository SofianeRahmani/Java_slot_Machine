package symbols;
import javax.swing.*;
public class BellSymbol implements Symbol{

    @Override
    public String getSymbol() {
        return "Bell";
    }

    private static final int WIDTH = 55;
    private static final int HEIGHT = 55;

    @Override
    public ImageIcon getSymbolImage() {
        ImageIcon originalIcon = new ImageIcon("src/img/bell.png");
        return resizeIcon(originalIcon, WIDTH, HEIGHT);
    }


}