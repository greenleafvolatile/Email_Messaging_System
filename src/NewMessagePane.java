import javax.swing.*;
import java.awt.*;

/**
 * This class provides a GUI for the user to send a new message with.
 */
class NewMessagePane {

    private final User user;
    private JTextArea messageArea;
    private JTextField toField, subjectField;

    /**
     * Constructor
     * @param thisUser a User object. The user that logged in.
     */
    public NewMessagePane(User thisUser){
        this.user=thisUser;
        JOptionPane.showOptionDialog(null, createMainPanel(), "New Message", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,  createButtonsArray(), createButtonsArray()[0]);
    }

    /**
     * This method creates the main panel displayed on the JOptionPane.
     * @return a JPanel object.
     */
    private JPanel createMainPanel(){
        JPanel mainPanel=new JPanel(new BorderLayout(10, 10));
        mainPanel.add(createLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createTextFieldsPanel(), BorderLayout.CENTER);
        mainPanel.add(createMessageArea(), BorderLayout.SOUTH);
        return mainPanel;
    }


    /**
     * This method creates a JPanel with JLabels as part of the main panel.
     * @return a JPanel object.
     */
    private JPanel createLabelsPanel(){
        JPanel labelsPanel=new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel toLabel=new JLabel("To:", SwingConstants.LEFT);
        JLabel subjectLabel=new JLabel("Subject:", SwingConstants.RIGHT);

        labelsPanel.add(toLabel);
        labelsPanel.add(subjectLabel);
        return labelsPanel;
    }

    /**
     * this methods creates JTextField objects as part of the main panel.
     * @return a JPanel object.
     */
    private JPanel createTextFieldsPanel(){
        JPanel textFieldsPanel=new JPanel(new GridLayout(0, 1, 10, 10));

        toField=new JTextField();
        subjectField=new JTextField();

        textFieldsPanel.add(toField);
        textFieldsPanel.add(subjectField);
        return textFieldsPanel;
    }

    /**
     * This methods creates a JTextArea object for the message text as part of the main panel.
     * @return a JScrollPane object.
     */
    private JScrollPane createMessageArea(){
        messageArea=new JTextArea(15, 25);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        return new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * This methods creates a 'send' button and a 'cancel' button to replace the default buttons of the JOptionPane.
     * @return a array of (2) JButton objects.
     */
    private JButton[] createButtonsArray(){

        JButton sendButton=new JButton("Send");
        sendButton.addActionListener(event ->{

            User recipient=UserController.getUser(toField.getText());

            if(recipient!=null){

                Message message=new Message(UserController.getUsername(user), toField.getText(), subjectField.getText(), messageArea.getText());
                // Add new message to User object.
                UserController.addMessage(recipient, message);
                // Delete old User object from file.
                UserController.removeUserFromFile(recipient);
                // Write updated User object to file.
                UserController.writeUserToFile(recipient); // writes updated User object to file.
                JOptionPane.getRootFrame().dispose();
            }
            else{
                // send generic message-could-not-be-delivered message.
                Message message = new Message(UserController.getUsername(user), UserController.getUsername(user), subjectField.getText(), "A message that you sent could not be delivered to one or more of its recipients: \n" + toField.getText() + "\n" + messageArea.getText());
                UserController.addMessage(user, message);
                UserController.removeUserFromFile(user);
                UserController.writeUserToFile(user);
                JOptionPane.getRootFrame().dispose();
            }
        });

        JButton cancelButton=new JButton("Cancel");
        cancelButton.addActionListener(event -> JOptionPane.getRootFrame().dispose());
        return new JButton[]{sendButton, cancelButton};
    }




}
