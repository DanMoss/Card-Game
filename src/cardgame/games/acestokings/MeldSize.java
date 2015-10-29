package cardgame.games.acestokings;

import cardgame.player.Selectable;

class MeldSize
    implements Selectable
{
    private final int size_;
    
    // Constructor
    MeldSize(int size)
    {
        this.size_ = size;
    }
    
    int getSize()
    {
        return this.size_;
    }
    
    /* (non-Javadoc)
     * @see cardgame.player.Selectable#getMessage()
     */
    @Override
    public String getMessage() {
        return getSize() + " cards";
    }
}
