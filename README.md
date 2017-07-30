# intellij-propertiessort

If you are working on a project where different IDEs are used, properties files will often cause merge conflicts because of different format and ordering.

This plugin can sorts and format property files directly in IntelliJ.

All properties will be sorted alphabetically and the intention can be grouped by keys or over all.

### Versions

- Version 0.1
  - Quick and Dirty Initial Release.


### Build

Edit ```<lib.path>``` in ```pom.xml``` and let it point to your IntelliJ lib folder. 

To build the plugin jar simply call ```mvn package```

If you want to open the project in IntelliJ you have to do it as PluginModule. The MavenModule wont work correctly.
The ```.iml``` file i use is committed. 

### How to use

Open a ```.properties``` file in IntelliJ, press ALT + INS and select ```Sort Properties...```