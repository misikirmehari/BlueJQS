import bluej.extensions.*;
import bluej.extensions.event.*;
import bluej.extensions.editor.*;

import java.net.URL;
import javax.swing.*;
import java.awt.event.*;

/*
 * This is the starting point of a BlueJ Extension
 */
public class PMDExecExt extends Extension implements PackageListener {
    /*
     * When this method is called, the extension may start its work.
     */
    public void startup (BlueJ bluej) {
        // Register a "preferences" panel generator
        Preferences myPreferences = new Preferences(bluej);
        bluej.setPreferenceGenerator(myPreferences);

        // Register a generator for menu items
        bluej.setMenuGenerator(new MenuBuilder(myPreferences));

        // Listen for BlueJ events at the "package" level
        bluej.addPackageListener(this);
    }

    /*
     * A package has been opened. Print the name of the project it is part of.
     * System.out is redirected to the BlueJ debug log file.
     * The location of this file is given in the Help/About BlueJ dialog box.
     */
    public void packageOpened ( PackageEvent ev ) {
        try {
            System.out.println ("Project " + ev.getPackage().getProject().getName() + " opened.");
        } catch (ExtensionException e) {
            System.out.println("Project closed by BlueJ");
        }
    }

    /*
     * A package is closing.
     */
    public void packageClosing ( PackageEvent ev ) {
    }

    /*
     * This method must decide if this Extension is compatible with the
     * current release of the BlueJ Extensions API
     */
    public boolean isCompatible () {
        return true;
    }

    /*
     * Returns the version number of this extension
     */
    public String  getVersion () {
        return ("PMD 5.8.1");
    }

    /*
     * Returns the user-visible name of this extension
     */
    public String  getName () {
        return ("PMD");
    }

    public void terminate() {
        System.out.println ("PMD terminates");
    }

    public String getDescription () {
        return ("Bluej extension for PMD");
    }
}

