package Fallin.engine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Represents a cell in the game grid.
 * Each cell can contain a game item and display an image.
 */
public class Cell implements Serializable {
    private static final double ICON_SIZE = 55; // Fixed size for the icons
    private GameItem item; // The game item contained in this cell
    private transient String imageName; // The image name to display the item or player

    /**
     * Constructor to initialize the cell with no item.
     */
    public Cell() {
        this.item = null;
    }

    /**
     * Sets the game item in the cell and updates the image.
     *
     * @param item the game item to set
     */
    public void setItem(GameItem item) {
        this.item = item;
        updateImageName();
    }

    /**
     * Returns the game item in the cell.
     *
     * @return the game item
     */
    public GameItem getItem() {
        return item;
    }

    /**
     * Interacts with the player using the game item and clears the item.
     *
     * @param player the player to interact with
     */
    public void interact(Player player) {
        if (item != null) {
            item.interact(player);
            item = null; // clears item following interaction
            updateImageName();
        }
    }

    /**
     * Updates the image name based on the game item.
     */
    private void updateImageName() {
        if (item instanceof Treasures) {
            imageName = "treasure_icon.png";
        } else if (item instanceof MedicalUnit) {
            imageName = "MedicalUnit.png";
        } else if (item instanceof Trap) {
            imageName = "trap_icon.png";
        } else if (item instanceof Mutant) {
            imageName = "mutant_icon.png";
        } else {
            imageName = null;
        }
    }

    /**
     * Gets the image name for the cell's content.
     *
     * @return the image name
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Sets the player image name in the cell.
     */
    public void setPlayerImage() {
        imageName = "player_icon.png";
    }

    /**
     * Clears the player image name and updates the image name.
     */
    public void clearPlayerImage() {
        updateImageName();
    }

    /**
     * Clears the item from the cell and updates the image name.
     */
    public void clearItem() {
        this.item = null;
        updateImageName();
    }

    /**
     * Restore the transient fields after deserialization.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        updateImageName();
    }
}
