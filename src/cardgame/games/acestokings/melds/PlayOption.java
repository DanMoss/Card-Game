package cardgame.games.acestokings.melds;

import java.util.ArrayList;
import java.util.List;

import cardgame.card.Card;
import cardgame.card.CardCollection;
import cardgame.card.Rank;
import cardgame.player.Selectable;

/**
 * A possible play that can be made with some cards to a meld.
 * 
 * @see AbstractMeld
 * @see Card
 * @see Selectable
 */
class PlayOption
    implements Selectable
{
    private final AbstractMeld meld_;
    private final Card[]       cards_;
    private final List<Rank>   jokers_;
    private       int          aceValue_;
    private       Rank         startingRank_;
    private       boolean      runMeldVariablesSet_;
    
    /**
     * Sole constructor.
     * 
     * @param meld  the destination {@AbstractMeld} for the {@code Card}s in
     *              the {@code PlayOption}
     * @param cards the {@code Card}s to play if the {@code PlayOption} is
     *              selected
     */
    public PlayOption(AbstractMeld aMeld, Card... cards)
    {
        this.meld_                = aMeld;
        this.cards_               = cards;
        this.runMeldVariablesSet_ = false;
        this.jokers_              = new ArrayList<Rank>();
    }
    
    /**
     * Returns the {@code Card}s that make up this {@code PlayOption}.
     * 
     * @return the {@code Card}s
     */
    protected Card[] getCards()
    {
        return this.cards_;
    }
    
    /**
     * Returns an ordered list of {@code Rank}s that the jokers played from
     * this {@code PlayOption} will mimic. For use by the {@code RunMeld}.
     * 
     * @return jokerRanks the ordered list of {@code Rank}s
     * @see    RunMeld
     */
    protected List<Rank> getJokers()
    {
        return this.jokers_;
    }
    
    /**
     * Returns the value of aces in this {@code PlayOption. For use by the
     * {@code RunMeld}.
     * 
     * @return the value of aces
     * @see    RunMeld
     */
    protected int getAceValue()
    {
        return this.aceValue_;
    }
    
    /**
     * Sets an ordered list of {@code Rank}s that the jokers played from this
     * {@code PlayOption} will mimic. For use by a {@code RunMeld}.
     * 
     * @param jokerRanks the ordered list of {@code Rank}s
     * @see   RunMeld
     */
    protected void setJokers(List<Rank> jokerRanks)
    {
        this.jokers_.addAll(jokerRanks);
        this.runMeldVariablesSet_ = true;
    }
    
    /**
     * Sets the value of aces in this {@code PlayOption}. For use by a
     * {@code RunMeld}.
     * 
     * @param aceValue the value of aces
     * @see   RunMeld
     */
    protected void setAceValue(int aceValue)
    {
        this.aceValue_            = aceValue;
        this.runMeldVariablesSet_ = true;
    }
    
    /**
     * Sets the first {@code Rank} of the run of {@code Card}s. For use in
     * {@link #getMessage()}.
     * 
     * @param startingRank the first {@code Rank} of the run
     * @see   RunMeld
     */
    protected void setFirstRank(Rank startingRank)
    {
        this.startingRank_        = startingRank;
        this.runMeldVariablesSet_ = true;
    }
    
    /* (non-Javadoc)
     * @see cardgame.player.Selectable#getMessage()
     */
    public String getMessage()
    {
        String string = "Play to " + this.meld_;
        if (this.runMeldVariablesSet_) {
            String multipleCards = " starting with a ";
            String singleCard    = " as the ";
            string += this.cards_.length > 1 ? multipleCards : singleCard;
            string += this.startingRank_;
        }
        return string;
    }
    
    /**
     * Plays the {@code Card}s in this {@code PlayOption} to the
     * {@code AbstractMeld}.
     * 
     * @param collection the source of the {@code Card}s
     */
    protected void play(CardCollection collection)
    {
        this.meld_.play(collection, this);
    }
}
