# intellij-propertiessort

If you are working on a project where different IDEs are used, properties files will often cause merge conflicts because of different format and ordering.

This plugin can sorts and format property files directly in IntelliJ.

All properties will be sorted alphabetically and the intention can be grouped by keys or over all.

### Build

Edit ```<lib.path>``` in ```pom.xml``` and let it point to your IntelliJ lib folder. 

To build the plugin jar simply call ```mvn package```

### How to use

Open a ```.properties``` file in IntelliJ and press ALT + INS ans select ```Sort Properties...```