package cardgame.games.acestokings.melds;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import cardgame.card.PlayingCard;
import cardgame.card.CardCollection;
import cardgame.card.Rank;
import cardgame.card.Suit;

/**
 * A meld of cards that have the same suit. May contain jokers mimicking other
 * cards. Aces can be either high or low, but once chosen must remain either
 * high or low.
 * 
 * @see AbstractMeld
 * @see PlayingCard
 * @see Suit
 */
class RunMeld extends AbstractMeld
{
    private static final int HIGH_ACE_VALUE = Rank.KING.getValue() + 1;
    private static final int LOW_ACE_VALUE  = Rank.TWO.getValue()  - 1;
    
    private final Suit              meldSuit_;
    private final List<PlayingCard> cards_;
    private final List<PlayingCard> jokers_;
    private final List<Rank>        ranks_;
    // the ranks jokers in this meld are mimicking
    private final List<Rank>        jokerRanks_;
    
    // the next ranks that jokers added will mimic
    private List<Rank>  playOptionJokers_;
    // whether aces have been fixed to be low/high yet
    private boolean     aceIsFinalised_;
    // determines whether aces are low/high
    private int         aceValue_;
    // the last card removed by remove(Card) from this meld
    private PlayingCard lastRemovedCard_;
    
    /**
     * Sole constructor.
     * 
     * @param meldSuit the {@code Suit} of {@code PlayingCard}s that the
     *                 {@code RunMeld} can accept
     */
    protected RunMeld(Suit meldSuit)
    {
        this.meldSuit_         = meldSuit;
        this.cards_            = new ArrayList<PlayingCard>();
        this.jokers_           = new ArrayList<PlayingCard>();
        this.ranks_            = new ArrayList<Rank>();
        this.jokerRanks_       = new ArrayList<Rank>();
        this.playOptionJokers_ = new ArrayList<Rank>();
        this.aceIsFinalised_   = false;
    }
    
    /* (non-Javadoc)
     * @see AbstractMeld#play(CardCollection, PlayOption)
     */
    protected void play(CardCollection<PlayingCard> collection,
                        PlayOption anOption)
    {
        PlayingCard[] cards    = anOption.getCards();
        this.playOptionJokers_ = anOption.getJokers();
        if (!this.aceIsFinalised_)
            this.aceValue_ = anOption.getAceValue();
        
        for (PlayingCard aCard : cards) {
            boolean canPickUpJoker = this.remove(aCard);
            if (canPickUpJoker)
                collection.add(this.lastRemovedCard_);
            collection.transferTo(this, aCard);
        }
    }
    
    /** 
     * Finds the possible positions that a {@code PlayingCard} can be added to
     * in this {@code RunMeld}, then appends them to a list of
     * {@code PlayOption}s.
     * <p>
     * Firstly checks that the {@code PlayingCard} is of the correct
     * {@code Suit}. Then checks if there is an open position at the edge of a
     * meld within this {@code RunMeld}. Jokers can not be placed into a
     * position that is already occupied by the {@code PlayingCard} the joker
     * would attempt to mimic.
     * 
     * @see AbstractMeld#addCardPlays(List, PlayingCard)
     */
    protected void addCardPlays(List<PlayOption> options, PlayingCard aCard)
    {
        boolean correctSuit = checkSuits(aCard);
        
        if (correctSuit && isJoker(aCard)) {
            Set<Rank> ranks = EnumSet.range(Rank.ACE, Rank.KING);
            for (Rank aRank : ranks) {
                PlayingCard mimickedCard = new PlayingCard(aRank, meldSuit_);
                boolean  mimickedPresent = this.cards_.contains(mimickedCard);
                boolean  neighboursExist = checkForNeighbours(aRank);
                
                if (neighboursExist && !mimickedPresent) {
                    this.playOptionJokers_.add(aRank);
                    addOption(options, aRank, aCard);
                    this.playOptionJokers_.clear();
                }
            }
        }
        else if (correctSuit) {
            Rank aRank = aCard.getRank();
            if (checkForNeighbours(aRank))
                addOption(options, aRank, aCard);
        }
    }
    
