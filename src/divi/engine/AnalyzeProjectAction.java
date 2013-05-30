package divi.engine;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import divi.model.node.Node;
import divi.views.DIViewer;

public class AnalyzeProjectAction extends Action implements IObjectActionDelegate {

    @Override
    public void run() {
        run(null);
    }

    @Override
    public void run(IAction action) {
        try {
            IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DIViewer.ID);

            if (viewPart instanceof DIViewer) {
                DIViewer diViewer = (DIViewer) viewPart;
                Node rootNode = Engine.analyzeProject(getSelection());
                diViewer.setRootNode(rootNode);
                diViewer.refreshViewer();
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    private ISelection getSelection() {
        ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
        String projectExplorerID = "org.eclipse.ui.navigator.ProjectExplorer";
        ISelection selection = selectionService.getSelection(projectExplorerID);
        return selection;
    }

    @Override
    public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
        // Do nothing

    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {
        // Do nothing
    }
}
