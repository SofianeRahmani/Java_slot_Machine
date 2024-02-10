package symbols;
import javax.swing.*;
public class LemonSymbol implements Symbol{

    @Override
    public String getSymbol() {
        return "Lemon";
    }

    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    @Override
    public ImageIcon getSymbolImage() {
        ImageIcon originalIcon = new ImageIcon("src/img/lemon.png");
        return resizeIcon(originalIcon, WIDTH, HEIGHT);
    }


}