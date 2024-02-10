package symbols;
import javax.swing.*;
public class OrangeSymbol implements Symbol{

    @Override
    public String getSymbol() {
        return "Orange";
    }

    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    @Override
    public ImageIcon getSymbolImage() {
        ImageIcon originalIcon = new ImageIcon("src/img/orange.png");
        return resizeIcon(originalIcon, WIDTH, HEIGHT);
    }


}