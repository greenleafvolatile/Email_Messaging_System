import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class NewMessagePane {

    public NewMessagePane(){
       showPane();
    }

    private JPanel createMainPanel(){

        JPanel mainPanel=new JPanel(new BorderLayout(10, 10));

        mainPanel.add(createLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createTextFieldsPanel(), BorderLayout.CENTER);
        mainPanel.add(createMessageArea(), BorderLayout.SOUTH);

        return mainPanel;
    }


    private JPanel createLabelsPanel(){
        JPanel labelsPanel=new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel toLabel=new JLabel("To:", SwingConstants.LEFT);
        JLabel subjectLabel=new JLabel("Subject:", SwingConstants.RIGHT);

        labelsPanel.add(toLabel);
        labelsPanel.add(subjectLabel);

        return labelsPanel;
    }

    private JPanel createTextFieldsPanel(){
        JPanel textFieldsPanel=new JPanel(new GridLayout(0, 1, 10, 10));
        JTextField toField=new JTextField();
        JTextField subjectField=new JTextField();
        textFieldsPanel.add(toField);
        textFieldsPanel.add(subjectField);
        return textFieldsPanel;
    }

    public JScrollPane createMessageArea(){
        JTextArea messageArea=new JTextArea(15, 25);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane scrollPane=new JScrollPane(messageArea, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    private JButton[] createButtonsArray(){

        JButton sendButton=new JButton("Send");
        JButton cancelButton=new JButton("Cancel");
        return new JButton[]{sendButton, cancelButton};

    }

    private void showPane(){
        JOptionPane.showOptionDialog(null, createMainPanel(), "New Message", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,  createButtonsArray(), createButtonsArray()[0]);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){

            public void run(){
                new NewMessagePane().showPane();
            }
        });
    }




}
