package divi.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import divi.model.node.DependencyNode;
import divi.model.node.ImageNode;
import divi.model.node.Node;

public class ViewLabelProvider extends LabelProvider {

    @Override
    public String getText(Object obj) {
        String text;

        if (obj instanceof Node) {
            if (obj instanceof DependencyNode) {
                DependencyNode dependencyNode = (DependencyNode) obj;
                text = dependencyNode.getName() + " : " + dependencyNode.getVariableName();
            } else {
                text = ((Node) obj).getName();
            }
        } else {
            text = obj.toString();
        }

        return text;
    }

    @Override
    public Image getImage(Object obj) {
        Image image;

        if (obj instanceof ImageNode) {
            image = ((ImageNode) obj).getImage();
        } else {
            image = null;
        }

        return image;
    }
}
