import java.util.Locale;

//BlueJQS - BlueJ Quality Tool Suite
//Copyright (C) 2017
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this library; if not, write to the Free Software
//Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

/**
 * Defines the OS being used.
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

public class SystemUtils {

    /**
     * Checks if the operating system the extension is being ran on is Windows.
     * @return true if the operating system being ran on is Windows
     */
    public static boolean isWindows() {
        String osname = System.getProperty("os.name");
        return osname.toLowerCase(Locale.ROOT).contains("win");
    }
}
