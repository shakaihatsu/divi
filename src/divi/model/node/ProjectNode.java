package divi.model.node;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

public class ProjectNode extends ImageNode {
    @Override
    public Image getImage() {
        return PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJ_PROJECT);
    }
}
