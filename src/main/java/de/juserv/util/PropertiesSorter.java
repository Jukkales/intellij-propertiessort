package de.juserv.util;

import de.juserv.model.SortSettings;
import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;

public class PropertiesSorter {

    /**
     * This is a variation of {@link org.apache.commons.text.StringEscapeUtils}.ESCAPE_JAVA without any unicode escapes.
     * While sorting, we just want to escape control sequences.
     * <p>
     * If unicode are escaped there will break IntelliJ's transparent native-to-ascii conversion.
     */
    private static final CharSequenceTranslator ESCAPE_JAVA_WITHOUT_UNICODE;

    public static StringBuilder sortPropertiesFile(String fileName, SortSettings settings) {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try (StringReader reader = new StringReader(fileName)) {
            config.read(reader);
        } catch (Exception e) {
            throw new RuntimeException("Error while loading properties.", e);
        }
        PropertiesConfigurationLayout layout = config.getLayout();

        Iterable<String> iterable = config::getKeys;
        List<String> keyList = IteratorUtils.toList(iterable.iterator());
        StringBuilder sb = new StringBuilder();
        final String[] last = {""};
        keyList.stream().sorted().forEachOrdered(key -> {
            String value = ESCAPE_JAVA_WITHOUT_UNICODE.translate(config.getString(key));
            String comment = layout.getComment(key);

            // Group Splitting?
            String current = key.split("[._]")[0];
            if (!"".equals(last[0]) && !last[0].equals(current) && settings.isSplitGroups()) {
                sb.append("\n");
            }

            // Add Comment
            if (comment != null) {
                sb.append(comment).append('\n');
            }

            // Check Arrangement
            if (settings.isRearrangeAll()) {
                String max = Collections.max(keyList, Comparator.comparing(String::length));
                sb.append(String.format("%-" + max.length() + "s", key));
            } else if (settings.isSplitGroups()) {
                List<String> group = keyList.stream().filter(s -> s.matches(current + "[^\\w].*")).collect(Collectors.toList());
                if (group.size() > 0 && settings.isRearrangeGroup()) {
                    String max = Collections.max(group, Comparator.comparing(String::length));
                    sb.append(String.format("%-" + max.length() + "s", key));
                } else {
                    sb.append(key);
                }
            } else {
                sb.append(key);
            }

            // Add spaces?
            if (settings.isAddSpace() && !StringUtils.isEmpty(value)) {
                sb.append(" = ");
            } else {
                sb.append("=");
            }

            sb.append(StringUtils.isEmpty(value) ? "" : value).append('\n');

            last[0] = current;
        });

        return sb;
    }

    static {
        final Map<CharSequence, CharSequence> escapeJavaMap = new HashMap<>();
        //escapeJavaMap.put("\"", "\\\"");
        escapeJavaMap.put("\\", "\\\\");
        ESCAPE_JAVA_WITHOUT_UNICODE = new AggregateTranslator(
                new LookupTranslator(Collections.unmodifiableMap(escapeJavaMap)),
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE));
    }

}
