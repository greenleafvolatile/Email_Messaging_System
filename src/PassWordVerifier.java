import java.util.logging.Logger;

public class PassWordVerifier {

    public boolean isCorrectPassword(char[] password){

        boolean hasUpperCase=false;
        boolean hasLowerCase=false;

        if(password.length!=4){
            Logger.getGlobal().info("Length: " + password.length);
            return false;
        }
        for(int i=0;i<password.length;i++){
            if(Character.isUpperCase(password[i])){
                hasUpperCase=true;
                Logger.getGlobal().info("Has upper case!");

            }
            else if(Character.isLowerCase(password[i])){
                hasLowerCase=true;
                Logger.getGlobal().info("Has lower case!");
            }
            if(hasUpperCase && hasLowerCase){
                return true;
            }

        }
        return false;
    }

}
