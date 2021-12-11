package utils;

import domain.User;
import service.IService;

import java.util.Optional;

public class Utils {
    /**
     * Converts a hex string to a byte array
     * @throws IllegalArgumentException if hex string is invalid
     */
    public static byte[] hexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if(len % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string");
        }

        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int first_digit = Character.digit(s.charAt(i), 16),
                second_digit = Character.digit(s.charAt(i + 1), 16);

            if(first_digit == -1 || second_digit == -1) {
                throw new IllegalArgumentException("Invalid hex string");
            }

            data[i / 2] = (byte) ((first_digit << 4) + second_digit);
        }
        return data;
    }

    public static Integer validateToken(String bearerToken, IService service) throws Exception {

        if (bearerToken != null && bearerToken.startsWith("Bearer")) { //verify the token format
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);
            if (user.isPresent()) { //verify if there is an user associated with the token
                return user.get().getId();
            }
            else {
                throw  new Exception("Unauthorized");
            }
        }
        else{
            throw new Exception("Forbidden");
        }
    }
}
