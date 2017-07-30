package de.juserv.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import de.juserv.model.SortSettings;

import javax.swing.*;

/**
 * A simple configuration dialog.
 *
 * @author Steffen Herrmann (Jukkales)
 * @since 29.07.2017
 */
public class SortDialog extends DialogWrapper {

    private final JPanel component;

    private final JCheckBox cbSpace;
    private final JCheckBox cbSplit;
    private final JCheckBox cbRearrangeAll;
    private final JCheckBox cbRearrangeGroup;

    public SortDialog(Project project) {
        super(project);

        cbSpace = new JCheckBox("Add space around delimiter", null, true);
        cbSplit = new JCheckBox("Split in Groups", null, true);
        cbRearrangeAll = new JCheckBox("Rearrange intention over all", null, false);
        cbRearrangeGroup = new JCheckBox("Rearrange intention in groups", null, true);

        setTitle("Sort Properties");
        JPanel panel = new JPanel();
        panel.setBounds(61, 11, 81, 140);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(cbSpace);
        panel.add(cbSplit);
        panel.add(cbRearrangeAll);
        panel.add(cbRearrangeGroup);

        component = LabeledComponent.create(panel, "Sort Options");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        return component;
    }

    public SortSettings getSettings() {
        SortSettings settings = new SortSettings();
        settings.setAddSpace(cbSpace.isSelected());
        settings.setRearrangeAll(cbRearrangeAll.isSelected());
        settings.setRearrangeGroup(cbRearrangeGroup.isSelected());
        settings.setSplitGroups(cbSplit.isSelected());
        return settings;
    }
}
