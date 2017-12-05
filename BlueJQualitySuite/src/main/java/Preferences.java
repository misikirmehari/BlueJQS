import bluej.extensions.BlueJ;
import bluej.extensions.PreferenceGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Preferences implements PreferenceGenerator {

    private JPanel panel;
    private JTextField pmdPath;
    private JTextField pmdOptions;
    private final BlueJ bluej;
    private static final String PROPERTY_PMD_PATH = "PMD.Path";
    private static final String PROPERTY_PMD_OPTIONS = "PMD.Options";
    private static final String PMD_OPTIONS_DEFAULT = "-format text -R java-basic,java-design -version 1.8 -language java";

    public Preferences(BlueJ bluej) {
        this.bluej = bluej;
        renderPanel();
        loadValues(); // Load the default/initial values
    }

    private void renderPanel() {
        panel = new JPanel();
        pmdPath = new JTextField();
        pmdOptions = new JTextField();

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("BlueJQS Library Path:"), c);

        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pmdPath,c );

        JButton selectPmdPathButton = new JButton("Select");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(selectPmdPathButton, c);

        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("PMD Options:"), c);

        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pmdOptions, c);

        JButton resetToDefaultButton = new JButton("Reset to default");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(resetToDefaultButton, c);
        selectPmdPathButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setCurrentDirectory(new File(pmdPath.getText()));
            int result = fileChooser.showDialog(panel, "Select");
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("selectedFile.getPath()-> " + selectedFile.getPath());// TODO: 12/1/17 remove me
                System.out.println("selectedFile.getAbsolutePath()-> " + selectedFile.getAbsolutePath());// TODO: 12/1/17 remove me
                boolean valid = verifySuiteLibPath(selectedFile);
                if (valid) {
                    pmdPath.setText(selectedFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(panel, "The selected path " + selectedFile + " doesn't seem to be"
                            + " a PMD installation. E.g. the file bin/pmd.bat or bin/run.sh is missing.");// TODO: 12/3/17 cleanup- make generic
                }
            }
        });

        resetToDefaultButton.addActionListener(e -> pmdOptions.setText(PMD_OPTIONS_DEFAULT));
    }

    private boolean verifySuiteLibPath(File selectedFile) {
        File pathToExecutable;
        if (SystemUtils.isWindows()) {
            pathToExecutable = new File(selectedFile, "/pmd-bin-5.8.1/bin/pmd.bat");
        } else {
            System.out.println("selectedFile.getPath()-> "+ selectedFile.getPath());
            System.out.println("pmdPath.getText()-> "+ pmdPath.getText());
            pathToExecutable = new File(selectedFile, "/pmd-bin-5.8.1/bin/run.sh");
        }
        return pathToExecutable.exists();
    }

    public JPanel getPanel ()  { return panel; }

    public void saveValues () {
        bluej.setExtensionPropertyString(PROPERTY_PMD_PATH, pmdPath.getText());
        bluej.setExtensionPropertyString(PROPERTY_PMD_OPTIONS, pmdOptions.getText());
    }

    public final void loadValues () {
        pmdPath.setText(getSuiteLibPath());
        pmdOptions.setText(getPMDOptions());
    }

    public final String getPMDOptions() {
        return bluej.getExtensionPropertyString(PROPERTY_PMD_OPTIONS, PMD_OPTIONS_DEFAULT);
    }

    public final String getSuiteLibPath() {
        return bluej.getExtensionPropertyString(PROPERTY_PMD_PATH, "");
    }
}