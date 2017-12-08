import bluej.extensions.BlueJ;
import bluej.extensions.PreferenceGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
 *  Defines the preferences of the extensions being used,
 * like the location of the extension.
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

public class Preferences implements PreferenceGenerator {
    /** The panel to display to the user. */
    private JPanel panel;
    
    /** The field to put the path to PMD in. */
    private JTextField pmdPath;
    
    /** The field to change the option of PMD. */
    private JTextField pmdOptions;
    
    /** The instance of BlueJ being used. */
    private final BlueJ bluej;
    
    /** The string PMD.Path. */
    private static final String PROPERTY_PMD_PATH = "PMD.Path";
    
    /** The string PMD.Options. */
    private static final String PROPERTY_PMD_OPTIONS = "PMD.Options";
    
    /** The string of the default option of PMD. */
    private static final String PMD_OPTIONS_DEFAULT = "-format text -R java-basic,java-design -version 1.8 -language java";

    /** Constructor with the parameter for the instance of BlueJ being used.
     *  @param bluej the instance of BlueJ being used
     */
    public Preferences(BlueJ bluej) {
        this.bluej = bluej;
        renderPanel();
        loadValues(); // Load the default/initial values.
    }

    /**
     * Creates the preference panel in a dialog box.
     */
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
        panel.add(new JLabel(BlueJQualitySuite.SUITE_NAME + " Library Path:"), c);

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
                boolean valid = verifySuiteLibPath(selectedFile);
                if (valid) {
                    pmdPath.setText(selectedFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(panel, "The selected path " + selectedFile + " doesn't seem to be"
                            + " a PMD installation. E.g. the file bin/pmd.bat or bin/run.sh is missing.");
                }
            }
        });

        resetToDefaultButton.addActionListener(e -> pmdOptions.setText(PMD_OPTIONS_DEFAULT));
    }

    /**
     * Verifies the path for the suite is correct.
     * @param selectedFile the file that contains the suite
     * @return true if the file is correct
     */
    private boolean verifySuiteLibPath(File selectedFile) {
        File pathToExecutable;
        if (SystemUtils.isWindows()) {
            pathToExecutable = new File(selectedFile, "/pmd-bin-5.8.1/bin/pmd.bat");
        } else {
            pathToExecutable = new File(selectedFile, "/pmd-bin-5.8.1/bin/run.sh");
        }
        return pathToExecutable.exists();
    }

    /**
     * Gets the panel being used.
     * @return the panel being used for preferences
     */
    public JPanel getPanel ()  { return panel; }

    /**
     * Saves the PMD path and options to the extension.
     */
    public void saveValues () {
        bluej.setExtensionPropertyString(PROPERTY_PMD_PATH, pmdPath.getText());
        bluej.setExtensionPropertyString(PROPERTY_PMD_OPTIONS, pmdOptions.getText());
    }

    /**
     * Loads the path and options.
     */
    public final void loadValues () {
        pmdPath.setText(getSuiteLibPath());
        pmdOptions.setText(getPMDOptions());
    }

    /**
     * Gets the PMD options chosen.
     * @return the options of PMD
     */
    public final String getPMDOptions() {
        return bluej.getExtensionPropertyString(PROPERTY_PMD_OPTIONS, PMD_OPTIONS_DEFAULT);
    }

    /**
     * Gets the path to the suite.
     * @return the path to the suite
     */
    public final String getSuiteLibPath() {
        return bluej.getExtensionPropertyString(PROPERTY_PMD_PATH, "");
    }
}
