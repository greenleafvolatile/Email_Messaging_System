import java.io.Serializable;

public class Message implements Serializable {

    private String sender;
    private String recipient;
    private String text;
    private String subject;

    public Message(String sender, String recipient, String subject, String text){
        this.sender=sender;
        this.recipient=recipient;
        this.text=text;
        this.subject=subject;
    }

    // This toString method does not display all the data in this object. This is because I want only the sender and the subject displayed in a JList.
    // JList calls toString. I realize this is not the proper use of the toString method. Creating a JList with custom behavior using ListCellRenderer is beyond
    // the scope of this project.
    public String toString(){
        return new StringBuffer("Sender: ").append(sender).append(" title: ").append(subject).toString();
    }

    public String getSender(){
        return sender;
    }

    public String getSubject(){
        return subject;
    }

    public String format(){
        return new StringBuffer("Sender: ").append(sender+"\n").append("Title: ").append(subject+"\n").append(text).toString();
    }

}
