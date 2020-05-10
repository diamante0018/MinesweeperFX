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

/**
 * <p>This class represents a Tile.</p> It has a boolean field to see if it has been mined, integer field to see the number of mined 
 * neighbours, a boolean field to see if it has been revealed, a boolean field to see if it has been marked and a toString method that
 * returns the representation of the Tile.
 * @author es597 CandNo: 215755
 */
public class MineTile {
    private boolean mined,reveal,marked;
    private int minedN;
    
    /**
     * MineTile Constructor, initialises fields
     */
   public MineTile() {
       mined = false;
       reveal = false;
       marked = false;
       minedN = 0;
   } 
    
    /**
     * Sets the value of mined to true      
     */
    public void setMined() {
        this.mined = true;
    }
    
    /**
     * Sets the value of revealed      
     */
    public void setRevealed() {
        this.reveal = true;
    }
    
    /**
     * Marks or un-marks a tile
     */
    public void setMarked() {
        this.marked = !this.marked;
    }
    
    /**
     * Sets the value of Mined Neighbours
     * @deprecated it's best to use the method that increments this field by one
     * @param minedN number of mined neighbouring tiles
     */
    public void setMinedN(int minedN) {
        this.minedN = minedN;
    }
    
    /**
     * This method increases the filed mineN by 1
     */
    public void addMinedN() {
        this.minedN++;
    }
    
    /**
     * Returns true if this tile has a mine
     * @return boolean mined
     */
    public boolean isMined() {
        return mined;
    }
    
    /**
     * Returns true if this tile was already revealed by the player
     * @return boolean reveal
     */
    public boolean isReveal() {
        return reveal;
    }
    
    /**
     * Returns true if this tile was already marked by the player
     * @return boolean marked
     */
    public boolean isMarked() {
        return marked;
    }
    
    /**
     * Returns the number of mined neighbours that this tile has
     * @return int mineNeighbours
     */
    public int getMinedN() {
        return minedN;
    }
}
