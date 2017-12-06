import bluej.extensions.*;
import bluej.extensions.event.*;

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

    private static final String VERSION = "1.1.0";
    static final String SUITE_NAME = "BlueJQS";
    /**
     * Starts the BlueJQualitySuite.
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

    /**
     * A package has been opened. Print the name of the project it is part of.
     * System.out is redirected to the BlueJ debug log file.
     * The location of this file is given in the Help/About BlueJ dialog box.
     * @param ev The package event that occurred, ie a package has been open
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
     * @param ev The package event that occurred, ie a package closing
     */
    public void packageClosing ( PackageEvent ev ) {
    }

    /**
     * This method must decide if this Extension is compatible with the
     * current release of the BlueJ Extensions API
     * @return boolean If the extension is compatible with the BlueJ Extensions API
     */
    public boolean isCompatible () {
        return true;
    }

    /**
     * Returns the version number of this extension
     * @return String the version number of the extension
     */
    public String  getVersion () {
        return (SUITE_NAME + " " + VERSION);
    }

    /**
     * Returns the user-visible name of this extension
     * @return String The name of the extension being used
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
     * @return String The description of the extension being used
     */
    public String getDescription () {
        return ("BlueJ extension for " + SUITE_NAME);
    }
}

