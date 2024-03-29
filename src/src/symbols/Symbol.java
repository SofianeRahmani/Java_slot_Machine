package symbols;

import javax.swing.*;
import java.awt.*;

public interface Symbol {

    String getSymbol();
    ImageIcon getSymbolImage();

    default ImageIcon resizeIcon(ImageIcon originalIcon, int width, int height) {
        Image image = originalIcon.getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
