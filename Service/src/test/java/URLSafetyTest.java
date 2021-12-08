import org.junit.jupiter.api.Test;
import service.productParser.parserutils.URLSafety;

class URLSafetyTest {
    @Test
    void testSanitizeString(){
        String dirtyString = "\"$_COOKIES'''''";

        String cleanString = URLSafety.sanitizeString(dirtyString);

        System.out.println(cleanString);

        assert(cleanString.equals("_COOKIES"));

        String normalString = "telefon mobil";
        assert(URLSafety.sanitizeString(normalString).equals(normalString));

    }

    @Test
    void testIsAltexValidURL(){
        String dirtyURL = "https://altex.ro/telefon-mobil-nokia-105-2019-4mb-ram-2g-negru;$@!/cpd/GSMNO105DS19BK";

        assert(!URLSafety.isAltexValidURL(dirtyURL));

        String cleanURL = "https://altex.ro/telefon-mobil-nokia-105-2019-4mb-ram-2g-negru/cpd/GSMNO105DS19BK";

        assert(URLSafety.isAltexValidURL(cleanURL));

        String maliciousURL = "http://hackerman.runtimeterror/book.html?default=<script>alert(document.cookie)</script>";

        assert(!URLSafety.isAltexValidURL(maliciousURL));
    }
}
