import java.util.*;
import java.io.Serializable;
import java.util.logging.Logger;

public class User implements Serializable {

    private String firstName, lastName, username;
    private char[] password;
    private List<Message> messages;

    public User(String firstName, String lastName, String aUsername, char[] aPassword){
        messages=new ArrayList<>();
        username=aUsername;
        password=aPassword;
    }

    public String getUsername(){
        return username;
    }

    public char[] getPassword(){
        return password;
    }

    public void addMessage(Message message){

        messages.add(message);
    }

    public List<Message> getMessages(){
        return messages;
    }

    public String toString(){
        return new StringBuffer("Username: ").append(username).append(" Password: ").append(password).toString();
    }

    public int getNrOfMessages(){
        return messages.size();
    }

    public void removeMessage(int index){
        messages.remove(index);
    }

    @Override
    public int hashCode(){
        final int HASH_MULTIPLIER=31;
        int result=17; // should always return a prime;
        result=HASH_MULTIPLIER * result + (firstName==null? 0 : firstName.hashCode());
        result=HASH_MULTIPLIER * result + (lastName==null? 0 : lastName.hashCode());
        result=HASH_MULTIPLIER * result + (username==null? 0 : username.hashCode());
        result=HASH_MULTIPLIER * result + (password==null? 0 : Arrays.hashCode(password));
        result=HASH_MULTIPLIER * result + (messages==null? 0 : messages.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o){
        if(o==this){
            return true;
        }

        if(!(o instanceof User)){
            return false;
        }

        User comparate=(User) o;

        return this.firstName.compareTo(comparate.firstName)==0 && this.lastName.compareTo(comparate.lastName)==0 && this.username.compareTo(comparate.username)==0 && Arrays.equals(this.password, comparate.password) && this.messages.size()==comparate.messages.size();


    }
}
