import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class UserController {

    public static final String path="/home/daan/Downloads/users.dat";

    public UserController(){
    }

    public static void writeUserToFile(User aUser) {

        File file=new File(path);
        boolean exists=file.exists();
        try {
            FileOutputStream fileOut = new FileOutputStream(path,true);
            ObjectOutputStream objOut = null;
            try{
                if(exists){
                    objOut= new AppendingObjectOutputStream(fileOut);
                }
                else {
                    objOut = new ObjectOutputStream(fileOut);
                }

                objOut.writeObject(aUser);
                Logger.getGlobal().info("A User object was written to file!");
            }
            finally{
                objOut.close();
                fileOut.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static ArrayList<User> readUsersFromFile(){

        ArrayList<User> users =new ArrayList<>();

        try(FileInputStream fileIn=new FileInputStream(path);
            ObjectInputStream objIn=new ObjectInputStream(fileIn)) {
            while(true) {
                try{
                    Object obj = objIn.readObject();
                    users.add((User)obj);
                    Logger.getGlobal().info("A User object was added to the list!");
                }
                catch(EOFException eof){
                    Logger.getGlobal().info("An EOF exception occurred!");
                    break;
                }
            }
        }
        catch(Exception e){

            e.printStackTrace();
            Logger.getGlobal().info("Failed to read User object from disk!");
           return null; // Temporary solution for case when first user is created and file does not exist.

        }
        return users;
    }

    public static void removeUserFromFile(User aUser){
        // Store all current User objects in a list.
        ArrayList<User> currentUsers= readUsersFromFile();

        // Delete file containing all the User objects.
        File file=new File(path);
        file.delete();

        // Write all User objects to file except the one to be removed.
        for(int i=0;i<currentUsers.size();i++){
            if(!currentUsers.get(i).getUsername().equals(aUser.getUsername())){
                writeUserToFile(currentUsers.get(i));
            }
        }

    }

    public static User getUser(String aUsername){

        ArrayList<User> users= readUsersFromFile();
        User user=null;
        for(User aUser : users){
            if(aUser.getUsername().equals(aUsername)){
                Logger.getGlobal().info("Found user!");
                user=aUser;
                break;
            }
        }
        return user;
    }

    public static String getUsername(User aUser){
        return aUser.getUsername();
    }

    public static void addMessage(User aUser, Message aMessage){
        aUser.addMessage(aMessage);
    }

}
