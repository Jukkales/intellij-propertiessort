package de.juserv;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.util.ThrowableRunnable;
import de.juserv.gui.SortDialog;
import de.juserv.model.SortSettings;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Plugin main action.
 * It's placed under "Generate".
 * Not the best but it is "generating" a better properties ... and better for quick access :)
 *
 * @author Steffen Herrmann (Jukkales)
 * @since 29.07.2017
 */
public class SortPropertiesAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        SortDialog dialog = new SortDialog(e.getProject());
        dialog.show();
        if (dialog.isOK()) {
            try {
                String fileText = e.getData(DataKeys.FILE_TEXT);
                Editor editor = e.getData(DataKeys.EDITOR);

                // Its not null here
                assert fileText != null;
                assert editor != null;

                // Convert all
                StringBuilder sb = sortProperties(fileText, dialog.getSettings());

                if (sb != null) {
                    // Allright lets go
                    WriteAction.run((ThrowableRunnable<Throwable>) () -> {
                        final Document document = editor.getDocument();
                        CommandProcessor.getInstance().executeCommand(editor.getProject(),
                                () -> document.replaceString(0, document.getTextLength(), sb.toString()),
                                "Property Sorter",
                                null,
                                UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);
                    });
                }
            } catch (Throwable ex) {
                Messages.showErrorDialog(e.getProject(), "Error while sorting peroperties. " + ex.getLocalizedMessage(), "Property Sorter");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Sort and format the properties file by the given sort settings.
     *
     * @param text     The properties contents
     * @param settings The {@link SortSettings} generated by the {@link SortDialog}
     * @return A StringBuilder with the sorted properties contents.
     * @throws IOException If something wents wrong.
     */
    private StringBuilder sortProperties(String text, SortSettings settings) throws IOException {

        StringBuilder sb = new StringBuilder();

        Properties p = new Properties();
        p.load(new StringReader(text));

        // Sort keys
        List<String> sorted = p.stringPropertyNames().stream().sorted(String::compareTo).collect(Collectors.toList());

        String last = "";
        for (String key : sorted) {
            String current = key.split("\\.")[0];
            if (!"".equals(last) && !last.equals(current) && settings.isSplitGroups()) {
                sb.append("\n");
            }

            if (settings.isRearrangeAll()) {
                String max = Collections.max(sorted, Comparator.comparing(String::length));
                sb.append(String.format("%-" + max.length() + "s", key));
            } else if (settings.isSplitGroups()) {
                List<String> group = sorted.stream().filter(s -> s.matches(current + "[^\\w].*")).collect(Collectors.toList());
                if (group.size() > 0) {
                    String max = Collections.max(group, Comparator.comparing(String::length));
                    sb.append(String.format("%-" + max.length() + "s", key));
                } else {
                    sb.append(key);
                }
            } else {
                sb.append(key);
            }

            if (settings.isAddSpace()) {
                sb.append(" = ");
            } else {
                sb.append("=");
            }
            sb.append(p.getProperty(key)).append("\n");
            last = current;
        }

        return sb;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(isPropertiesContext(e));
    }

    /**
     * Checks if the active {@link PsiFile} is a properties file.
     *
     * @param e The IntelliJ ActionEvent.
     * @return true if its a properties file.
     */
    private boolean isPropertiesContext(AnActionEvent e) {
        PsiFile file = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        return file != null && editor != null && "Properties".equals(file.getLanguage().getID());
    }

}
