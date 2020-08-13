# Loopy
## Summary
This app is a PC adaption of the popular game Infinity Loop ([Android](https://play.google.com/store/apps/details?id=com.balysv.loop&hl=en), [Apple](https://apps.apple.com/us/app/infinity-loop/id977028266)). The game is about connecting tiles by turning them until there are no more lose ends on the board. The game supports import and export of levels, randomized levels, an algorithm to solve any valid level automatically and a JavaFX GUI.

## Configuration
The app was created for practice reasons only and is not tied to a build tool. To run the GUI from your IDE you need to have installed [JavaFX](https://openjfx.io/) and add JavaFX as a library to the project in your IDE. Depending on your IDE you might have to configure VM-options. 
#### Example for IntelliJ
- Go to: Run -> Edit Configurations... and select GuiView on the left hand side
- In the textbox for VM-options put: --module-path [PATH TO JAVAFX lib FOLDER] --add-modules javafx.controls,javafx.fxml
- Change the path according to you JavaFX installation
#### Run application
To start the GUI run GuiView in the package src.jpp.infinityloop.gui
