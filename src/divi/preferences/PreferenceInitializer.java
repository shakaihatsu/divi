package divi.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import divi.Activator;
import divi.data.DiviConfiguration;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(DiviConfiguration.P_ANNOTATIONS, DiviConfiguration.createAnnotationPreference(DiviConfiguration.DEFAULT_INJECT_ANNOTATION_ARRAY));
    }

}
