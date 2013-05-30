package divi.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import divi.model.TypeInfo;
import divi.model.node.Node;

// TODO Maybe shouldn't be static
public class Engine {
    public static Node analyzeProject(ISelection selection) {
        Node rootNode = null;

        final Map<String, Set<TypeInfo>> projectNameToClassTypeInfoSet = new HashMap<>();
        final Map<String, Set<TypeInfo>> interfaceNameToImplementationSet = new HashMap<>();

        try {
            Object selectedObject = selection;
            IStructuredSelection structuredSelection;

            if (selection instanceof IStructuredSelection) {
                structuredSelection = (IStructuredSelection) selection;

                Iterator<?> structuredSelectionIterator = structuredSelection.iterator();
                while (structuredSelectionIterator.hasNext()) {
                    selectedObject = structuredSelectionIterator.next();

                    // selectedObject = structuredSelection.getFirstElement();

                    processProject(selectedObject, projectNameToClassTypeInfoSet, interfaceNameToImplementationSet);
                }

                TreeBuilder treeBuilder = new TreeBuilderImpl();
                rootNode = treeBuilder.buildTree(projectNameToClassTypeInfoSet, interfaceNameToImplementationSet);
            }
        } catch (CoreException e) {
            // TODO Proper exception handling
            e.printStackTrace();
        }

        return rootNode;
    }

    private static void processProject(Object selectedObject, final Map<String, Set<TypeInfo>> projectNameToClassTypeInfoSet,
            final Map<String, Set<TypeInfo>> interfaceNameToImplementationSet) throws CoreException, JavaModelException {

        if (selectedObject instanceof IAdaptable) {
            IResource res = (IResource) ((IAdaptable) selectedObject).getAdapter(IResource.class);
            IProject project = res.getProject();

            if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
                processJavaProject(project, projectNameToClassTypeInfoSet, interfaceNameToImplementationSet);
            }
        }
    }

    private static void processJavaProject(IProject project, final Map<String, Set<TypeInfo>> projectNameToClassTypeInfoSet,
            final Map<String, Set<TypeInfo>> interfaceNameToImplementationSet) throws JavaModelException {

        IPackageFragment[] packageFragments = JavaCore.create(project).getPackageFragments();

        TypeInfoHierarchyBuilderImpl visitor = new TypeInfoHierarchyBuilderImpl(project.getName(), projectNameToClassTypeInfoSet,
            interfaceNameToImplementationSet);

        for (IPackageFragment packageFragment : packageFragments) {
            if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
                for (ICompilationUnit unit : packageFragment.getCompilationUnits()) {

                    CompilationUnit compilationUnit = parse(unit);
                    compilationUnit.accept(visitor);
                }
            }

        }
    }

    private static CompilationUnit parse(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null);
    }
}
