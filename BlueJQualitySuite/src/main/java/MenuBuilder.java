import bluej.extensions.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuBuilder extends MenuGenerator {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private Frame frame;
    private String javaFileName;
    private Preferences preferences;

    public MenuBuilder(Preferences preferences) {
        this.frame = null;
        this.preferences = preferences;
    }

    public JMenuItem getClassMenuItem(BClass aClass) {
        return new JMenuItem(new PMDAction("Check that code"));
    }

    public void notifyPostClassMenu(BClass bc, JMenuItem jmi) {
        try {
            javaFileName = bc.getJavaFile().getPath();
        } catch (ProjectNotOpenException | PackageNotFoundException e) {
            e.printStackTrace();
        }
    }

    class PMDAction extends AbstractAction {
        public PMDAction(String menuName) {
            putValue(AbstractAction.NAME, menuName);
        }

        public void actionPerformed(ActionEvent anEvent) {
            if (javaFileName == null || javaFileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No file selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pmdPath = preferences.getPMDPath();
            if (pmdPath == null || pmdPath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "The path to PMD Installation is not configured. "
                                + "Please select the path under \"Tools / Preferences / Extensions / PMD\".",
                        "No Path to PMD Installation", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                JOptionPane.showMessageDialog(frame, "Running PMD on selected Class (Click OK)");
                String mycommand = preferences.getPMDPath() + "/bin/run.sh pmd " + preferences.getPMDOptions() + " -d " + javaFileName;

                if (SystemUtils.isWindows()) {
                    mycommand = preferences.getPMDPath() + "\\bin\\pmd.bat " + preferences.getPMDOptions() + " -d " + javaFileName;
                }

                String output = runPMD(mycommand);

                JOptionPane.showMessageDialog(frame, "Class Checked");

                StringBuilder msg = new StringBuilder("Any problems found are displayed below:");
                msg.append(LINE_SEPARATOR);
                msg.append(output);
                JOptionPane.showMessageDialog(frame, msg);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Couldn't run PMD: " + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Couldn't run PMD: " + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(frame, "PMD run completed");
        }

        private String runPMD(String mycommand) throws IOException, InterruptedException {
            ProcessBuilder pb = new ProcessBuilder(mycommand.split(" +"));
            pb.redirectErrorStream(true);
            final Process p = pb.start();

            final StringBuilder preOutput = new StringBuilder();
            Thread reader = new Thread(() -> {
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s;
                try {
                    while ((s = stdInput.readLine()) != null ){
                        preOutput.append(s);
                        preOutput.append(LINE_SEPARATOR);
                    }
                } catch (IOException e) {
                    preOutput.append(e.toString());
                    e.printStackTrace();
                } finally {
                    try { stdInput.close(); } catch (IOException e) { /* quiet */ }
                }
            });
            reader.setDaemon(true);
            reader.start();
            p.waitFor();
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < preOutput.length(); i++) {
                if (i % 80 == 0) {
                    output.append("\n");
                }
                output.append(preOutput.charAt(i));
            }
            return output.toString();
        }
    }
}