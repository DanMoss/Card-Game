package cardgame.games.acestokings.melds;

import java.util.ArrayList;
import java.util.List;

import cardgame.card.Card;
import cardgame.card.CardCollection;
import cardgame.card.Rank;

/**
 * A meld of cards that have the same rank. May contain jokers mimicking other
 * cards.
 * 
 * @see AbstractMeld
 * @see Card
 * @see Rank
 */
class RankMeld extends AbstractMeld
{
    private static final int MELD_CAPACITY = 4;
    
    private final Rank       meldRank_;
    private final List<Card> meld_;
    private final List<Card> jokers_;
    
    /**
     * Sole constructor.
     * 
     * @param meldRank the {@code Rank} of {@code Card}s that the
     *                 {@code RankMeld} can accept
     */
    protected RankMeld(Rank meldRank)
    {
        this.meldRank_ = meldRank;
        this.meld_     = new ArrayList<Card>(RankMeld.MELD_CAPACITY);
        this.jokers_   = new ArrayList<Card>(RankMeld.MELD_CAPACITY);
    }
    
    /* (non-Javadoc)
     * @see AbstractMeld#play(cardgame.card.CardCollection, PlayOption)
     */
    protected void play(CardCollection collection, PlayOption option)
    {
        // Building the collection of jokers to pick up
        Card[] cards            = option.getCards();
        int    nJokersToReplace = countJokersToPickUp(cards);
        Card[] jokers           = new Card[nJokersToReplace];
        for (int i = 0; i < nJokersToReplace; i++)
            jokers[i] = this.jokers_.get(i);
        
        // Transferring the cards
        this.transferTo(collection, jokers);
        collection.transferTo(this, cards);
    }
    
    /* (non-Javadoc)
     * @see AbstractMeld#addCardPlays(java.util.List, cardgame.card.Card)
     */
    protected void addCardPlays(List<PlayOption> options, Card aCard)
    {
        boolean meldExists = this.size() >= MeldsManager.MINIMUM_MELD_SIZE;
        boolean canPlay    = meldExists  && checkRanks(aCard);
        canPlay            = canPlay     && hasSpace(aCard);
        if (canPlay)
            addOption(options, aCard);
    }
    
    /* (non-Javadoc)
     * @see AbstractMeld#addMeldPlays(java.util.List, cardgame.card.Card[])
     */
    protected void addMeldPlays(List<PlayOption> options, Card... cards)
    {
        boolean canPlay = hasSpace(cards) && checkRanks(cards);
        if (canPlay)
            addOption(options, cards);
    }
    
    /* (non-Javadoc)
     * @see AbstractMeld#toString()
     */
    public String toString()
    {
        return "a set of " + this.meldRank_.getPlural();
    }
    
    
    /* (non-Javadoc)
     * @see cardgame.card.CardCollection#size()
     */
    public int size()
    {
        return this.meld_.size();
    }
    
    /**
     * Adds a {@code Card} to this {@code RankMeld}. Jokers are also added to
     * their own list.
     * 
     * @see cardgame.card.CardCollection#add(cardgame.card.Card)
     */
    public void add(Card aCard)
    {
        this.meld_.add(aCard);
        if (isJoker(aCard))
            this.jokers_.add(aCard);
    }
    
    /**
     * Removes a {@code Card} from this {@code RankMeld}. Jokers are further
     * removed from their own list.
     * 
     * @see cardgame.card.CardCollection#remove(cardgame.card.Card)
     */
    public boolean remove(Card aCard)
    {
        if (isJoker(aCard))
            this.jokers_.remove(aCard);
        return this.meld_.remove(aCard);
    }
    
    /* (non-Javadoc)
     * @see cardgame.card.CardCollection#reset()
     */
    public void reset()
    {
        this.meld_.clear();
    }
    
    // Checks that {@code cards} are all of the correct {@code Rank}
    private boolean checkRanks(Card... cards)
    {
        boolean allCorrect = true;
        for (Card aCard : cards) {
            boolean singleCorrect = aCard.getRank() == this.meldRank_;
            singleCorrect         = singleCorrect   || isJoker(aCard);
            allCorrect            = allCorrect      && singleCorrect;
        }
        return allCorrect;
    }
    
    // Checks that there is space to play {@code cards}
    private boolean hasSpace(Card... cards)
    {
        int     nJokersToPickUp = countJokersToPickUp(cards);
        int     endMeldSize     = this.size() + cards.length - nJokersToPickUp;
        boolean withinCapacity  = endMeldSize <= RankMeld.MELD_CAPACITY;
        return  withinCapacity;
    }
    
    // Finds out how many jokers to pick up when playing {@code cards}
    private int countJokersToPickUp(Card... cards)
    {
        int nNonJokers = 0;
        for (Card card : cards) {
            if (!isJoker(card))
                nNonJokers++;
        }
        // Minimum of the number of non-jokers being played and the number of
        // jokers in the meld.
        int nJokers         = this.jokers_.size();
        int nJokersToPickUp = nNonJokers < nJokers ? nNonJokers : nJokers;
        return nJokersToPickUp;
    }
    
    // Appends {@code options} with a new option
    private void addOption(List<PlayOption> options, Card... cards)
    {
        PlayOption anOption = new PlayOption(this, cards);
        options.add(anOption);
    }
}
