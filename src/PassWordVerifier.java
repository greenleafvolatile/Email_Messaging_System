/**
 * This is a utility class with one function: to verify that a password meets a certain standard.
 * I kept it simple because this project is not about passwords but for me to practice object oriented programming.
 * Hence, a password has to only consist of 2-4 characters and contain one uppercase and one lowercase character.
 */
public class PassWordVerifier {

    public static boolean isCorrectPassword(char[] aPassword){

        String password=new String(aPassword);
        boolean hasUpperCase=!password.equals(password.toLowerCase());
        boolean hasLowerCase=!password.equals(password.toUpperCase());
        if(hasUpperCase && hasLowerCase && password.length()>=2 && password.length()<=4){
            return true;
        }
        return false;
    }
}
