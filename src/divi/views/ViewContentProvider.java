package divi.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import divi.model.node.Node;

public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
    private DIViewer viewPart;

    public ViewContentProvider(DIViewer viewPart) {
        this.viewPart = viewPart;
    }

    @Override
    public Object[] getElements(Object parent) {
        Object[] result;

        if (parent.equals(viewPart.getViewSite())) {
            result = getChildren(viewPart.getRootNode());
        } else {
            result = getChildren(parent);
        }

        return result;
    }

    @Override
    public Object getParent(Object child) {
        if (child instanceof Node) {
            return ((Node) child).getParent();
        }
        return null;
    }

    @Override
    public Object[] getChildren(Object parent) {
        Object[] children;

        if (parent instanceof Node) {
            children = ((Node) parent).getChildren().toArray(new Node[0]);
        } else {
            children = new Object[0];
        }

        return children;
    }

    @Override
    public boolean hasChildren(Object parent) {
        boolean hasChildren;

        if (parent instanceof Node) {
            hasChildren = ((Node) parent).getChildren().size() > 0;
        } else {
            hasChildren = false;
        }

        return hasChildren;
    }

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        // Do nothing
    }

    @Override
    public void dispose() {
        // Do nothing
    }
}
