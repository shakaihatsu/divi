DIVi: Dependency Injection Viewer plugin for Eclipse

Overview
--------

DIVi is a dependency injection analyzing tool. It can show in a tree view what classes have injected dependencies,
and what are the types of those dependencies. It also indicates implementations, in case of interfaces.

How to install
--------------

DIVi is a regular Eclipse plugin project. Once it has been imported, it can be exported as a Deployable plugin and added
to Eclipse.

To do this, you can:

1. Import DIVi into Eclipse as an existing project. (General, not Maven.)
2. Left-click on [divi] -> Export... -> Plug-in Development / Deployable plug-ins and fragments -> Next >
  Select Directory, enter the {path} where you want to export [divi] as a jar
3. Copy {path}/plugins/divi_{version}.jar to the plugins directory of your Eclipse installation
4. (Re)start Eclipse (might need to do it whith the -clean option)

How to use
----------

1. In __Project Explorer__ select one or more projects
2. Right-click -> DIVi -> View Dependency Injection
3. DI Viewer view should appear with a tree, like this:

![ATMStateMachine](http://shakaihatsu.github.io/divi/images/example.png)

4. Double-click on "Class" or "injected.Type" or "ImplementationN" to open it in the Eclipse editor.

5. You can (re)analyze your project (resolve dependency injections again) by pressing the "i" button on the action bar
  of the DI Viewer, or from a context menu in the DI Viewer. Project(s) need to be selected beforehand in this case.
