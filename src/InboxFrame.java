import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class provides an inbox GUI for the messaging system.
 */
public class InboxFrame extends JFrame{

    private User thisUser;
    private JTextArea messageTextArea;
    private JList<Message> messageJList;
    private int messageJListIndex;
    private DefaultListModel<Message> listModel;

    /**
     * Constructor.
     * @param aUser, a User object. The user that logged in.
     */
    public InboxFrame(User aUser) {
        thisUser = aUser;
        setContentPane(createMainPanel());
        pack();
        int delay=1000;
        // Listener checks if User object has been updated.
        ActionListener messageListener= event -> {
            User user = UserController.getUser(thisUser.getUsername());
            if (listModel.size() < user.getMessages().size()) { // If updated User object saved to file has more messages than this snapshot of user.
                thisUser=user; // thisUser variable now references updated User Object.
                listModel = new DefaultListModel<>();
                for (Message message : user.getMessages()) {
                    listModel.addElement(message);
                }
                messageJList.setModel(listModel);
            }
        };
        Timer inboxRefresher=new Timer(delay, messageListener);
        inboxRefresher.start();
    }

    /**
     * This method creates the main panel (content pane) for the inbox frame.
     * @return a JPanel object.
     */
    private JPanel createMainPanel(){
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(createMessageTextArea(), BorderLayout.EAST);
        mainPanel.add(createInboxArea(), BorderLayout.CENTER);
        mainPanel.add(createControlPanel(), BorderLayout.WEST);
        mainPanel.add(createOwnerLabel(), BorderLayout.NORTH);
        return mainPanel;
    }

    /**
     * This methods creates a label displayed at the top of the inbox frame.
     * @return a JLabel object.
     */
    private JLabel createOwnerLabel(){
        return new JLabel("Mailbox of: " + thisUser.getUsername(), SwingConstants.CENTER);
    }

    /**
     * This method creates an area that displays the full contents of a message
     * @return a JScrollPane object.
     */
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

    /**
     * This method create an area the displays a user's received messages (that have not been deleted).
     * @return a JScrollPane object.
     */
    private JScrollPane createInboxArea(){
        listModel=new DefaultListModel<>();
        for(Message message : thisUser.getMessages()){
            listModel.addElement(message);
        }
        messageJList=new JList<>(listModel);
        // Because I do not want JList to use the toString (apparently toString is for debugging) method of the Message class, I added a ListCellRenderer.
        messageJList.setCellRenderer(new ListCellRenderer<>() {
            final JLabel label = new JLabel();

            public Component getListCellRendererComponent(JList<? extends Message> jList, Message message, int index, boolean isSelected, boolean cellHasFocus) {
                label.setText("From: " + message.getSender() + " Subject: " + message.getSubject());

                if (isSelected) {
                    label.setBackground(jList.getSelectionBackground());
                    label.setForeground(jList.getSelectionForeground());
                } else {
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
        return inboxArea;
    }

    /**
     * This method creates a JPanel with buttons to create a new message, delete a message, and logout.
     * @return a JPanel object.
     */
    private JPanel createControlPanel(){

        class CustomButton extends JButton{

            private CustomButton(String text){
                super(text);
                Dimension size = new Dimension(125, 25);
                setPreferredSize(size);
                setMinimumSize(size);
                setMaximumSize(size);
            }
        }

        JPanel controlPanel=new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton newMessageButton=new CustomButton("New");
        newMessageButton.addActionListener(event -> new NewMessagePane(thisUser));

        controlPanel.add(newMessageButton);

        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton deleteMessageButton=new CustomButton("Delete");
        deleteMessageButton.addActionListener(event -> {
            if(messageJList.getModel().getSize()>0 && messageJListIndex<listModel.size() && messageJListIndex!=-1){ //JList getSelectedIndex() returns -1 if there is no selection.
                UserController.deleteMessage(thisUser, messageJListIndex);

                UserController.removeUserFromFile(thisUser); // update the database, remove old User object from file.
                UserController.writeUserToFile(thisUser); // write updated User object to file.

                listModel.remove(messageJListIndex);
            }

        });

        controlPanel.add(deleteMessageButton);

        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton logoutButton=new CustomButton("Logout");
        logoutButton.addActionListener(event -> {
             InboxFrame.this.dispose();
                LoginPane newLoginPane=new LoginPane();
        });
        controlPanel.add(logoutButton);

        return controlPanel;
    }
}
