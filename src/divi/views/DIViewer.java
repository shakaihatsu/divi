package divi.views;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.CollapseAllHandler;
import org.eclipse.ui.handlers.ExpandAllHandler;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import divi.engine.AnalyzeProjectAction;
import divi.model.node.Node;
import divi.model.node.TypeNode;

public class DIViewer extends ViewPart {
    public static final String ID = "divi.views.DIViewer";

    private TreeViewer viewer;
    private DrillDownAdapter drillDownAdapter;

    private Action analyzeProject;
    private Action doubleClickAction;

    private Node rootNode;

    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ViewContentProvider(this));
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setSorter(new NameSorter());
        viewer.setInput(getViewSite());

        drillDownAdapter = new DrillDownAdapter(viewer);

        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
    }

    @Override
    public void setFocus() {

    }

    private void makeActions() {
        analyzeProject = new AnalyzeProjectAction();
        analyzeProject.setText("Analyze Project");
        analyzeProject.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

        doubleClickAction = new Action() {
            public void run() {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();

                if (obj instanceof TypeNode) {
                    TypeNode typeNode = (TypeNode) obj;
                    try {
                        JavaUI.openInEditor(typeNode.getTypeInfo().getJavaElement());
                    } catch (PartInitException | JavaModelException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void hookContextMenu() {
        MenuManager menuManager = new MenuManager("#PopupMenu");
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                DIViewer.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuManager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuManager, viewer);
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }

    private void contributeToActionBars() {
        IActionBars actionBars = getViewSite().getActionBars();
        fillLocalToolBar(actionBars.getToolBarManager());

        IHandlerService service = (IHandlerService) getSite().getService(IHandlerService.class);

        CollapseAllHandler collapseAllHandler = new CollapseAllHandler(viewer);
        ExpandAllHandler expandAllHandler = new ExpandAllHandler(viewer);

        service.activateHandler(CollapseAllHandler.COMMAND_ID, collapseAllHandler);
        service.activateHandler(ExpandAllHandler.COMMAND_ID, expandAllHandler);
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(analyzeProject);

        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(analyzeProject);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    public void refreshViewer() {
        viewer.refresh();
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    // public static void showMessage(String message) {
    // MessageDialog.openInformation(viewer.getControl().getShell(), "DI Viewer", message);
    // }
}