import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.logging.Logger;

public class RegistrationPane {

    JTextField firstNameField, lastNameField, userNameField;
    JPasswordField passwordField, retypePasswordField;
    JLabel errorLabel;

    public RegistrationPane() {

        showPane(createMainPanel(), createButtons());
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
        JLabel usernameLabel=new JLabel("User name:", SwingConstants.LEFT);
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

        passwordField.addFocusListener(new FocusAdapter(){

            public void focusLost(FocusEvent e){
                if(passwordField.getPassword()!=null & !new PassWordVerifier().isCorrectPassword(passwordField.getPassword())){
                    errorLabel.setText("Error message!");

                }
            }
        });


        retypePasswordField=new JPasswordField();
        retypePasswordField.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent event){
                if(passwordField.getPassword()!=null && retypePasswordField.getPassword()!=null){
                    if (!Arrays.equals(passwordField.getPassword(), retypePasswordField.getPassword())) {
                        errorLabel.setText("Error message 1!");
                    }

                }
            }
        });

        textFieldsPanel.add(firstNameField);
        textFieldsPanel.add(lastNameField);
        textFieldsPanel.add(userNameField);
        textFieldsPanel.add(passwordField);
        textFieldsPanel.add(retypePasswordField);

        return textFieldsPanel;
    }

    private JButton[] createButtons(){

        JButton createAccountButton=new JButton("Create account");
        createAccountButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                String firstName=firstNameField.getText();
                String lastName=lastNameField.getText();
                String aUsername=userNameField.getText();
                char[] aPassword=passwordField.getPassword();
                Logger.getGlobal().info("Password: " + new String(aPassword));

                if(UserUtils.getUser(userNameField.getText())==null){
                    User newUser =new User(firstName, lastName, aUsername, aPassword);
                    UserUtils.writeUserToFile(newUser);
                    JOptionPane.getRootFrame().dispose();
                    LoginPane login = new LoginPane();
                }
                else{
                    errorLabel.setText("That username has already been taken!");
                    //JOptionPane.showMessageDialog(null, "That username is taken!", "Error!", JOptionPane.ERROR_MESSAGE);
                    RegistrationPane.this.userNameField.setText("");
                }
            }
        });

        JButton cancelButton=new JButton("Cancel");
        JButton[] buttons={createAccountButton, cancelButton};
        return buttons;
    }


    public void showPane(JPanel mainPanel, JButton[] buttons){

        JOptionPane.showOptionDialog(null, mainPanel, "Registration", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
    }
}
