import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImprovedGridCipherTest {

    @Test
    @DisplayName("encrypt method test")
    public void testEncryptDecryptBasicMessage() {
        String key = "SECRETKEY";
        String originalMessage = "HelloWorld";
        int rows = 3;
        int cols = 4;

        ImprovedGridCipher cipher = new ImprovedGridCipher(key, rows, cols);

        String encrypted = cipher.encrypt(originalMessage);

        assertNotEquals(originalMessage.toUpperCase(), encrypted);
        assertEquals(rows * cols, encrypted.length());

        String decrypted = cipher.decrypt(encrypted);
        assertEquals(originalMessage.toUpperCase(), decrypted);
    }

    @Test
    @DisplayName("decrypt method test")
    public void testEncryptDecryptWithSpecialCharsAndPadding() {
        String key = "CRYPTO123";
        String originalMessage = "This is a secret message! @#$";
        int rows = 4;
        int cols = 5;

        ImprovedGridCipher cipher = new ImprovedGridCipher(key, rows, cols);

        String encrypted = cipher.encrypt(originalMessage);

        assertTrue(encrypted.matches("[A-Z]+"));
        assertEquals(rows * cols, encrypted.length());

        String decrypted = cipher.decrypt(encrypted);
        String expected = "THISISASECRETMESSAGE";
        assertEquals(expected, decrypted);
    }
}