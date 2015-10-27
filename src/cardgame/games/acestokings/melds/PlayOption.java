package cardgame.games.acestokings.melds;

import cardgame.card.Card;
import cardgame.card.Bank;
import cardgame.card.Rank;
import cardgame.player.Selectable;

class PlayOption
    implements Selectable
{
    private final AbstractMeld meld_;
    private final Card[]       cards_;
    private       int          index_;
    private       boolean      indexSet_;
    
    // Constructor
    public PlayOption(AbstractMeld meld, Card... cards)
    {
        meld_     = meld;
        cards_    = cards;
        indexSet_ = false;
    }
    
    // Accessors
    protected Card[] getCards()
    {
        return cards_;
    }
    
    protected int getIndex()
    {
        return index_;
    }
    
    // Mutator
    protected void setIndex(int index)
    {
        index_    = index;
        indexSet_ = true;
    }
    
    // Implementation of Selectable
    // Used by Selector to convey the option to the player
    public String getMessage()
    {
        String string = "Play to " + meld_;
        if (indexSet_) {
            String multipleCards = " starting with a ";
            String singleCard    = " as the ";
            string += cards_.length > 1 ? multipleCards : singleCard;
            string += Rank.values()[index_];
        }
        return string;
    }
    
    // Other methods
    protected void play(Bank hand)
    {
        meld_.play(hand, this);
    }
}