import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.Arrays;

/**
 * This class provides a login window for the messaging system.
 */
class LoginPane{

    private JTextField usernameField;
    private JLabel errorLabel;
    private JPasswordField passwordField;

    /**
     * Constructor.
     */
    public LoginPane(){
        JOptionPane.showOptionDialog(null, createMainPanel(), "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, createButtonsArray(), createButtonsArray()[0]);
    }

    /**
     * This class will attempt to login the user. It is added to the loginButton and the passwordField (so the user can login with the enter button instead of the button).
     */
    class LoginListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            User user = UserController.getUser(usernameField.getText());
            char[] password = passwordField.getPassword();
            if (user != null) {
                if (Arrays.equals(password, user.getPassword())) {
                    JOptionPane.getRootFrame().dispose();
                    InboxFrame inbox = new InboxFrame(user);
                    inbox.setLocationRelativeTo(null);
                    inbox.setVisible(true);
                } else {
                    errorLabel.setText("Incorrect password!");
                }
            } else {
                errorLabel.setText("Unknown username!");
            }
        }
    }

    /**
     * This method constructs a JPanel with JLabel and JTextAreas that is passed JOptionPane.showOptionDialog.
     * @return a JPanel object
     */
    private JPanel createMainPanel(){

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        final int ERROR_LABEL_NR_OF_LINES=1;
        errorLabel=new ErrorLabel(ERROR_LABEL_NR_OF_LINES);

        mainPanel.add(errorLabel, BorderLayout.SOUTH);
        mainPanel.add(createLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createTextFieldsPanel(), BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * This method creates the JLabels for the mainPanel.
     * @return a JPanel object.
     */
    private JPanel createLabelsPanel(){

        JPanel labelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        labelsPanel.add(new JLabel("Username", SwingConstants.RIGHT));
        labelsPanel.add(new JLabel("Password", SwingConstants.RIGHT));

        return labelsPanel;
    }

    /**
     * This method creates the JTextFields for the mainPanel.
     * @return a JPanel object.
     */
    private JPanel createTextFieldsPanel(){


        /*
          This class allows for clearing the text of the errorLabel when the user corrects either the username or the password.
         */
        class ClearErrorLabelListener implements DocumentListener{

            public void insertUpdate(DocumentEvent documentEvent) {
                clearErrorLabel();
            }
            public void removeUpdate(DocumentEvent documentEvent) {
                clearErrorLabel();
            }

            public void changedUpdate(DocumentEvent documentEvent) {
            }

            private void clearErrorLabel(){
                errorLabel.setText("");
            }
        }

        JPanel textFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));


        usernameField = new JTextField();
        // add a ClearErrorLabelListener to clear the errorLabel's text when the user corrects the username.
        usernameField.getDocument().addDocumentListener(new ClearErrorLabelListener());

        passwordField = new JPasswordField();
        passwordField.addActionListener(new LoginListener());

        // Because the password must consist of 4 to 8 characters(and because I wanted to mess around with DocumentFilter which
        // was new to me), I added a DocumentFilter to limit the number of characters in the passwordField to 8.
        AbstractDocument document=(AbstractDocument) passwordField.getDocument();
        document.setDocumentFilter(new DocumentFilter(){

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String string=fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if(string.length()<=8){ super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Add a documentListener to clear the errorLabel's text when a user corrects the password.
        passwordField.getDocument().addDocumentListener(new ClearErrorLabelListener());
       /* passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent){
                clearErrorLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                clearErrorLabel();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
            }

            void clearErrorLabel(){
                    errorLabel.setText("");
                }
        });*/

        textFieldsPanel.add(usernameField);
        textFieldsPanel.add(passwordField);

        return textFieldsPanel;
    }

    private JButton[] createButtonsArray() {

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginListener());


        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(event -> {
            JOptionPane.getRootFrame().dispose();
            new RegistrationPane();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> System.exit(0));

        return new JButton[]{loginButton, registerButton, cancelButton};
    }
}