    /** 
     * Finds the possible melds that can be played to this {@code RunMeld} with
     * a set of {@code PlayingCard}s, then appends them to a list of
     * {@code PlayOption}s.
     * <p>
     * Firstly checks that the {@code PlayingCard}s are of the correct
     * {@code Suit} and that they form a run. Then potential starting locations
     * of the run are identified based on the kinds of {@code PlayingCard}s in
     * the run. Afterwards a check is made to see if all specified jokers can 
     * be appropriately added to this {@code RunMeld} without removing the 
     * actual {@code PlayingCard} they would be mimicking.
     * 
     * @see AbstractMeld#addMeldPlays(List, PlayingCard[])
     */
    protected void addMeldPlays(List<PlayOption> options, PlayingCard... cards)
    {
        if (checkRun(cards) && checkSuits(cards)) {
            Set<Rank> viableMeldStarts = findMeldStarts(cards);
            
            for (Rank aRank : viableMeldStarts) {
                boolean canPlace = verifyMeldStart(aRank, cards);
                if (canPlace)
                    addOption(options, aRank, cards);
                this.playOptionJokers_.clear();
            }
        }
    }
    
    /* (non-Javadoc)
     * @see Selectable#getMessage()
     */
    public String getMessage()
    {
        return "a run of " + meldSuit_;
    }
    
    /* (non-Javadoc)
     * @see CardCollection#size()
     */
    public int size()
    {
        return this.cards_.size();
    }
    
    /**
     * Attempts to add a {@code PlayingCard} to this {@code RunMeld}.
     * <p>
     * If the {@code PlayingCard} is a joker, then the {@code Rank} is taken
     * from a list of upcoming jokers.
     * 
     * @see CardCollection#add(PlayingCard)
     */
    public void add(PlayingCard aCard)
    {
        Rank rank;
        if (isJoker(aCard)) {
            rank = this.playOptionJokers_.remove(0);
            this.jokers_.add(aCard);
            this.jokerRanks_.add(rank);
        }
        else {
            rank = aCard.getRank();
        }
        
        if (rank == Rank.ACE)
            this.aceIsFinalised_ = true;
        
        this.cards_.add(aCard);
        this.ranks_.add(rank);
    }
    
    /**
     * Attempts to remove a {@code PlayingCard} from this {@code RunMeld}.
     * <p>
     * First checks if the {@code PlayingCard} or a joker mimicking the 
     * {@code PlayingCard} is in this {@code RunMeld}. If it is, then either
     * the {@code PlayingCard} or a joker is removed appropriately. The removed
     * {@code PlayingCard} is stored as the field {@code lastRemovedCard_} for
     * potential uses elsewhere.
     * 
     * @see CardCollection#remove(PlayingCard)
     */
    public boolean remove(PlayingCard aCard)
    {
        Rank    rank     = aCard.getRank();
        boolean isInMeld = this.ranks_.contains(rank);
        boolean cardRemoved;
        
        if (isInMeld) {
            boolean isAJoker = this.jokerRanks_.contains(rank);
            if (isAJoker)
                this.lastRemovedCard_ = this.jokers_.remove(0);
            else
                this.lastRemovedCard_ = new PlayingCard(rank, this.meldSuit_);
            cardRemoved = this.cards_.remove(this.lastRemovedCard_);
        }
        else {
            cardRemoved = false;
        }
        
        return cardRemoved;
    }
    
    /* (non-Javadoc)
     * @see CardCollection#reset()
     */
    public void reset()
    {
        this.cards_.clear();
        this.jokers_.clear();
        this.ranks_.clear();
        this.jokerRanks_.clear();
        this.playOptionJokers_.clear();
        this.aceIsFinalised_ = false;
    }
    
    // Checks if some given {@code PlayingCard}s are all of the correct
    // {@code Suit} for addition to this {@code RunMeld}.
    private boolean checkSuits(PlayingCard... cards)
    {
        boolean allCorrect = true;
        for (PlayingCard aCard : cards) {
            boolean singleCorrect = aCard.getSuit() == this.meldSuit_;
            singleCorrect         = singleCorrect   || isJoker(aCard);
            allCorrect            = allCorrect      && singleCorrect;
        }
        return allCorrect;
    }
    
    // Checks if some given {@code PlayingCard}s form an ascending run by
    // checking that each {@code PlayingCard} is consecutive with the one after
    // it.
    private boolean checkRun(PlayingCard... cards)
    {
        boolean isARun = true;
        int     nCards = cards.length;
        for (int i = 1; i < nCards; i++)
            isARun = isARun && checkConsecutiveCards(cards[i - 1], cards[i]);
        
        return isARun;
    }
    
