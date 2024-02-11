package symbols;
import javax.swing.*;
public class OrangeSymbol implements Symbol{

    @Override
    public String getSymbol() {
        return "Orange";
    }

    private static final int WIDTH = 55;
    private static final int HEIGHT = 55;
    @Override
    public ImageIcon getSymbolImage() {
        ImageIcon originalIcon = new ImageIcon("src/src/img/orange.png");
        return resizeIcon(originalIcon, WIDTH, HEIGHT);
    }


}