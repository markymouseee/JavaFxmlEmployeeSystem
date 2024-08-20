package passwordhasher;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
    private static final int DESIRED_COST = 12;

    public static boolean password_needs_rehash(String hashPassword){
        int currentCost = getCost(hashPassword);

        return currentCost != DESIRED_COST;
    }

    private static int getCost(String hashedPassword){
        return Integer.parseInt(hashedPassword.substring(4, 6));
    }

    public static String password_hash(String password){
        String salt = BCrypt.gensalt(DESIRED_COST);

        return BCrypt.hashpw(password, salt);
    }

    public static boolean password_verify(String password, String storedHashed){
        return BCrypt.checkpw(password, storedHashed);
    }
}
