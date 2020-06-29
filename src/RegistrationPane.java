import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This class provides a GUI for users to create a new account.
 */
class RegistrationPane {

    private JTextField firstNameField, lastNameField, usernameField;
    private JPasswordField passwordField, retypePasswordField;
    private JLabel errorLabel;
    private boolean isValidPassword, isVerifiedPassword, isValidUsername;


    /**
     * Constructor
     */
    public RegistrationPane() {
        JOptionPane.showOptionDialog(null, createMainPanel(), "Registration", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, createButtons(), createButtons()[0]);
    }


    /**
     * This method creates the main panel displayed on the JOptionPane.
     * @return a JPanel object.
     */
    private JPanel createMainPanel(){

        JPanel mainPanel=new JPanel(new BorderLayout(10, 10));

        mainPanel.add(createLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createTextFieldsPanel(), BorderLayout.CENTER);

        final int ERROR_LABEL_NR_OF_LINES=3;
        errorLabel=new ErrorLabel(ERROR_LABEL_NR_OF_LINES);

        mainPanel.add(errorLabel, BorderLayout.SOUTH);
        return mainPanel;
    }


    /**
     * This method creates a JPanel with JLabels as part of the main panel.
     * @return a JPanel object.
     */
    private JPanel createLabelsPanel(){

        JPanel labelsPanel=new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel firstNameLabel=new JLabel("First Name:", SwingConstants.LEFT);
        JLabel lastNameLabel=new JLabel("Last name:", SwingConstants.LEFT);
        JLabel usernameLabel=new JLabel("Username:", SwingConstants.LEFT);
        JLabel passwordLabel=new JLabel("Password:", SwingConstants.LEFT);
        JLabel passwordRetypeLabel=new JLabel("Re-type password:", SwingConstants.LEFT);

        labelsPanel.add(firstNameLabel);
        labelsPanel.add(lastNameLabel);
        labelsPanel.add(usernameLabel);
        labelsPanel.add(passwordLabel);
        labelsPanel.add(passwordRetypeLabel);

        return labelsPanel;
    }

    /**
     * this methods creates JTextField objects as part of the main panel.
     * @return a JPanel object.
     */
    private JPanel createTextFieldsPanel(){

        final int NR_OF_COLUMNS=10;
        JPanel textFieldsPanel=new JPanel(new GridLayout(0, 1, 10, 10));

        firstNameField=new JTextField(NR_OF_COLUMNS);

        lastNameField=new JTextField(NR_OF_COLUMNS);

        usernameField=new JTextField(NR_OF_COLUMNS);
        // Add a DocumentListener that informs the user that the  username that was typed is already bound to an account.
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {

                validateUsername();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {

                validateUsername();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }

            private void validateUsername(){
                if(UserController.getUser(usernameField.getText())!=null){
                    errorLabel.setText("That username has already been taken!");
                    isValidUsername=false;

                }
                else{
                    isValidUsername=true;
                    errorLabel.setText("");
                }
            }
        });

        passwordField=new JPasswordField(NR_OF_COLUMNS);
        // Add a FocusListener that informs the user when an invalid password has been chosen.
        passwordField.addFocusListener(new FocusAdapter(){

            public void focusLost(FocusEvent e){
                super.focusLost(e);
                // If password is not of valid format display message.
                if(passwordField.getPassword().length < 2 || passwordField.getPassword().length < 4 || !PassWordVerifier.isCorrectPassword(passwordField.getPassword())){
                    errorLabel.setText("<html>Password must be between 2 and 4 characters. <br /> Must contain 1 uppercase and 1 lowercase character.</html>");
                }
                else {
                    if(retypePasswordField.getPassword().length!=0 && !Arrays.equals(passwordField.getPassword(), retypePasswordField.getPassword())) {
                        errorLabel.setText("Passwords don't match");
                    }
                    isValidPassword=true;
                }
            }
        });
        // Add a Document Listener that clears the error label's text when a user goes back to correct an invalid password.
        passwordField.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent d){
               clearErrorLabel();
            }

            public void removeUpdate(DocumentEvent d){
                clearErrorLabel();
            }

            public void changedUpdate(DocumentEvent d){}

            private void clearErrorLabel() {
                errorLabel.setText("");
            }
        });


        retypePasswordField=new JPasswordField(NR_OF_COLUMNS);
        // Add a DocumentListener that checks if the password in the retypePasswordField matches that in the passwordField.
        retypePasswordField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                comparePasswords();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                comparePasswords();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}


            private void comparePasswords() {


                // If the password is of a valid format then compare retype-password field content to password field content.
                if(isValidPassword){
                    if(!Arrays.equals(passwordField.getPassword(), retypePasswordField.getPassword())) {
                        errorLabel.setText("Passwords don't match");
                    }
                    else{
                        isVerifiedPassword=true;
                        errorLabel.setText("");
                    }
                }
            }
        });


        class CustomPanel extends JPanel{

            private CustomPanel(JTextField field, JLabel label){
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(field);
                add(label);
            }
        }

        textFieldsPanel.add(new CustomPanel(firstNameField, new JLabel()));
        textFieldsPanel.add(new CustomPanel(lastNameField, new JLabel()));
        textFieldsPanel.add(new CustomPanel(usernameField, new JLabel("*")));
        textFieldsPanel.add(new CustomPanel(passwordField, new JLabel("*")));
        textFieldsPanel.add(new CustomPanel(retypePasswordField, new JLabel("*")));
        return textFieldsPanel;
    }

    /**
     * This methods creates a 'create account' button and a 'cancel' button to replace the default buttons of the JOptionPane.
     * @return a array of (2) JButton objects.
     */
    private JButton[] createButtons(){

        JButton createAccountButton=new JButton("Create account");
        createAccountButton.addActionListener(event -> {
            // Check if required fields have been filled.
            if ((usernameField.getText().equals("") || passwordField.getPassword().length == 0 || retypePasswordField.getPassword().length == 0)) {
                errorLabel.setText("* Required fields!");
                errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }


            // If all required fields have been filled AND password is valid AND has been verified THEN create new User object.
            else if(isVerifiedPassword && isValidUsername){

                    String aUsername = usernameField.getText();
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    char[] aPassword = passwordField.getPassword();
                    User newUser = new User(firstName, lastName, aUsername, aPassword);
                    newUser.addMessage(new Message("Admin", aUsername, "Welcome!", "Welcome to the email_messaging_system!"));
                    UserController.writeUserToFile(newUser);
                    JOptionPane.getRootFrame().dispose();
                    new LoginPane();
                }
            });

        JButton cancelButton=new JButton("Cancel");
        cancelButton.addActionListener(event -> {
            JOptionPane.getRootFrame().dispose();
            new LoginPane();
        });

        return new JButton[]{createAccountButton, cancelButton};
    }
}
