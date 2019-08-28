import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewMessagePane {


    private User user;
    private JTextArea messageArea;
    private JTextField toField, subjectField;

    public NewMessagePane(User thisUser, JFrame frame){
        User user=thisUser;

        JOptionPane.showOptionDialog(frame, createMainPanel(), "New Message", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,  createButtonsArray(), createButtonsArray()[0]);
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
        toField=new JTextField();
        subjectField=new JTextField();
        textFieldsPanel.add(toField);
        textFieldsPanel.add(subjectField);
        return textFieldsPanel;
    }

    private JScrollPane createMessageArea(){
        messageArea=new JTextArea(15, 25);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane scrollPane=new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JButton[] createButtonsArray(){

        JButton sendButton=new JButton("Send");
        sendButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                User recipient=UserController.getUser(toField.getText());
                // Do something if user does not exist.
                Message message=new Message(UserController.getUsername(user), toField.getText(), subjectField.getText(), messageArea.getText());
                UserController.addMessage(recipient, message);

            }
        });

        JButton cancelButton=new JButton("Cancel");
        return new JButton[]{sendButton, cancelButton};

    }




}
