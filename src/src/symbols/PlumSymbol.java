package symbols;
import javax.swing.*;
public class PlumSymbol implements Symbol{

    @Override
    public String getSymbol() {
        return "Plum";
    }

    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    @Override
    public ImageIcon getSymbolImage() {
        ImageIcon originalIcon = new ImageIcon("src/img/plum.png");
        return resizeIcon(originalIcon, WIDTH, HEIGHT);
    }


}