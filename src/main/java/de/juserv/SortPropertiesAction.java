package de.juserv;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
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

import static de.juserv.util.PropertiesSorter.sortPropertiesFile;

/**
 * Plugin main action.
 * It's placed under "Generate".
 * Not the best but it is "generating" a better properties ... and better for quick access :)
 *
 * @author Jukka
 * @since 29.07.2017
 */
public class SortPropertiesAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        SortSettings settings = new SortSettings(e.getProject());
        SortDialog dialog = new SortDialog(e.getProject(), settings);
        dialog.show();
        settings.updateSettings(e.getProject());
        if (dialog.isOK()) {
            try {
                Editor editor = e.getData(DataKeys.EDITOR);
                if (editor != null) {
                    StringBuilder sb = sortPropertiesFile(editor.getDocument().getText(), settings);

                    WriteAction.run((ThrowableRunnable<Throwable>) () -> {
                        final Document document = editor.getDocument();
                        CommandProcessor.getInstance().executeCommand(editor.getProject(),
                                () -> document.replaceString(0, document.getTextLength(), sb.toString()),
                                "Property Sorter", null, UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);
                    });
                } else {
                    throw new RuntimeException("No editor!");
                }
            } catch (Throwable ex) {
                Messages.showErrorDialog(e.getProject(), "Error while sorting properties. " + ex.getLocalizedMessage(),
                        "Property Sorter");
                ex.printStackTrace();
            }
        }
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

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(isPropertiesContext(e));
    }

}
