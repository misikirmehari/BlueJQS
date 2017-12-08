import bluej.extensions.*;
import bluej.extensions.event.*;

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

/** Defines the extension of the suite that will allow users to use multiple BlueJ extensions
 * to improve the quality of their code.
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
public class BlueJQualitySuite extends Extension implements PackageListener {
    /** Project suite BlueJQS most current version. */
    private static final String VERSION = "1.1.0";
    
    /** Name of this project suite. */
    static final String SUITE_NAME = "BlueJQS";
    
    /**
     * Starts the BlueJQualitySuite.
     * @param bluej the instance of BlueJ being used
     */
    public void startup (BlueJ bluej) {
        
        // Register a "preferences" panel generator.
        Preferences myPreferences = new Preferences(bluej);
        bluej.setPreferenceGenerator(myPreferences);

        // Register a generator for menu items.
        bluej.setMenuGenerator(new MenuBuilder(myPreferences));

        // Listen for BlueJ events at the "package" level.
        bluej.addPackageListener(this);
    }

    /**
     * A package has been opened. Print the name of the project it is part of.
     * System.out is redirected to the BlueJ debug log file.
     * The location of this file is given in the Help/About BlueJ dialog box.
     * @param ev the package event that occurred, ie a package has been open
     */
    public void packageOpened (PackageEvent ev) {
        try {
            System.out.println ("Project " + ev.getPackage().getProject().getName() + " opened.");
        } catch (ExtensionException e) {
            System.out.println("Project closed by BlueJ");
        }
    }

    /**
     * A package is closing.
     * @param ev the package event that occurred, ie a package closing
     */
    public void packageClosing ( PackageEvent ev ) {
    }

    /**
     * This method must decide if this Extension is compatible with the
     * current release of the BlueJ Extensions API.
     * @return true if the extension is compatible with the BlueJ Extensions API
     */
    public boolean isCompatible () {
        return true;
    }

    /**
     * Returns the version number of this extension.
     * @return the version number of the extension
     */
    public String getVersion () {
        return (SUITE_NAME + " " + VERSION);
    }

    /**
     * Returns the user-visible name of this extension.
     * @return the name of the extension being used
     */
    public String  getName () {
        return (SUITE_NAME);
    }

    /**
     * Lets the user know the extension terminated.
     */
    public void terminate() {
        System.out.println (SUITE_NAME + " terminates");
    }

    /**
     * Get the description of the extension.
     * @return the description of the extension being used
     */
    public String getDescription () {
        return ("BlueJ extension for " + SUITE_NAME);
    }
}

