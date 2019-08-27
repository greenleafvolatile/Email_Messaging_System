import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InboxFrame extends JFrame{

    private List<Message> messages;
    User user;
    JPanel mainPanel;

    public InboxFrame(User aUser) {
        user = aUser;
        messages = aUser.getMessage();
        setContentPane(createMainPanel());
        pack();
    }

    private JPanel createMainPanel(){
        mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(createMessageTextArea(), BorderLayout.EAST);
        mainPanel.add(createInboxArea(), BorderLayout.CENTER);
        mainPanel.add(createControlPanel(), BorderLayout.WEST);
        mainPanel.add(createOwnerLabel(), BorderLayout.NORTH);
        return mainPanel;
    }

    public JLabel createOwnerLabel(){
        JLabel ownerLabel=new JLabel("Mailbox of: " + user.getUsername(), SwingConstants.CENTER);
        return ownerLabel;
    }


    private JTextArea createMessageTextArea(){
        JTextArea messageTextArea=new JTextArea(20, 20);
        messageTextArea.append(messages.get(0).format());
        return messageTextArea;
    }

    private JScrollPane createInboxArea(){
        JList messageList=new JList(messages.toArray());
        //JList messageList=new JList(messages.toArray());
        JScrollPane inboxArea=new JScrollPane(messageList);
        inboxArea.setPreferredSize(new Dimension(200, 200));
        return inboxArea;
    }

    private JPanel createControlPanel(){
        JPanel controlPanel=new JPanel();

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlPanel.add(createControlPanelButton("New Message"));

        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        controlPanel.add(createControlPanelButton("Log out"));
        return controlPanel;

    }


    private JButton createControlPanelButton(String text){
        JButton button=new JButton(text);
        button.setMaximumSize(new Dimension(125, 25));
        return button;

    }

    /*public static void createAndShowGUI(){
        JFrame frame=new InboxFrame();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                InboxFrame.createAndShowGUI();
            }
        });
    }*/
}
