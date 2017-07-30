package de.juserv.model;

import lombok.Data;

/**
 * Simple settings holder class.
 *
 * @author Steffen Herrmann (Jukkales)
 * @since 30.07.2017
 */
@Data
public class SortSettings {
    private boolean addSpace;
    private boolean splitGroups;
    private boolean rearrangeAll;
    private boolean rearrangeGroup;
}
