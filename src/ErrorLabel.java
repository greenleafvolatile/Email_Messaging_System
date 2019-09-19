import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Flow;
import java.util.logging.Logger;

public class ErrorLabel extends JLabel {


    public ErrorLabel(int nrOfLines) {
        setFont(new Font(this.getFont().getName(), Font.PLAIN, 10));
        setForeground(Color.RED);
        setPreferredSize(new Dimension(Integer.MIN_VALUE, this.getFont().getSize() * nrOfLines)); // I'd rather not use this.
        setHorizontalAlignment(SwingConstants.CENTER);
        setText("");
    }
}
