import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.logging.Logger;

public class RegistrationPane {

    private JTextField firstNameField, lastNameField, userNameField;
    private JPasswordField passwordField, retypePasswordField;

    private JLabel errorLabel;
    private boolean isValidPassword, isVerifiedPassword;


    public RegistrationPane() {

        JOptionPane.showOptionDialog(null, createMainPanel(), "Registration", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, createButtons(), createButtons()[0]);
    }

    private class PasswordFieldListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            comparePasswords();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            comparePasswords();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }

        public void comparePasswords() {

            if (passwordField.getPassword().length!=0 && retypePasswordField.getPassword().length==passwordField.getPassword().length) {
                if (!Arrays.equals(passwordField.getPassword(), retypePasswordField.getPassword())) {
                    errorLabel.setText("Passwords don't match");
                }
                else{
                   isVerifiedPassword=true;
                   Logger.getGlobal().info("Password has been verified");
                }
            }
            else {
                errorLabel.setText("");
            }
        }
    }



    private JPanel createMainPanel(){

        JPanel mainPanel=new JPanel(new BorderLayout(10, 10));

        mainPanel.add(createLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createTextFieldsPanel(), BorderLayout.CENTER);

        errorLabel=new ErrorLabel();

        mainPanel.add(errorLabel, BorderLayout.SOUTH);
        return mainPanel;
    }

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

    private JPanel createTextFieldsPanel(){

        JPanel textFieldsPanel=new JPanel(new GridLayout(0, 1, 10, 10));

        firstNameField=new JTextField(12);
        lastNameField=new JTextField(12);
        userNameField=new JTextField(12);

        passwordField=new JPasswordField(12);
        passwordField.getDocument().addDocumentListener(new PasswordFieldListener());
        passwordField.addFocusListener(new FocusAdapter(){

            public void focusLost(FocusEvent e){
                if(passwordField.getPassword()!=null & ! PassWordVerifier.isCorrectPassword(passwordField.getPassword())){
                    errorLabel.setText("Invalid password");
                }
                else {
                    isValidPassword=true;
                }
            }
        });


        retypePasswordField=new JPasswordField(12);
        retypePasswordField.getDocument().addDocumentListener(new PasswordFieldListener());

        class CustomPanel extends JPanel{

            public CustomPanel(JTextField field, JLabel label){
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(field);
                add(label);
            }
        }

        textFieldsPanel.add(new CustomPanel(firstNameField, new JLabel()));
        textFieldsPanel.add(new CustomPanel(lastNameField, new JLabel()));
        textFieldsPanel.add(new CustomPanel(userNameField, new JLabel("*")));
        textFieldsPanel.add(new CustomPanel(passwordField, new JLabel("*")));
        textFieldsPanel.add(new CustomPanel(retypePasswordField, new JLabel("*")));
        return textFieldsPanel;
    }

    private JButton[] createButtons(){

        JButton createAccountButton=new JButton("Create account");
        createAccountButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event) {
                // Check if required fields have been filled.
                if (userNameField.getText() == "" || passwordField.getPassword().length == 0 || retypePasswordField.getPassword().length == 0) {
                    errorLabel.setText("* required fields!");
                    errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
                // Check is username already exists.
                else if (UserController.getUser(userNameField.getText()) != null) {
                        errorLabel.setText("That username has already been taken!");
                }
                else if(!PassWordVerifier.isCorrectPassword(passwordField.getPassword())){
                    JOptionPane.showMessageDialog(null, "<html> Invalid password. <br />  Passwords must  between 4 adn 8 characters. </html>" );
                }
                else if(isValidPassword && isVerifiedPassword){
                    {
                        String aUsername = userNameField.getText();
                        String firstName = firstNameField.getText();
                        String lastName = lastNameField.getText();
                        char[] aPassword = passwordField.getPassword();
                        User newUser = new User(firstName, lastName, aUsername, aPassword);
                        newUser.addMessage(new Message("Admin", aUsername, "Welcome!", "Welcome to the email_messaging_system!"));
                        UserController.writeUserToFile(newUser);
                        //Logger.getGlobal().info("Nr of users: " + UserController.getNumberOfRegisteredUsers());
                        JOptionPane.getRootFrame().dispose();
                        new LoginPane();
                    }
                }

            }
        });

        JButton cancelButton=new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                JOptionPane.getRootFrame().dispose();
                new LoginPane();
            }
        });
        JButton[] buttons={createAccountButton, cancelButton};
        return buttons;
    }


}
