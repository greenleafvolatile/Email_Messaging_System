import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class LoginPane {

    private JTextField usernameField;
    private JLabel errorLabel;
    private JPasswordField passwordField;
    private User user;

    public LoginPane(){
        JOptionPane.showOptionDialog(null, createMainPanel(), "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, createButtonsArray(), createButtonsArray()[0]);
    }

    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent even){

            user = UserController.getUser(usernameField.getText());
            char[] password=passwordField.getPassword();
            if(user!=null){
                if(Arrays.equals(password, user.getPassword())){

                    JOptionPane.getRootFrame().dispose();

                    InboxFrame inbox=new InboxFrame(user);
                    inbox.setLocationRelativeTo(null);
                    inbox.setVisible(true);
                }
                else{
                    errorLabel.setText("Incorrect password!");
                }
            }
            else{

                errorLabel.setText("Unknown username!");
            }
        }
    }

    private JPanel createMainPanel(){

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        errorLabel=new ErrorLabel();

        mainPanel.add(errorLabel, BorderLayout.SOUTH);
        mainPanel.add(createLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createTextFieldsPanel(), BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createLabelsPanel(){

        JPanel labelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        labelsPanel.add(new JLabel("Username", SwingConstants.RIGHT));
        labelsPanel.add(new JLabel("Password", SwingConstants.RIGHT));

        return labelsPanel;
    }

    private JPanel createTextFieldsPanel(){


        JPanel textFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        usernameField = new JTextField();
        usernameField.addFocusListener(new FocusAdapter(){

            public void focusGained(FocusEvent e){
                if(user==null){
                    errorLabel.setText("");
                }
            }
        });
        passwordField = new JPasswordField();
        passwordField.addActionListener(new LoginListener());
        //passwordField.addFocusListener(new removeErrorTextListener());

        // Password must consist of between 4 and 6 characters containing at least one uppercase and one lowercase character.
        // I added a DocumentFilter to limit the number of characters in the passwordField to 6.

        AbstractDocument document=(AbstractDocument) passwordField.getDocument();
        document.setDocumentFilter(new DocumentFilter(){

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String string=fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if(string.length()<=6){ super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Add a documentListener to clear clear the errorLabel's text when a user corrects the password without the passwordField having lost focus.
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        textFieldsPanel.add(usernameField);
        textFieldsPanel.add(passwordField);

        return textFieldsPanel;
    }

    private JButton[] createButtonsArray() {

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginListener());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(event -> {
            /*if(UserController.isFirstWrite()){
                JOptionPane.showInputDialog(null, "Please provide a path for the user data file: ", null, JOptionPane.INFORMATION_MESSAGE);


            }*/
            JOptionPane.getRootFrame().dispose();
            new RegistrationPane();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> {

                System.exit(0);
        });

        return new JButton[]{loginButton, registerButton, cancelButton};
    }
}
