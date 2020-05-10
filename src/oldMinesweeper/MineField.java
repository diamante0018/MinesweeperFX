/*
 * Copyright (C) 2020 Edoardo Sanguineti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package oldMinesweeper;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Random;

/**
 * Main minefield class
 * @author es597
 */
public class MineField {
    private MineTile[][] field;
    private final int maxMines,rows,columns;
    private int ctrl;
    
    /**
     * This is the class constructor, it takes in as parameters the number of rows, columns and the maximum amount of mines
     * @param rows row
     * @param columns column
     * @param maxMines max number of mines that can e placed on the mine field
     */
    public  MineField(int rows,int columns,int maxMines) {
        this.columns = columns;
        this.rows = rows;
        this.maxMines = maxMines;
        ctrl = 0;
        field = this.conMineTile(this.rows,this.columns); //Rows and columns are initialized here
    }
    
    /**
     * This method places a mine on the indicated tile
     * @param x row
     * @param y column
     * @return True if Successful, False if MaxMines was reached or tile was already mined
     */
    public boolean mineTile(int x,int y) {                
        if(field[x][y].isMined() || maxMines == ctrl) {
            return false;
        }        
        
        field[x][y].setMined(); //Mines the tile
        
        //From this point on it's just about increasing the number in the sorrounding tiles
        int rowLimit = this.rows - 1;
        int columnLimit = this.columns - 1;
        
        int i,j;
        
        for(i = max(0,x-1); i <= min(x+1,rowLimit); i++) {
           for(j = max(0,y-1); j <= min(y+1,columnLimit); j++) {
               if ( i != x || j != y) 
                   field[i][j].addMinedN();
           }
        }
        
        ctrl++;
        return true;
    }
    
    /**
     * This method populates the mine filed with mines
     */
    public void populate() {
        Random gen = new Random();
        int i = 0,x,y;
        
         while (i < maxMines) {
             x = gen.nextInt(this.rows);
             y = gen.nextInt(this.columns); 
             
             if(x != 0 || y != 0) //If both of them are 0 we skip this iteration
                 if(this.mineTile(x, y)) { //If mining a tile was successful the return value is true so we increase 'i'
                     i++;
                 }
         }       
    }
    /**
     * This method uses a specified seed to populate the field
     * @deprecated This method should not be used because using the same seed twice means getting the same mine field
     * @param seed seed for the Random generator
     */
    public void populate(long seed) {
        Random gen = new Random(seed);
        int i = 0,x,y;
        
         while (i < maxMines) {
             x = gen.nextInt(this.rows);
             y = gen.nextInt(this.columns);
             
             if(x != 0 || y != 0)
                 if(this.mineTile(x, y))
                     i++;
         }       
    }
    
    /**
     * This methods returns a representation of the minefield
     * @return String mineField
     */
    @Override
    public String toString() {
        int check;
        String Sfield = "";
        
        for(int i=0;i<this.columns;i++) {
            for(int j=0;j<this.rows;j++) {
                check = this.getMinedNeighbour(i, j);
                
                if (check == -1)
                    Sfield = Sfield + 'x';
                else
                    Sfield = Sfield + check;
            }
            Sfield = Sfield + "\n";
        }
        
        return Sfield;
    }
    
    /**
     * New String representation of the Minefield
     * Used when actually playing the game
     * @return String mineField
     */
    public String newToString() {
        String Sfield = "";
        
        for(int i=0;i<this.columns;i++) {
            for(int j=0;j<this.rows;j++) {
                if(this.isRevealed(i, j)) 
                    Sfield = Sfield + this.getMinedNeighbour(i, j) + " "; //Check for -1
                
                else if(this.isMarked(i, j))
                    Sfield = Sfield + '?' + " ";
                
                else
                  Sfield = Sfield + "[]" + " ";  
            }
            Sfield = Sfield + "\n";
        }
        
        return Sfield;
    }
    
