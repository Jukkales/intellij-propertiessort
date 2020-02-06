## intellij-propertysort

If you are working on a project where different IDEs are used, properties files will often cause merge conflicts because of different format and ordering.

This plugin sorts and can format the property files inside IDEA.

All properties will be sorted alphabetically and the intention can be grouped by keys or over all.

### Versions

- Version 0.2
  - Feature: Support for grouping based on underscore character </li>
  - Feature: Save dialog selection on project level</li>
  - Bugfix: keep control characters</li>
  - Bugfix: keep comments</li>
  - Bugfix: split groups without rearrange now working</li>
 
- Version 0.1
  - Quick and Dirty Initial Release.

### Build

Use Gradle to build the Plugin. Use the Task ``buildPlugin``. 

### How to use

Open a ```.properties``` file in IntelliJ, press ALT + INS and select ```Sort Properties...```