package service.productParser.parserutils;

public class URLSafety {
    private URLSafety(){}

    /**
     * Removes all characters that are not whitelisted
     * Currently whitelisted characters are alphanumeric, _ , . and -
     * @param keyword the string to be sanitized
     * @return a string containing only whitelisted characters
     */
    public static String sanitizeString(String keyword){
        String whitelist = "[^ a-zA-Z0-9_.-]";
        return keyword.replaceAll(whitelist, "");
    }

    /**
     * Checks if the URL is in the valid format for an altex product
     * @param url the url to be checked
     * @return true if the url format is valid, false otherwise
     */
    public static boolean isAltexValidURL(String url){
        String altexUrlRegex = "^(http)|(https)://altex.ro/[a-zA-Z0-9\\-]+/cpd/[a-zA-Z0-9]+$";

        return url.matches(altexUrlRegex);
    }
}