    // Checks if the specified {@code PlayingCard}s are consecutive.
    //
    // More specifically, checks if {@code card1} directly follows
    // {@code card2}. If either of the {@code Card}s is a joker, then the
    // {@code PlayingCard}s are treated as consecutive.
    //
    // @param  card1 the speculative first {@code PlayingCard}
    // @param  card2 the speculative following {@code PlayingCard}
    // @return true if the {@code PlayingCard}s are consecutive
    private boolean checkConsecutiveCards(PlayingCard card1, PlayingCard card2)
    {
        boolean jokerPresent = isJoker(card1) || isJoker(card2);
        boolean consecutiveCards;
        
        if (jokerPresent) {
            consecutiveCards = true;
        }
        else {
            Rank card1Rank   = card1.getRank();
            Rank card2Rank   = card2.getRank();
            consecutiveCards = checkConsecutiveRanks(card1Rank, card2Rank);
        }
        
        return consecutiveCards;
    }
    
    // Checks if the specified {@code Rank}s are consecutive.
    //
    // More specifically, checks if {@code rank2} directly follows
    // {@code rank1}. If neither of these {@code Rank}s are an ace, or in the
    // event that aces have had their value fixed in this {@code RunMeld}, then
    // this is just a simple comparison of the {@code Rank}s.
    //
    // Otherwise, checks if the non-Ace {@code Rank} (two consecutive
    // {@code Rank}s must be different) is the sufficient {@code Rank} for a
    // high or low valued ace. If so then the value of aces in this
    // {@code RunMeld} is updated in case this ace is added to the
    // {@code RunMeld}.
    //
    // @param  rank1 the speculative first {@code Rank}
    // @param  rank2 the speculative following {@code Rank}
    // @return true if the {@code Rank}s are consecutive
    private boolean checkConsecutiveRanks(Rank rank1, Rank rank2)
    {
        boolean rank1IsAce = rank1 == Rank.ACE;
        boolean rank2IsAce = rank2 == Rank.ACE;
        boolean acePresent = rank1IsAce || rank2IsAce;
        boolean consecutiveRanks;
        
        if (this.aceIsFinalised_ || !acePresent) {
            int rank1Value   = rank1IsAce ? this.aceValue_ : rank1.getValue();
            int rank2Value   = rank2IsAce ? this.aceValue_ : rank2.getValue();
            consecutiveRanks = rank2Value - rank1Value == 1;
        }
        else {
            if (rank1 == Rank.KING) {
                this.aceValue_   = RunMeld.HIGH_ACE_VALUE;
                consecutiveRanks = true;
            }
            else if (rank2 == Rank.TWO) {
                this.aceValue_   = RunMeld.LOW_ACE_VALUE;
                consecutiveRanks = true;
            }
            else {
                consecutiveRanks = false;
            }
        }
        
        return consecutiveRanks;
    }
    
    // Checks if the given {@code Rank} could have neighbours upon being added
    // to this {@code RunMeld}. Having neighbours is a necessary requirement of
    // playing {@code PlayingCard}s one at a time.
    //
    // To check for neighbours, the method iterates through the list of
    // {@code Rank}s in this {@code RunMeld}. {@code Rank}s are compared to the
    // {@code Rank} of the given {@code PlayingCard} to see if they are
    // neighbours.
    // 
    // @param  aRank the {@code Rank} to check for neighbours
    // @return true if the {@code Rank} could have a neighbour after placement
    private boolean checkForNeighbours(Rank aRank)
    {
        boolean isNextToAMeld = false;
        
        for (Rank rank2 : this.ranks_) {
            boolean aRankBeforeRank2 = checkConsecutiveRanks(aRank, rank2);
            boolean rank2BeforeARank = checkConsecutiveRanks(rank2, aRank);
            if (aRankBeforeRank2 || rank2BeforeARank)
                isNextToAMeld = true;
        }
        
        return isNextToAMeld;
    }
    
