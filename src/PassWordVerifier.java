
public class PassWordVerifier {

    public static boolean isCorrectPassword(char[] aPassword){

        String password=new String(aPassword);
        boolean hasUpperCase=!password.equals(password.toLowerCase());
        boolean hasLowerCase=!password.equals(password.toUpperCase());
        if(hasUpperCase && hasLowerCase && password.length()>=4 && password.length()<=8){
            return true;
        }
        return false;
    }
}
