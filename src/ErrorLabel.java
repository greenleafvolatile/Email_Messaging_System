import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Flow;
import java.util.logging.Logger;

public class ErrorLabel extends JLabel {


    public ErrorLabel() {
        setFont(new Font(this.getFont().getName(), Font.PLAIN, 10));
        setForeground(Color.RED);
        setPreferredSize(new Dimension(Integer.MIN_VALUE, this.getFont().getSize())); // I'd rather not use this.
        setHorizontalAlignment(SwingConstants.CENTER);
        setText("");
    }
}
