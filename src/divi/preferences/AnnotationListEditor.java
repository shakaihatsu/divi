package divi.preferences;

import java.util.Arrays;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

import divi.data.DiviConfiguration;

public class AnnotationListEditor extends ListEditor {

    public AnnotationListEditor(String name, String labelText, Composite parent) {
        super(name, labelText, parent);
    }

    @Override
    protected String createList(String[] arg0) {
        return DiviConfiguration.createAnnotationPreference(arg0);
    }

    @Override
    protected String getNewInputObject() {
        InputDialog inputDialog = new InputDialog(this.getShell(), "DIVi", "Add new annotation:", "", null);
        inputDialog.create();
        inputDialog.open();
        if (Arrays.asList(getList().getItems()).contains(inputDialog.getValue())) {
            return null;
        }
        return inputDialog.getValue();
    }

    @Override
    protected String[] parseString(String arg0) {
        return DiviConfiguration.parseAnnotationPreference(arg0);
    }
}
