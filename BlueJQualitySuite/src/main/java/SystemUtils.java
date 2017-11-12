/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

import java.util.Locale;

public class SystemUtils {
    public static boolean isWindows() {
        String osname = System.getProperty("os.name");
        return osname.toLowerCase(Locale.ROOT).contains("win");
    }
}
