import bluej.extensions.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Defines the menu for the BlueJQualitySuite.
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

public class MenuBuilder extends MenuGenerator {
    /** The line separator character. */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    /** The Frame of the menu being used. */
    private final Frame frame;
    
    /** The name of the java file to use the suite on. */
    private String javaFileName;
    
    /** The preferences of the extensions being used inside the suite. */
    private final Preferences preferences;
    
    /** The size of the frame in the horizontal plane. */
    private static int currentX;
    
    /** The size of the frame in the vertical plane. */
    private static int currentY;
    
    /** The name of the suite library. */
    private static final String SUITE_LIB_NAME = "BlueJQualitySuiteLibraries";
    
    /** The string all. */
    private static final String ALL = "ALL";
    
    /** The string PMD. */
    private static final String PMD = "PMD";
    
    /** The string of the PMD directory. */
    private static final String PMD_DIRECTORY_NAME = "pmd-bin-5.8.1";
    
    /** The string CheckStyle. */
    private static final String CHECKSTYLE = "Checkstyle";
    
    /** The string of the checkstyle jar name. */
    private static final String CHECKSTYLE_JAR_NAME = "checkstyle-8.5-all.jar";

    /**
     * Builds the menu from the preferences given.
     * @param preferences the preferences for the extension being used
     */
    public MenuBuilder(Preferences preferences) {
        this.frame = null;
        this.preferences = preferences;
    }

    /**
     * Gets menu item of a given class.
     * @param aClass the class of the item to put on the menu
     * @return the menu item to put in the suite menu
     */
    public JMenuItem getClassMenuItem(BClass aClass) {
        return new JMenuItem(new MenuAction("Open Quality Suite Tools"));
    }

