# Mathdoku

## COMP1206: Programming III

---

## Contents

- **[Introduction](#introduction)**
  * **[Task Description](#task-description)**
- **[Running the Application](#running-the-application)**
- **[Usage](#usage)**
  * **[Starting a Game](#starting-a-game)**
  * **[Grid Completion/Clearing](#grid-completion-clearing)**
    + **[Via Keyboard](#via-keyboard)**
    + **[Via Mouse](#via-mouse)**
  * **[Undo/Redo](#undo-redo)**
  * **[Mistake Detection](#mistake-detection)**
  * **[Win Detection](#win-detection)**
  * **[Grid Solver](#grid-solver)**
    + **[Solving Puzzles](#solving-puzzles)**
    + **[Receiving Hints](#receiving-hints)**
  * **[Appearance](#appearance)**
    + **[Font Sizes](#font-sizes)**
- **[Compilation Guide](#compilation-guide)**
  * **[Compilation](#compilation)**
  * **[Running](#running)**
  * **[Building](#building)**

---

## Introduction

### Task Description

- Develop a graphical application that allows the user to play the game of **Mathdoku** using **JavaFX**.

<video alt="Introduction" src="https://user-images.githubusercontent.com/60888912/132039160-f06449ca-638c-4e69-b768-c51e54efc8b5.mp4" width="550"></video>

---

## Running the Application

- There are several ways to run the application:

  - **1** - Run the `Mathdoku.jar` file contained in `build` (i.e., double-click it).

  - **2** - Enter the following command into the command line when in the `build` directory:

    - ```bash
      java -jar Mathdoku.jar
      ```

  - **3** - Use the following series of commands to compile the source code and run the application (requires JavaFX to be installed on the local machine).

    - ```bash
      export PATH_TO_FX= <Insert path to JavaFX here> 
      ```

    - ```bash
      javac src --source-path src --module-path $PATH_TO_FX --add-modules javafx.controls --class-path out -d out *.java
      ```

    - ````bash
      java --module-path $PATH_TO_FX --add-modules javafx.controls -cp out Mathdoku
      ````

- The application should look like this at open:

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132037719-020e9675-28af-41bc-b129-8397362e427b.png" alt="distributed_file_storage_system" width="450"/></p> 

---

## Usage

- The following is a guide on how to use the application and all of it's features.

### Starting a Game

- Press the `SETTINGS` button (this will be the only button enabled when first starting the application). This will open a new dialog window.
- Navigate to the `New Game` tab of the Settings menu.
- Within this tab, there are three different options to select depending on the desired loading method:
  - **Option 1**: Load a random game. You will need to select a grid size and difficulty in order to do this.
    - The application is configured to produce grids that have only a single, unique solution.
  - **Option 2**: Load a game from a text file. The `Select File` button will open a file chooser dialog, where the desired configuration file can be found.
- **Option 3**: Load a game from text input. When selecting this option, a text area will be enabled, and the configuration for the game can be entered into this area.

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132037768-c21b51b8-0867-412c-87c8-2663545bd27e.png" alt="distributed_file_storage_system" width="450"/></p> 

- When the desired loading method has been chosen, and the configuration entered/selected, the `Apply Changes` button in the bottom corner of the dialog can be used to apply these changes and load the new game.
- If there was an error loading the configuration, no game will be loaded, and an error dialog will be displayed on the screen detailing the reason.

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132037817-f8b2aea5-b30a-4340-b8cc-86b84c558e55.png" alt="distributed_file_storage_system" width="450"/></p> 

### Grid Completion/Clearing

- The cells of the generated/loaded Mathdoku grid can be entered and cleared via keyboard and/or mouse.
- At any time, the **currently selected cell** will be shown in red.

#### Via Keyboard

- Select the cell you wish to edit with the mouse (*If a cell is already selected, the arrow keys can be used to navigate the grid*).
- **Entering Values**:
  - Press the key for the number you wish to insert into the cell using the keyboard.
  - The application will only allow for numbers that are between one and the dimension of the grid to be entered via the keyboard.
    - E.g. If the grid has a dimension of 4, only keys 1,2,3 and 4 can be used to enter numbers into the grid.
- **Clearing Values**:
  - Use the backspace key on the currently selected cell to clear this individual cell. 
  - If the currently selected cell has no value, no change will occur.

#### Via Mouse

- Select the cell you wish to edit with the mouse.
  - If a cell is already selected, the arrow keys can be used to navigate the grid.
  - The currently selected cell will be highlighted red.
- **Entering Values:**
  - The number pad will show an array of buttons labelled with numbers. The number of buttons will depend on the dimension of the grid (e.g. if the grid has a dimension of 4, there will be four buttons, labelled with numbers 1 to 4).
  - Press the corresponding button for the value you wish to enter into the currently selected cell.
- **Clearing Values**:
  - If the currently selected cell contains a value (and thus can be cleared), the `Clear Cell` button within the toolbar will be enabled.
    - When pressed, this button will clear the currently selected cell.
  - If at least one cell contains a value, the `Clear Grid` button will be enabled. 
    - When pressed this button will clear the values from all cells in the grid.
    - You will first be greeted with a confirmation dialog before this action is carried out. 

### Undo/Redo

- The toolbar contains `UNDO` and `REDO` buttons.
- These buttons can be used to undo/redo the entering and clearing of values into individual cells, as well as undo/redo the clearing of the entire grid.
- The buttons will be enabled and disabled accordingly when they can be used. As such, both buttons will be disabled when a game is first loaded.
  - After entering a cell into the grid, the undo button will become enabled, and when pressed, the redo button will become enabled, and the undo disabled, and so on.

### Mistake Detection

- The **toolbar** contains a `Show Mistakes` button that can be used to show the incorrect numbers within the Mathdoku grid.
- When the button is first pressed upon starting the program, a dialog will be displayed explaining how the button functions, and how the different mistakes are shown to the user.
- When this dialog is closed, **holding the button** will now reveal the mistakes on the grid to the user:
  - If there is a mistake in a **column** within the grid, the column containing the error will be highlighted **purple**.
  - If there is a mistake in a **row** within the gird, the row containing the mistake will be highlighted **yellow**.
  - If there is a mistake within a **cage** within the grid, the cells of that cage will be highlighted **red**.

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132037891-4850dea4-daa6-4d03-8491-27cd8c1d37f3.png" alt="distributed_file_storage_system" width="350"/> <img src="https://user-images.githubusercontent.com/60888912/132042464-bb1877c7-5d54-4718-8fe1-681301ae0d81.png" alt="distributed_file_storage_system" width="350"/> </p>

### Win Detection

- Every time a value is entered into the grid, the program checks if the grid is now complete.
- If the grid is now complete after entering a value (all cells have a value no rules are broken), then the following occurs:
  - An animation is played, in which the colour of the cells turns to green starting from the top left cell, and finishing with the bottom right cell.
  - A dialog is then displayed on the screen, detailing that the grid is now complete, and containing animated ‘CONGRATULATIONS’ text, which cycles in size.
- This animation can quickly be demonstrated by making a new random game of size 2 and by completing the grid.

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132037969-caad5e1c-c2c9-47d7-9f01-62ad9c6f1ff0.png" alt="distributed_file_storage_system" width="450"/></p> 

### Grid Solver

#### Solving Puzzles

- The toolbar contains a `SOLVE` button.
- When pressed, a dialog is shown asking for you to confirm that you want to see the solution(s) to the grid.
- If you confirm this action, a dialog window will be opened that contains the solution(s).
  - Each solution is contained within its own tab.
  - Each solution consists of a copy of the grid, with the values inserted into the cell in a red font. 
- If the current grid has no solutions, an error message is displayed detailing that the grid has no solutions, and so nothing can be shown, instead of a confirmation dialog.

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132038027-e1faddbb-1ea7-45b2-838f-3b476565a765.png" alt="distributed_file_storage_system" width="450"/></p> 

#### Receiving Hints

- The toolbar contains a `SHOW HINT` button.
- For each game that is loaded, the application will allow for 3 hints to be shown.
- When the button is pressed, a dialog is displayed asking for you to confirm that you would like to see a hint to the puzzle and informing you of how many hints you have remaining.
- If this action is confirmed, a random cell is chosen which does not have the solution value (i.e. no value or incorrect value), and the solution value is inserted into the cell.
- If the grid has more than one solution, the first solution that was found by the ‘solver’ is used to reveal the hints.
- If all of the hints have been used, an information dialog is shown detailing that all hints have been used instead of a confirmation box.
- If the grid is close to completion (less than 3 cells left to be completed), then the ‘Show Hint’ button will display a dialog detailing that no hint can be shown as the grid is too close to completion instead of a confirmation dialog.

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132038104-5cf56e4e-cad3-4b4e-bcb6-49bfc21a5145.png" alt="show hint" width="450" />

### Appearance

- The application allows for some customizations of appearence.
- The application will also respond to **resizing** by adjust the size of the grid to fit the window.

#### Font Sizes

- To change the font size of the application:
  - Open the Settings window by pressing the `SETTINGS` button within the toolbar.
  - Navigate to the `Appearance` tab of the settings window.
  - There are three radio buttons which can be used to select one of three font sizes; `Small`, `Medium` and `Large`.
  - Select the desired font size and press the `Apply Changes` button in the bottom right of the settings window.
  - This will now change the font size of the text within the cells (target and value).

<p align="center"><img src="https://user-images.githubusercontent.com/60888912/132038168-13e94530-60d8-4e58-855c-03f5a44982da.png" alt="distributed_file_storage_system" width="450"/></p> 

---

## Compilation Guide

- The provided `pom.xml` file can be used with `Maven` to compile, run and build the application.

### Compilation

- Use the following command to **compile** the application:

```bash
mvn clean compile
```

### Running 

- Use the following command to **run** the application:

```bash
mvn javafx:run
```

### Building

- Use the following command to **build** (package) the application:

```bash
mvn clean package
```

- The produced executable `mathdoku-1.0.jar` file will be located in the `target` directory.

---
