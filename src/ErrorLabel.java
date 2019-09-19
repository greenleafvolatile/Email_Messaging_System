import javax.swing.*;
import java.awt.*;

class ErrorLabel extends JLabel {

    ErrorLabel(int nrOfLines) {
        final int FONT_SIZE=10;
        setFont(new Font(this.getFont().getName(), Font.PLAIN, FONT_SIZE));
        setForeground(Color.RED);
        setPreferredSize(new Dimension(Integer.MIN_VALUE, this.getFont().getSize() * nrOfLines));
        setHorizontalAlignment(SwingConstants.CENTER);
        setText("");
    }
}
