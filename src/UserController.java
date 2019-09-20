import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * This is a utility class.
 */
public class UserController {

    private static final File file=new File("/home/daan/Downloads/users.dat");
    private static boolean isFirstWrite;    // A file is supposed to have only one serialization stream header that is written
                                            // the first time an object is written to the file. When I want to update a User object,
                                            // I first read all User object from the file. I then write all User object back to the file
                                            // minus the one that was updated. It is a this point I need to make sure that when the
                                            // first User object is written a new serialization stream header is also written.

    /**
     * Constructor
     */
    private UserController(){ // Constructor has private access because UserController is a static utility class that should not be instantiated.
    }                         // If you specify no constructor a constructor a default constructor with a public access specifier will be create that
                              // calls the constructor of the Object class.

    /**
     * This method writes a User object to file.
     * @param aUser, a User object.
     */
    public static void writeUserToFile(User aUser) {

        try {
            FileOutputStream fileOut = new FileOutputStream(file,true);
            ObjectOutputStream objOut = null;
            try{
                if(!isFirstWrite){

                    objOut= new AppendingObjectOutputStream(fileOut);
                }
                else{
                    objOut = new ObjectOutputStream(fileOut);
                    isFirstWrite=false;
                }

                objOut.writeObject(aUser);
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
     * This method reads a User object from file.
     * @return a HashSet with User objects. I chose a HashSet because 1) I dont need to access elements in this collection by position,
     * 2) order of the elements does not matter 3) Finding elements must be as fast as possible.
     */

    private static Set<User> readUsersFromFile(){

        Set<User> users=new HashSet<>();
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

            Logger.getGlobal().info("File does not exist!");
            try{

                boolean isCreated=file.createNewFile();
                isFirstWrite=true;
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return users;
    }

    /**
     * This method removes a User object from a file (done as part of process that updates a User object).
     * @param aUser a User object.
     */
    public static void removeUserFromFile(User aUser){
        // Store all current User objects in a list.
        Set<User> currentUsers= readUsersFromFile();

        // Delete file containing all the User objects.
        boolean isDeleted=file.delete();

        // Write all User objects to file except the one to be removed.
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

    public static String getUsername(User aUser){
        return aUser.getUsername();
    }

    public static void addMessage(User aUser, Message aMessage){
        aUser.addMessage(aMessage);
    }

    public static void deleteMessage(User aUser, int index){
        aUser.getMessages().remove(index);
    }
}
