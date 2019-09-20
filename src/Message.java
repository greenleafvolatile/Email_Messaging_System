import java.io.Serializable;

public class Message implements Serializable {

    private String sender;
    private String recipient;
    private String text;
    private String subject;

    /**
     * Constructor
     * @param sender the sender of the message.
     * @param recipient the intended recipient of the message.
     * @param subject the subject of the message.
     * @param text the actual message text (body of the message).
     */
    public Message(String sender, String recipient, String subject, String text){
        this.sender=sender;
        this.recipient=recipient;
        this.text=text;
        this.subject=subject;
    }

    public String toString(){
        return "Sender: " + sender + " Recipient: " + recipient + " Text: " + text + " title: " + subject;
    }

    public String getSender(){
        return sender;
    }

    public String getSubject(){
        return subject;
    }

    /**
     * This methods formats a Message object in such a way that it is optimally displayed in the messageTextArea of the InboxFrame class.
     * @return a string.
     */
    public String format(){
        return new StringBuffer("Sender: ").append(sender+"\n").append("Title: ").append(subject+"\n").append(text).toString();
    }

}
