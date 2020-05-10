So the GUI has a MenuBar witch contains 2 options for different difficulties.
Each time the user changes difficulty everything is set to new and the scene gets updated with the new GRID.
The GUI is kept up to date with a refresh method which goes through the whole minefield object and if a mine has been reveled the Button text gets updated to the number 
of mined neighbours, after it check if everything was revealed/flagged and if so the winning dialog pops up.
The data is fecthed from the 'old' Minefield object simply by having the buttons coordinates match with the tiles on the field.
Each interaction with the GUI (Left/Right Click) will correspond to an alteration to the original Minefield object.
The user can left/right click a tile.
Left click will trigger the step method, if you step on a tile a defeat dialog pops up asking for a new game or quit.
Right click will place a flag img ontop on the button to mark a tile (must flag every mine tile to win). Incorrectly placed flags can be removed with another right click.
Each button as an inner EventHandler to handle the left/right click event
There's a boolean 2D array to keep track of which button has a flag img and need to be removed.
Additional features:
Ony the flag img feature.