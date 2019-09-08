import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InboxFrame extends JFrame{

    private List<Message> messages;
    private User thisUser;
    private JPanel mainPanel;
    private JTextArea messageTextArea;
    private JList<Message> messageList;
    private int messageListIndex;
    private DefaultListModel<Message> listModel;

    public InboxFrame(User aUser) {
        thisUser = aUser;
        messages = UserController.getMessages(aUser);
        Logger.getGlobal().info("Messages length: " + messages.size());
        setContentPane(createMainPanel());
        pack();
        int delay=1000;
        ActionListener messageListener=new ActionListener(){

            public void actionPerformed(ActionEvent event){
                User user=UserController.getUser(thisUser.getUsername());
                if(messages.size()<user.getMessages().size()){
                    messages=user.getMessages();
                    listModel = new DefaultListModel<>();
                    for(Message message : messages){
                        listModel.addElement(message);
                    }
                    messageList.setModel(listModel);
                }
            }
        };
        Timer inboxRefresher=new Timer(delay, messageListener);
        inboxRefresher.start();
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


    private JScrollPane createMessageTextArea(){
        messageTextArea=new JTextArea(20, 20);
        messageTextArea.setBorder(new EtchedBorder());
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        if(messages.size()>0){
            messageTextArea.append(messages.get(0).format());
        }
        JScrollPane scrollPane=new JScrollPane(messageTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JScrollPane createInboxArea(){
        listModel=new DefaultListModel<>();
        for(Message message : messages){
            listModel.addElement(message);
        }
        messageList=new JList<>(listModel);
        messageList.setCellRenderer(new ListCellRenderer<Message>() {
            JLabel label=new JLabel();
            @Override
            public Component getListCellRendererComponent(JList<? extends Message> jList, Message message, int index, boolean isSelected, boolean cellHasFocus) {
                label.setText("From: " + message.getSender() + " Subject: " + message.getSubject());

                if(isSelected){
                    label.setBackground(jList.getSelectionBackground());
                    label.setForeground(jList.getSelectionForeground());
                }
                else{
                    label.setBackground(jList.getBackground());
                    label.setForeground(jList.getForeground());
                }

                label.setFont(jList.getFont());
                label.setOpaque(true);
                return label;
            }
        });

        messageList.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                messageListIndex=messageList.getSelectedIndex();
                Logger.getGlobal().info("Message list index: " + messageListIndex);
               if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==2){
                    if(messageListIndex>=0){
                        Message message=messageList.getModel().getElementAt(messageListIndex);
                        messageTextArea.setText(message.format());
                    }
                }
            }
        });
        JScrollPane inboxArea=new JScrollPane(messageList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inboxArea.setViewportBorder(new EtchedBorder());
        inboxArea.setPreferredSize(new Dimension(200, 200));
        return inboxArea;
    }

    private JPanel createControlPanel(){

        class CustomButton extends JButton{

            private Dimension size=new Dimension(125, 25);

            public CustomButton(String text){
                super(text);
                setPreferredSize(size);
                setMinimumSize(size);
                setMaximumSize(size);
            }
        }

        JPanel controlPanel=new JPanel();


        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton newMessageButton=new CustomButton("New");

        newMessageButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                new NewMessagePane(thisUser);
            }
        });
        controlPanel.add(newMessageButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton deleteMessageButton=new CustomButton("Delete");
        deleteMessageButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                if(messageList.getModel().getSize()>0 && messageListIndex<listModel.size()){
                    Logger.getGlobal().info("Check!");


                    messages.remove(messageListIndex);
                    listModel.remove(messageListIndex);
                    UserController.removeUserFromFile(thisUser);
                    UserController.writeUserToFile(thisUser);


                    Logger.getGlobal().info("List size: " + messageList.getModel().getSize());
                    Logger.getGlobal().info("Messages length: " + messages.size());
                }

            }
        });
        controlPanel.add(deleteMessageButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton logoutButton=new CustomButton("Logout");
        logoutButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                InboxFrame.this.dispose();
                LoginPane newLoginPane=new LoginPane();
            }
        });
        controlPanel.add(logoutButton);

        return controlPanel;
    }


}
