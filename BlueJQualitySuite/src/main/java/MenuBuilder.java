import bluej.extensions.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuBuilder extends MenuGenerator {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final Frame frame;
    private String javaFileName;
    private final Preferences preferences;
    private static int currentX;
    private static int currentY;
    private static final String SUITE_LIB_NAME = "BlueJQualitySuiteLibraries";
    private static final String ALL = "ALL";
    private static final String PMD = "PMD";
    private static final String PMD_DIRECTORY_NAME = "pmd-bin-5.8.1";
    private static final String CHECKSTYLE = "Checkstyle";
    private static final String CHECKSTYLE_JAR_NAME = "checkstyle-8.5-all.jar";

    public MenuBuilder(Preferences preferences) {
        this.frame = null;
        this.preferences = preferences;
    }

    public JMenuItem getClassMenuItem(BClass aClass) {
        return new JMenuItem(new MenuAction("Open Quality Suite Tools"));
    }

    public void notifyPostClassMenu(BClass bc, JMenuItem jmi) {
        try {
            javaFileName = bc.getJavaFile().getPath();
        } catch (ProjectNotOpenException | PackageNotFoundException e) {
            e.printStackTrace();
        }
    }

    class MenuAction extends AbstractAction {
        MenuAction(String menuName) {
            putValue(AbstractAction.NAME, menuName);
        }

        public void actionPerformed(ActionEvent anEvent) {
            // init starting position of first JFrame
            currentX = 0;
            currentY = 0;

            // validate java file
            if (javaFileName == null || javaFileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "No file selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            final String suiteLibPath = getSuiteLibPath();
            if (suiteLibPath == null) {
                return;
            }

            // build tools list to display
            String[] menuItemArray = {ALL, PMD, CHECKSTYLE};

            // user makes tool choice
            // reference: http://www.java2s.com/Code/Java/Swing-JFC/AnexampleofusingtheJOptionPanewithacustomlistofoptionsinan.htm
            // For maintainers, this function call requires you to give a parent compnent, message, title,
            // the message type, the icon is being used, the list being used, and the initial selection in that order.
            String toolChoice = (String) JOptionPane.showInputDialog(null,
                    "Which suite tool(s) would you like to use?",
                    "BlueJQS", JOptionPane.QUESTION_MESSAGE, null, menuItemArray, menuItemArray[0]);

            if (toolChoice.trim().equals(ALL)) {
                runPMD(suiteLibPath);
                runCheckstyle(suiteLibPath);

            } else if(toolChoice.trim().equals(PMD)){
                runPMD(suiteLibPath);

            } else if (toolChoice.trim().equals(CHECKSTYLE)) {
                runCheckstyle(suiteLibPath);

            }
        }
    }

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

    private void createJScrollPane(String title, String text) {
        JFrame frame;
        frame = new JFrame(title);
        currentX += 25;
        currentY += 25;
        frame.setLocation(currentX, currentY);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JTextArea textArea = new JTextArea(30,90);// TODO: 12/1/17 turn into list
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

    private void runPMD(String suiteLibPath) {
        String command;
        if (!SystemUtils.isWindows()) {// macOS & GNU/Linux
            command = suiteLibPath + "/" + PMD_DIRECTORY_NAME + "/bin/run.sh pmd "
                    + preferences.getPMDOptions() + " -d " + javaFileName;
        } else {
            command = suiteLibPath + "/" + PMD_DIRECTORY_NAME  + "\\bin\\pmd.bat "
                    + preferences.getPMDOptions() + " -d " + javaFileName;// TODO: 12/1/17 fix windows version
        }
        doMenuAction(PMD, command);
    }

    private void runCheckstyle(String suiteLibPath) {
        String command = "java -jar " + suiteLibPath + "/" + CHECKSTYLE_JAR_NAME +  " -c "
                + suiteLibPath +"/google_checks.xml " + javaFileName;
        doMenuAction(CHECKSTYLE, command);
    }
}