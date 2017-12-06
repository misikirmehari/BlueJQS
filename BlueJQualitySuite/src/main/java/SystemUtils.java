/* Defines the OS being used.
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 *
 * @author Erin Gurnett
 * @author Haley Ittner
 * @author Hunter Hobbs
 * @author Maxwell Stark
 * @author Misikir Mehari
 * @author Vicky Lym
 *
 * @version 1.1.0
 */

import java.util.Locale;

public class SystemUtils {

    /**
     * Checks if the operating system the extension is being ran on is Windows.
     * @return boolean Returns true if the operating system being ran on is Windows
     */
    public static boolean isWindows() {
        String osname = System.getProperty("os.name");
        return osname.toLowerCase(Locale.ROOT).contains("win");
    }
}
