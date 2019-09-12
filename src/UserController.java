import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class UserController {

    private static final File file=new File("/home/daan/Downloads/users.dat");
    //private static File file;

    //private static boolean isFirstWrite=true;
    private static boolean isFirstWrite;    // A file is supposed to have only one serialization stream header that is written
                                            // the first time an object is written to the file. When I want to update a User object,
                                            // I first read all User object from the file. I then write all User object back to the file
                                            // minus the one that was updated. It is a this point I need to make sure that when the
                                            // first User object is written a new serialization stream header is also written.

    public UserController(){
    }

    public static void writeUserToFile(User aUser) {

        try {
            FileOutputStream fileOut = new FileOutputStream(file,true);
            ObjectOutputStream objOut = null;
            Logger.getGlobal().info("isFirstWrite: " + isFirstWrite);
            try{
                if(isFirstWrite==false){

                    //Logger.getGlobal().info("AppendingObjectOutputStream");
                    objOut= new AppendingObjectOutputStream(fileOut);
                }
                else if(isFirstWrite==true){
                    //Logger.getGlobal().info("ObjectOutputStream");
                    objOut = new ObjectOutputStream(fileOut);
                    isFirstWrite=false;
                }

                objOut.writeObject(aUser);
                //Logger.getGlobal().info("A user was added");
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
    public static Set<User> readUsersFromFile(){

        //ArrayList<User> users =new ArrayList<>();
        Set<User> users=new HashSet<>();
        if(file.isFile() && file.length()>0) {

            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream objIn = new ObjectInputStream(fileIn)) {
                while (true) {
                    try {
                        Object obj = objIn.readObject();
                        users.add((User) obj);
                        //Logger.getGlobal().info("Read User object from file");
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
            //Logger.getGlobal().info("File does not exist!");
            try{

                //Logger.getGlobal().info("Created new file");
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
        Set<User> currentUsers= readUsersFromFile();

        // Delete file containing all the User objects.
        //File file=new File(path);
        file.delete();

        // Write all User objects to file except the one to be removed.
        /*for(int i=0;i<currentUsers.size();i++){
            if(i==0){
                isFirstWrite=true;
            }
            if(!currentUsers.get(i).getUsername().equals(aUser.getUsername())){
                writeUserToFile(currentUsers.get(i));
            }
        }*/
        isFirstWrite=true;
        for(User user : currentUsers){
            if(!user.getUsername().equals(aUser.getUsername())){
                writeUserToFile(user);
            }
        }

    }

    public static User getUser(String aUsername){

        Set<User> users= readUsersFromFile();
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
        return null; // Perhaps I should not return null, but throw an exception instead?
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
