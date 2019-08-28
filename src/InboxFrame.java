import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InboxFrame extends JFrame{

    private List<Message> messages;
    User thisUser;
    JPanel mainPanel;

    public InboxFrame(User aUser) {
        thisUser = aUser;
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
        JLabel ownerLabel=new JLabel("Mailbox of: " + thisUser.getUsername(), SwingConstants.CENTER);
        return ownerLabel;
    }


    private JTextArea createMessageTextArea(){
        JTextArea messageTextArea=new JTextArea(20, 20);
        messageTextArea.setBorder(new BorderUIResource.EtchedBorderUIResource());
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        if(messages.size()>0){
            messageTextArea.append(messages.get(0).format());
        }
        return messageTextArea;
    }

    private JScrollPane createInboxArea(){
        JList messageList=new JList(messages.toArray());
        JScrollPane inboxArea=new JScrollPane(messageList);
        inboxArea.setPreferredSize(new Dimension(200, 200));
        return inboxArea;
    }

    private JPanel createControlPanel(){
        JPanel controlPanel=new JPanel();


        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton newMessageButton=new JButton("New Message");
        newMessageButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                new NewMessagePane(thisUser,InboxFrame.this);
            }
        });
        controlPanel.add(newMessageButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        controlPanel.add(createControlPanelButtons("Log out"));
        return controlPanel;
    }


    private JButton createControlPanelButtons(String text){
        JButton button=new JButton(text);
        button.setMaximumSize(new Dimension(125, 25));
        return button;

    }
}