    /**
     * This method is the one that let's you play
     * @param x row
     * @param y column
     * @return true if the tile was successfully stepped on or false if the tile was mined/already revealed
     */
    public boolean step(int x,int y) {
        if(this.field[x][y].isMined())
            return false;
        
        if(this.field[x][y].getMinedN() > 0) {
            this.revealTile(x,y);       
            return true; //Just reveal this tile if mined neighbours > 0
        }
                
        int i,j;
        
        for(i = max(0,x-1); i <= min(x+1,(this.rows-1)); i++) {
           for(j = max(0,y-1); j <= min(y+1,(this.columns-1)); j++) {
               if (i != x || j != y) {
                   if(!(this.field[i][j].isReveal())) {
                       this.revealTile(i,j);
                       step(i,j);
                   }
               }
           }
        }
        
        return true;
    }
    
    /**
     * This method returns true if all mined tiles have been flagged by the player and no other tile has been incorrectly flagged.
     * The mines must be flagged with a '?'
     * @return boolean true if game is over false if not
     */
    public boolean areAllMinesRevealed() {
                
        for(int i=0;i<this.rows;i++) {
            for(int j=0;j<this.columns;j++) {
                if(this.field[i][j].isMined() && !(this.field[i][j].isMarked())) 
                    return false;
                
                if(!(this.field[i][j].isMined()) && (this.field[i][j].isMarked() || !(this.field[i][j].isReveal())))
                    return false;
            }
        }
        
        return true;
    }
    
    /**
     * This method returns the number of maxMines
     * @return int maxMines
     */
    public int getMaxMines() {
        return maxMines;
    }
    
    /**
     * This method returns the number of Rows
     * @return int Rows
     * @deprecated Use the field 'rows' instead
     */
    public int getRows() {
        return this.rows;
    }
    
    /**
     * This method returns the number of Columns
     * @return int Columns
     * @deprecated Use the field 'columns' instead
     */
    public int getColumns() {
        return this.columns;
    }
    
    /**
     * This method return the number of mined neighbours that a specific tile has
     * @param x row
     * @param y column
     * @return number of mined neighbours or -1 if it's a mine
     */
    public int getMinedNeighbour(int x,int y) {
        if (field[x][y].isMined())
            return -1;
        
        return field[x][y].getMinedN();
    }
    
    /**
     * Returns true if the specified tile is mined or false if not
     * @param x row
     * @param y column
     * @return true if mined false otherwise
     */
    public boolean isMined(int x,int y) {
       return field[x][y].isMined();
    }
    
    /**
     * This method returns true if the tile was marked by the player
     * @param x row
     * @param y column
     * @return boolean marked
     */
    public boolean isMarked(int x,int y) {
        return field[x][y].isMarked();
    }
    
    /**
     * This method returns true if the tile was revealed
     * @param x row
     * @param y column
     * @return boolean revealed 
     */
    public boolean isRevealed(int x,int y) {
        return field[x][y].isReveal();
    }
    
    /**
     * This method returns ctrl
     * @return int ctrl
     */
    public int getCtrl() {
        return ctrl;
    }
    
    /**
     * This method sets the tile as marked/un-marked by the player
     * @param x row
     * @param y column
     */
    public void markTile(int x, int y) {
        this.field[x][y].setMarked();
    }
    
    /**
     * This method sets the tile as revealed
     * @param x row
     * @param y column
     */
    public void revealTile(int x, int y) {
        this.field[x][y].setRevealed();
    }
    
    /**
     * This method initialises the MineTile 2D array  
     * @param x number of rows
     * @param y number of columns
     * @return The initialized Minefield
     */
    private MineTile[][] conMineTile(int x, int y) {
        MineTile f[][] = new MineTile[x][y];
        
        for(int i=0;i<x;i++) 
            for(int j=0;j<y;j++) 
                f[i][j] = new MineTile();           
                
        return f;
    }
}
