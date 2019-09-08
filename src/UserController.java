import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserController {

    private static final File file=new File("/home/daan/Downloads/users.dat");
    //private static File file;

    private static boolean isFirstWrite=true;

    public UserController(){
    }

    public static void writeUserToFile(User aUser) {

        try {
            FileOutputStream fileOut = new FileOutputStream(file,true);
            ObjectOutputStream objOut = null;
            Logger.getGlobal().info("isFirstWrite: " + isFirstWrite);
            try{
                if(isFirstWrite==false){

                    Logger.getGlobal().info("AppendingObjectOutputStream");
                    objOut= new AppendingObjectOutputStream(fileOut);
                }
                else if(isFirstWrite==true){
                    Logger.getGlobal().info("ObjectOutputStream");
                    objOut = new ObjectOutputStream(fileOut);
                    isFirstWrite=false;
                }

                objOut.writeObject(aUser);
                Logger.getGlobal().info("A user was added");
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


    /**
     *
     * @throws EOFException when user data file is empty and when there are no more users to be read.
     * @return
     */
    public static ArrayList<User> readUsersFromFile(){

        ArrayList<User> users =new ArrayList<>();

        if(file.isFile() && file.length()>0) {

            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream objIn = new ObjectInputStream(fileIn)) {
                while (true) {
                    try {
                        Object obj = objIn.readObject();
                        users.add((User) obj);
                    } catch (EOFException eof) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!file.isFile())
            {
            Logger.getGlobal().info("Files does not exist!");
            try{

                Logger.getGlobal().info("Created new file");
                boolean isCreated=file.createNewFile();
                isFirstWrite=true;
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return users;
    }

    public static void removeUserFromFile(User aUser){
        // Store all current User objects in a list.
        ArrayList<User> currentUsers= readUsersFromFile();

        // Delete file containing all the User objects.
        //File file=new File(path);
        file.delete();

        // Write all User objects to file except the one to be removed.
        for(int i=0;i<currentUsers.size();i++){
            if(i==0){
                isFirstWrite=true;
            }
            if(!currentUsers.get(i).getUsername().equals(aUser.getUsername())){
                writeUserToFile(currentUsers.get(i));
            }
        }

    }

    public static User getUser(String aUsername){

        ArrayList<User> users= readUsersFromFile();
        User user=null;
        if(users.size()>0){
            for(User aUser : users){
                if(aUser.getUsername().equals(aUsername)){
                    user=aUser;
                    break;
                }
            }
            return user;
        }
        return null;
    }

    public static int getNumberOfRegisteredUsers(){
        ArrayList<User> users=readUsersFromFile();
        return users.size();
    }

    public boolean isFirstWrite(){
        return isFirstWrite;
    }

    public static String getUsername(User aUser){
        return aUser.getUsername();
    }

    public static void addMessage(User aUser, Message aMessage){
        aUser.addMessage(aMessage);
    }

    public static void deleteMessage(User aUser, int index){
        aUser.getMessages().remove(index);
    }

    public static List<Message> getMessages(User aUser){
        return aUser.getMessages();
    }

}