    // Finds the potential starting {@code Rank}s of a run of
    // {@code PlayingCard}s.
    private Set<Rank> findMeldStarts(PlayingCard... cards)
    {
        boolean nonJokerFound = false;
        int     nonJokerIndex = 0;
        int     nCards        = cards.length;
        
        // discovering the necessary parameters to create the set
        while (nonJokerIndex < nCards && !nonJokerFound) {
            if (!isJoker(cards[nonJokerIndex]))
                nonJokerFound = true;
            else
                nonJokerIndex++;
        }
        
        // obtaining the set
        Set<Rank> viableMeldStarts;
        if (!nonJokerFound) {
            viableMeldStarts = generateAllJokerStart(nCards);
        }
        else {
            Rank nonJokerRank = cards[nonJokerIndex].getRank();
            viableMeldStarts  = findNonJokerStart(nonJokerRank, nonJokerIndex);
        }
        
        return viableMeldStarts;
    }
    
    // Finds the potential range of starting {@code Rank}s of a run of
    // {@code PlayingCard}s which consists solely of jokers.
    private Set<Rank> generateAllJokerStart(int nCards)
    {
        // If the ace is finalised and high, then we cannot put a joker in the
        // high ace spot anyway
        boolean aceCanBeHigh      = !this.aceIsFinalised_;
        int     kingStepsFromLast = aceCanBeHigh ?        2 : 1;
        Rank    firstRankStart    = aceCanBeHigh ? Rank.TWO : Rank.ACE;
        Rank    lastRankStart     = Rank.KING;
        
        for (int i = kingStepsFromLast; i < nCards; i++)
            lastRankStart = lastRankStart.getNeighbour(false);
        
        return EnumSet.range(firstRankStart, lastRankStart);
    }
    
    // Finds the single potential starting {@code Rank} of a run of
    // {@code PlayingCard}s which does not consist solely of jokers.
    private Set<Rank> findNonJokerStart(Rank nonJokerRank, int nonJokerIndex)
    {
        for (int i = 0; i < nonJokerIndex; i++)
            nonJokerRank = nonJokerRank.getNeighbour(false);
        return EnumSet.of(nonJokerRank);
    }
    
    // Verifies that all of the jokers in a run of {@code PlayingCard}s can be
    // placed appropriately in this {@RunMeld} according to the
    // {@code PlayingCard}s they are mimicking.
    // 
    // Additionally, jokers are added to a list of jokers for use in the
    // creation of a {@code PlayOption} at a later time, assuming the run can
    // placed.
    // 
    // @param  meldStart the initial {@code Rank} the run is starting from
    // @param  cards     the {@code PlayingCard}s that make up the run
    // @return true if the run may be placed here
    private boolean verifyMeldStart(Rank meldStart, PlayingCard... cards)
    {
        Rank    aRank    = meldStart;
        boolean canPlace = true;
        
        for (PlayingCard aCard : cards) {
            if (isJoker(aCard)) {
                this.playOptionJokers_.add(aRank);
                canPlace = canPlace && !this.ranks_.contains(aRank);
                
                // Jokers skip the setting of the ace value elsewhere
                if (aRank == Rank.ACE && meldStart != Rank.ACE)
                    this.aceValue_ = RunMeld.HIGH_ACE_VALUE;
                else if (aRank == Rank.ACE && meldStart == Rank.ACE)
                    this.aceValue_ = RunMeld.LOW_ACE_VALUE;
            }
            aRank = aRank == Rank.KING ? Rank.ACE : aRank.getNeighbour(true);
        }
        
        return canPlace;
    }
    
    // Appends a list of {@code PlayOption}s with a new {@code PlayOption}.
    // 
    // Creates a new {@code PlayOption} and provides it with an ordered list of
    // {@code Rank}s that the jokers in this {@code PlayOption} will mimic when
    // added to this {@code RunMeld}. The current value of aces at the time of
    // creating the {@code PlayOption} is also provided. The list of
    // {@code PlayOption}s is then appended.
    //
    // @param options the list of {@code PlayOptions} to append
    // @param aRank   the first rank of this run
    // @param cards   the {@code PlayingCard}s that could be played by the
    //                created {@code PlayOption}
    private void addOption(List<PlayOption> options, Rank aRank,
                           PlayingCard... cards)
    {
        PlayOption anOption = new PlayOption(this, cards);
        anOption.setJokers(this.playOptionJokers_);
        anOption.setAceValue(this.aceValue_);
        anOption.setFirstRank(aRank);
        options.add(anOption);
    }
}
