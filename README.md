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

1. Left-click on [divi] -> Export... -> Plug-in Development / Deployable plug-ins and fragments -> Next >
  Select Directory, enter the {path} where you want to export [divi] as a jar
2. Copy {path}/plugins/divi_{version}.jar to the plugins directory of your Eclipse installation
3. (Re)start Eclipse (might need to do it whith the -clean option)

How to use
----------

1. Select one or more projects
2. Right-click -> DIVi -> View Dependency Injection
3. DI Viewer view should appear with a tree, like this:

[project_1]
  |-(C) fully.qualified.name.of.Class ({nr of injected dependencies})
    |- @ fully.qualified.name.of.injected.Type : {variable name}
      |- (C) fully.qualified.name.of.Implementation1
      |- (C) fully.qualified.name.of.Implementation2
[project_2]
  ... similar to project_1

4. Double-click on "Class" or "injected.Type" to open up that selected type in the editor.

2. alternative) You can analyze your project (again or for the first time as well) by pressing the "i" button on the
  action bar of the DI Viewer, or from a context menu in the DI Viewer. Project(s) need to be selected beforehand in
  this case.
