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

    /**
     * Checks if a token is valid, the TokenException this throws should be handled by the GlobalExceptionHandler.
     *
     * @param bearerToken
     * @param service
     * @return the id of the user, if the token is valid.
     * @throws AuthorizationException if the token is invalid
     */
    public static Integer validateToken(String bearerToken, IService service) throws AuthorizationException {
        if (bearerToken != null && bearerToken.startsWith("Bearer")) { //verify the token format
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);

            if (user.isPresent()) { //verify if the token is valid
                return user.get().getId();
            }
        }

        throw new AuthorizationException(Constants.AuthorizationExceptionCode.UNAUTHORIZED);
    }
}
