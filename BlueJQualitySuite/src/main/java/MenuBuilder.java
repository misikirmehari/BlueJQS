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

    public MenuBuilder(Preferences preferences) {
        this.frame = null;
        this.preferences = preferences;
    }

    public JMenuItem getToolsMenuItem(BPackage bp) {
        return new JMenuItem(new SpotbugsAction("Open Spotbugs"));
    }

    public void notifyPostToolsMenu(BPackage bp, JMenuItem jmi) {
        System.out.println("Opening Spotbugs GUI...");
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

    class SpotbugsAction extends AbstractAction {
        public SpotbugsAction(String menuName) {
            putValue(AbstractAction.NAME, menuName);
        }

        public void actionPerformed(ActionEvent anEvent) {

            try {
                JOptionPane.showMessageDialog(frame, "Opening Spotbugs...");
                String myCommand = "java -jar /Users/maxwell/spotbugs-3.1.0/lib/spotbugs.jar temp.jar";
                String output = runCommand(myCommand);

                StringBuilder msg = new StringBuilder();
                msg.append(LINE_SEPARATOR);
                msg.append(output);
                JOptionPane.showMessageDialog(frame, msg);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Couldn't run Spotbugs: " + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(frame, "Spotbugs run completed");
        }


    }

    class MenuAction extends AbstractAction {
        public MenuAction(String menuName) {
            putValue(AbstractAction.NAME, menuName);
        }

        public void actionPerformed(ActionEvent anEvent) {
            // validate java file
            if (javaFileName == null || javaFileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "No file selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // build tools list to display
            String[] menuItemArray = {"PMD"};
            StringBuilder menuItems = new StringBuilder();
            for (int i = 0; i < menuItemArray.length; i++) {
                String row = "(" + (i+1) + ") " + menuItemArray[i] + "\n";
                menuItems.append(row);
            }

            // user makes tool choice
            String toolChoice = JOptionPane.showInputDialog("Choose a tool from 1 to "
                    + menuItemArray.length + ":\n" + menuItems.toString());

            if (toolChoice.trim().equals("1")) {// PMD
                String pmdPath = preferences.getPMDPath();
                if (pmdPath == null || pmdPath.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "The path to PMD Installation is not configured. "
                                    + "Please select the path under \"Tools / Preferences / Extensions / PMD\".",
                            "No Path to PMD Installation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String command;
                if (!SystemUtils.isWindows()) {// macOS & GNU/Linux
                    command = preferences.getPMDPath() + "/bin/run.sh pmd "
                            + preferences.getPMDOptions() + " -d " + javaFileName;
                } else {
                    command = preferences.getPMDPath() + "\\bin\\pmd.bat "
                            + preferences.getPMDOptions() + " -d " + javaFileName;
                }
                doMenuAction("PMD", command);

            } else if (toolChoice.trim().equalsIgnoreCase("2")) {// Infer
//                doMenuAction("Infer", "infer capture -- javac " + javaFileName);

            } else {// Invalid choice, returns
                JOptionPane.showMessageDialog(null,
                        "Pick a number to indicate your choice.");
                return;
            }
        }
    }

    private void doMenuAction(String menuItemName, String command) {
        try {
            JOptionPane.showMessageDialog(frame, "Running " + menuItemName
                    + " on selected Class (Click OK)");

            String output = runCommand(command);

            JOptionPane.showMessageDialog(frame, "Class Checked");


            StringBuilder msg = new StringBuilder("Any problems found are displayed below:");
            msg.append(LINE_SEPARATOR);
            msg.append(output);
            JOptionPane.showMessageDialog(frame, msg);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Couldn't run " + menuItemName
                    + ": " + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(frame, menuItemName + " run completed");
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
}