    /**
     * Called when a class file in BlueJ is clicked. Gets the java file of the
     * project the extension is running on.
     * @param bc the class of the project
     * @param jmi the menu item being used
     */
    public void notifyPostClassMenu(BClass bc, JMenuItem jmi) {
        try {
            javaFileName = bc.getJavaFile().getPath();
        } catch (ProjectNotOpenException | PackageNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Defines the action to take when a menu item is selected.
     */
    class MenuAction extends AbstractAction {
        // Give the menu a name.
        MenuAction(String menuName) {
            putValue(AbstractAction.NAME, menuName);
        }

        /**
         * Starts the chosen extension.
         * @param anEvent the event that occurred to start the extension
         */
        public void actionPerformed(ActionEvent anEvent) {
            // Init starting position of first JFrame.
            currentX = 0;
            currentY = 0;

            // Validate java file.
            if (javaFileName == null || javaFileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "No file selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            final String suiteLibPath = getSuiteLibPath();
            if (suiteLibPath == null) {
                return;
            }

            // Build tools list to display.
            String[] menuItemArray = {ALL, PMD, CHECKSTYLE};

            // User makes tool choice.
            // Reference: http://www.java2s.com/Code/Java/Swing-JFC/AnexampleofusingtheJOptionPanewithacustomlistofoptionsinan.htm
            // For maintainers, this function call requires you to give a parent component, message, title,
            // the message type, the icon is being used, the list being used, and the initial selection in that order.
            String toolChoice = (String) JOptionPane.showInputDialog(null,
                    "Which suite tool(s) would you like to use?",
                    "BlueJQS", JOptionPane.QUESTION_MESSAGE, null, menuItemArray, menuItemArray[0]);

            switch (toolChoice.trim()) {
                case ALL:
                    runPMD(suiteLibPath);
                    runCheckstyle(suiteLibPath);

                    break;
                case PMD:
                    runPMD(suiteLibPath);

                    break;
                case CHECKSTYLE:
                    runCheckstyle(suiteLibPath);

                    break;
            }
        }
    }

    /**
     * Starts the chosen extension with the given command and prints the results.
     * @param menuItemName the name of the extension chosen
     * @param command the command to perform
     */
    private void doMenuAction(String menuItemName, String command) {
        try {
            String output = runCommand(command).replaceAll("\\.\n\\[", ". [")
                    .replaceAll("\n", "\n\n");

            String msg = "Any problems found are displayed below:\n" + LINE_SEPARATOR + output;
            createJScrollPane(menuItemName + " Results", msg);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Couldn't run " + menuItemName
                    + ": " + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Starts the extension with the given command.
     * @param myCommand the command given that starts the process
     * @return the output of the process started
     * @throws IOException If an input exception occurs
     * @throws InterruptedException If process has been interrupted
     */
    private String runCommand(String myCommand) throws IOException, InterruptedException {
        System.out.println("Running command: '" + myCommand + "'");
        ProcessBuilder pb = new ProcessBuilder(myCommand.split(" +"));
        pb.redirectErrorStream(true);
        final Process p = pb.start();

        final StringBuilder output = new StringBuilder();
        Thread reader = new Thread(() -> {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            try {
                while ((s = stdInput.readLine()) != null ){
                    output.append(s);
                    output.append(LINE_SEPARATOR);
                }
            } catch (IOException e) {
                output.append(e.toString());
                e.printStackTrace();
            } finally {
                try { stdInput.close(); } catch (IOException e) { /* quiet */ }
            }
        });
        reader.setDaemon(true);
        reader.start();
        p.waitFor();
        return output.toString().replaceAll("\\. ", ".\n");
    }

    /**
     * Gets the suite's library path.
     * @return the path to the suite's library
     */
    private String getSuiteLibPath() {
        String suiteLibPath = preferences.getSuiteLibPath();
        if (suiteLibPath == null || suiteLibPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "The path to suite lib that contains '" + PMD_DIRECTORY_NAME
                            + "', '" + CHECKSTYLE_JAR_NAME + "' \nPlease select the path to the "
                            + SUITE_LIB_NAME + " Directory.",
                    "No Path to " + SUITE_LIB_NAME + " folder", JOptionPane.ERROR_MESSAGE);
            suiteLibPath = null;
        }
        return suiteLibPath;
    }

    /**
     * Creates the scroll panel inside the dialog pop-up.
     * @param title the title of the extension ran
     * @param text the text generated from the extension
     */
    private void createJScrollPane(String title, String text) {
        JFrame frame;
        frame = new JFrame(title);
        currentX += 25;
        currentY += 25;
        frame.setLocation(currentX, currentY);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JTextArea textArea = new JTextArea(30,90);
        textArea.setLayout(new BorderLayout());
        textArea.setFont(new Font("Serif", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(text);
        textArea.setVisible(true);

        JScrollPane jScrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(jScrollPane, BorderLayout.CENTER);

        frame.setLayout(new FlowLayout());
        frame.pack();
    }

    /**
     * Runs the PMD extension
     * @param suiteLibPath the path to the suite library
     */
    private void runPMD(String suiteLibPath) {
        String command;
        if (!SystemUtils.isWindows()) {// macOS & GNU/Linux
            command = suiteLibPath + "/" + PMD_DIRECTORY_NAME + "/bin/run.sh pmd "
                    + preferences.getPMDOptions() + " -d " + javaFileName;
        } else {
            command = suiteLibPath + "\\" + PMD_DIRECTORY_NAME  + "\\bin\\pmd.bat "
                    + preferences.getPMDOptions() + " -d " + javaFileName;
        }
        doMenuAction(PMD, command);
    }

    /**
     * Runs the Checkstyle extension.
     * @param suiteLibPath the path to the suite library.
     */
    private void runCheckstyle(String suiteLibPath) {
        String command = "java -jar " + suiteLibPath + "/" + CHECKSTYLE_JAR_NAME +  " -c "
                + suiteLibPath +"/google_checks.xml " + javaFileName;
        doMenuAction(CHECKSTYLE, command);
    }
}
