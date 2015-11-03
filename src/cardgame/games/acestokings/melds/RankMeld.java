package cardgame.games.acestokings.melds;

import java.util.ArrayList;
import java.util.List;

import cardgame.card.CardCollection;
import cardgame.card.traditional.PlayingCard;
import cardgame.card.traditional.Rank;

/**
 * A meld of cards that have the same rank. May contain jokers mimicking other
 * cards.
 * 
 * @see Meld
 * @see PlayingCard
 * @see Rank
 */
class RankMeld extends Meld
{
    private static final int MELD_CAPACITY = 4;
    
    private final Rank              meldRank_;
    private final List<PlayingCard> meld_;
    private final List<PlayingCard> jokers_;
    
    /**
     * Sole constructor.
     * 
     * @param meldRank the {@code Rank} of {@code PlayingCard}s that the
     *                 {@code RankMeld} can accept
     */
    protected RankMeld(Rank meldRank)
    {
        this.meldRank_ = meldRank;
        this.meld_     = new ArrayList<PlayingCard>(RankMeld.MELD_CAPACITY);
        this.jokers_   = new ArrayList<PlayingCard>(RankMeld.MELD_CAPACITY);
    }
    
    /* (non-Javadoc)
     * @see Meld#play(CardCollection, PlayOption)
     */
    @Override
    protected void play(CardCollection<PlayingCard> collection,
                        PlayOption option)
    {
        // Building the collection of jokers to pick up
        PlayingCard[] cards            = option.getCards();
        int           nJokersToReplace = countJokersToPickUp(cards);
        PlayingCard[] jokers           = new PlayingCard[nJokersToReplace];
        for (int i = 0; i < nJokersToReplace; i++)
            jokers[i] = this.jokers_.get(i);
        
        // Transferring the cards
        this.transferTo(collection, jokers);
        collection.transferTo(this, cards);
    }
    
    /* (non-Javadoc)
     * @see Meld#addCardPlays(java.util.List, PlayingCard)
     */
    protected void addCardPlays(List<PlayOption> options, PlayingCard aCard)
    {
        boolean meldExists = this.size() >= MeldsManager.MINIMUM_MELD_SIZE;
        boolean canPlay    = meldExists  && checkRanks(aCard);
        canPlay            = canPlay     && hasSpace(aCard);
        if (canPlay)
            addOption(options, aCard);
    }
    
    /* (non-Javadoc)
     * @see Meld#addMeldPlays(java.util.List, PlayingCard[])
     */
    protected void addMeldPlays(List<PlayOption> options, PlayingCard... cards)
    {
        boolean canPlay = hasSpace(cards) && checkRanks(cards);
        if (canPlay)
            addOption(options, cards);
    }
    
    /* (non-Javadoc)
     * @see Selectable#getMessage()
     */
    public String getMessage()
    {
        return "a set of " + this.meldRank_.getPlural();
    }
    
    
    /* (non-Javadoc)
     * @see CardCollection#size()
     */
    public int size()
    {
        return this.meld_.size();
    }
    
    /**
     * Adds a {@code PlayingCard} to this {@code RankMeld}. Jokers are also
     * added to their own list.
     * 
     * @see CardCollection#add(PlayingCard)
     */
    public void add(PlayingCard aCard)
    {
        this.meld_.add(aCard);
        if (isJoker(aCard))
            this.jokers_.add(aCard);
    }
    
    /**
     * Removes a {@code PlayingCard} from this {@code RankMeld}. Jokers are
     * further removed from their own list.
     * 
     * @see CardCollection#remove(PlayingCard)
     */
    public boolean remove(PlayingCard aCard)
    {
        if (isJoker(aCard))
            this.jokers_.remove(aCard);
        return this.meld_.remove(aCard);
    }
    
    /* (non-Javadoc)
     * @see CardCollection#reset()
     */
    public void reset()
    {
        this.meld_.clear();
    }
    
    // Checks that the specified {@code PlayingCard}s are all of the correct
    // {@code Rank}.
    private boolean checkRanks(PlayingCard... cards)
    {
        boolean allCorrect = true;
        for (PlayingCard aCard : cards) {
            boolean singleCorrect = aCard.getRank() == this.meldRank_;
            singleCorrect         = singleCorrect   || isJoker(aCard);
            allCorrect            = allCorrect      && singleCorrect;
        }
        return allCorrect;
    }
    
    // Checks that there is space to play {@code cards}
    private boolean hasSpace(PlayingCard... cards)
    {
        int     nJokersToPickUp = countJokersToPickUp(cards);
        int     endMeldSize     = this.size() + cards.length - nJokersToPickUp;
        boolean withinCapacity  = endMeldSize <= RankMeld.MELD_CAPACITY;
        return  withinCapacity;
    }
    
    // Finds out how many jokers to pick up when playing the specified
    // {@code PlayingCard}s.
    private int countJokersToPickUp(PlayingCard... cards)
    {
        int nNonJokers = 0;
        for (PlayingCard aCard : cards) {
            if (!isJoker(aCard))
                nNonJokers++;
        }
        // Minimum of the number of non-jokers being played and the number of
        // jokers in the meld.
        int nJokers         = this.jokers_.size();
        int nJokersToPickUp = nNonJokers < nJokers ? nNonJokers : nJokers;
        return nJokersToPickUp;
    }
    
    // Appends {@code options} with a new option
    private void addOption(List<PlayOption> options, PlayingCard... cards)
    {
        PlayOption anOption = new PlayOption(this, cards);
        options.add(anOption);
    }
}
