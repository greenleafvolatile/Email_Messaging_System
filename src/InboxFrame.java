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

    private User thisUser;
    private JPanel mainPanel;
    private JTextArea messageTextArea;
    private JList<Message> messageJList;
    private int messageJListIndex;
    private DefaultListModel<Message> listModel;

    public InboxFrame(User aUser) {
        thisUser = aUser;
        setContentPane(createMainPanel());
        pack();
        int delay=1000;
        ActionListener messageListener=new ActionListener(){ // Listener checks if User object has been updated.

            public void actionPerformed(ActionEvent event) {
                //
                User user = UserController.getUser(thisUser.getUsername());
                if (listModel.size() < user.getMessages().size()) { // If updated User object saved to file has more messages than this snapshot of user.
                    thisUser=user; // thisUser variable now references updated User Object.
                    listModel = new DefaultListModel<>();
                    for (Message message : user.getMessages()) {
                        listModel.addElement(message);
                    }
                    messageJList.setModel(listModel);
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

    private JLabel createOwnerLabel(){
        return new JLabel("Mailbox of: " + thisUser.getUsername(), SwingConstants.CENTER);
    }


    private JScrollPane createMessageTextArea(){
        final int ROWS=20, COLUMNS=20;
        messageTextArea=new JTextArea(ROWS, COLUMNS);
        messageTextArea.setBorder(new EtchedBorder());
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        if(thisUser.getMessages().size()>0){
            messageTextArea.append(thisUser.getMessages().get(0).format());
        }

        return new JScrollPane(messageTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JScrollPane createInboxArea(){
        listModel=new DefaultListModel<>();
        for(Message message : thisUser.getMessages()){
            listModel.addElement(message);
        }
        messageJList=new JList<>(listModel);
        messageJList.setCellRenderer(new ListCellRenderer<Message>() {
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

        messageJList.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                messageJListIndex=messageJList.getSelectedIndex();
                Logger.getGlobal().info("Index: " + messageJListIndex);
                Logger.getGlobal().info("User: " + thisUser.getUsername());
               if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==2){
                    if(messageJListIndex>=0){
                        Message message=messageJList.getModel().getElementAt(messageJListIndex);
                        messageTextArea.setText(message.format());
                    }
                }
            }
        });
        JScrollPane inboxArea=new JScrollPane(messageJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
                if(messageJList.getModel().getSize()>0 && messageJListIndex<listModel.size() && messageJListIndex!=-1){ //JList getSelectedIndex() returns -1 if there is no selection.
                    Logger.getGlobal().info("Check!");




                    Logger.getGlobal().info("User before messages: " + thisUser.getNrOfMessages());
                    UserController.deleteMessage(thisUser, messageJListIndex);
                    //thisUser.removeMessage(messageJListIndex);

                    Logger.getGlobal().info("User after messages: " + thisUser.getNrOfMessages());
                    UserController.removeUserFromFile(thisUser);
                    UserController.writeUserToFile(thisUser);
                    listModel.remove(messageJListIndex);


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
