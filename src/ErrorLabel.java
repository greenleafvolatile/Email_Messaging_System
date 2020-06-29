import javax.swing.*;
import java.awt.*;

class ErrorLabel extends JLabel {

    /**
     * Constructor
     * @param nrOfLines, the number of lines of text to be displayed on the label.
     */
    ErrorLabel(int nrOfLines) {
        final int FONT_SIZE=10;
        setFont(new Font(this.getFont().getName(), Font.PLAIN, FONT_SIZE));
        setForeground(Color.RED);
        setPreferredSize(new Dimension(0, this.getFont().getSize() * nrOfLines));
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}
