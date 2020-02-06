package de.juserv.model;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;

/**
 * Simple settingsholder class.
 *
 * @author Jukka
 * @since 30.07.2017
 */
@Getter
@Setter
public class SortSettings {

    private static final String ADD_SPACE_ID = "de.juserv.model.SortSettings.addSpace";
    private static final String REARRANGE_ALL_ID = "de.juserv.model.SortSettings.rearrangeAll";
    private static final String REARRANGE_GROUPS_ID = "de.juserv.model.SortSettings.rearrangeGroup";
    private static final String SPLIT_GROUPS_ID = "de.juserv.model.SortSettings.splitGroups";

    private boolean addSpace;
    private boolean rearrangeAll;
    private boolean rearrangeGroup;
    private boolean splitGroups;

    public SortSettings(Project project) {
        PropertiesComponent component = PropertiesComponent.getInstance(project);

        addSpace = component.getBoolean(ADD_SPACE_ID, true);
        splitGroups = component.getBoolean(SPLIT_GROUPS_ID, true);
        rearrangeAll = component.getBoolean(REARRANGE_ALL_ID, false);
        rearrangeGroup = component.getBoolean(REARRANGE_GROUPS_ID, false);
    }

    public void updateSettings(Project project) {
        PropertiesComponent component = PropertiesComponent.getInstance(project);

        component.setValue(ADD_SPACE_ID, addSpace, true);
        component.setValue(SPLIT_GROUPS_ID, splitGroups, true);
        component.setValue(REARRANGE_ALL_ID, rearrangeAll, false);
        component.setValue(REARRANGE_GROUPS_ID, rearrangeGroup, false);
    }
}
