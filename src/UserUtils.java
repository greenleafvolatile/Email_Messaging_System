import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class UserUtils  {

    public static final String path="/home/daan/Downloads/users.dat";

    public UserUtils(){
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



    public static ArrayList<User> readUserFromFile(){

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

    public static User getUser(String aUsername){

        ArrayList<User> users=readUserFromFile();
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
}
