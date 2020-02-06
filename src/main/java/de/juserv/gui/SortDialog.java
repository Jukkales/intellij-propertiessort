package de.juserv.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import de.juserv.model.SortSettings;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A simple configuration dialog.
 *
 * @author Jukka
 * @since 29.07.2017
 */
public class SortDialog extends DialogWrapper {

    private final JPanel component;

    public SortDialog(Project project, SortSettings settings) {
        super(project);

        JCheckBox cbSpace = new JCheckBox("Add space around delimiter", null, settings.isAddSpace());
        cbSpace.addItemListener(e -> settings.setAddSpace(cbSpace.isSelected()));
        JCheckBox cbSplit = new JCheckBox("Split in Groups", null, settings.isSplitGroups());
        cbSplit.addItemListener(e -> settings.setSplitGroups(cbSplit.isSelected()));
        JCheckBox cbRearrangeAll = new JCheckBox("Rearrange intention over all", null, settings.isRearrangeAll());
        cbRearrangeAll.addItemListener(e -> settings.setRearrangeAll(cbRearrangeAll.isSelected()));
        JCheckBox cbRearrangeGroup = new JCheckBox("Rearrange intention in groups", null, settings.isRearrangeGroup());
        cbRearrangeGroup.addItemListener(e -> settings.setRearrangeGroup(cbRearrangeGroup.isSelected()));

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

}
