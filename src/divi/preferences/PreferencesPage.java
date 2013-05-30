package divi.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import divi.Activator;
import divi.data.DiviConfiguration;

public class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public PreferencesPage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Dependency injection resolution settings");
    }

    public void createFieldEditors() {
        addField(new AnnotationListEditor(DiviConfiguration.P_ANNOTATIONS, "Annotations considered for dependency injection:", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }

}