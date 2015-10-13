package cardgame.games.acestokings;

import cardgame.card.Rank;
import cardgame.card.Deck;
import cardgame.card.CardBank;
import cardgame.player.Player;
import cardgame.games.acestokings.melds.MeldsManager;
import cardgame.event.EventListener;
import cardgame.event.EventSource;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class Board
    implements EventListener
{
    private static final int    HAND_SIZE = 7;
    private static final String DECK      = "Deck";
    private static final String DISCARDS  = "Discard pile";
    
    private CardBank          deck_;
    private CardBank          discards_;
    private MeldsManager      melds_;
    private List<EventSource> eventSources_;
    private int               roundCount_;
    private int               startingPlayer_;
    
    // Constructor
    public Board(int nPlayers)
    {
        eventSources_   = new ArrayList<EventSource>();
        roundCount_     = 0;
        Random rng      = new Random();
        startingPlayer_ = rng.nextInt(nPlayers);
    }
    
    // Accessors
    public Rank getJokerRank()
    {
        return Rank.values()[roundCount_];
    }
    
    public CardBank getDeck()
    {
        return deck_;
    }
    
    public CardBank getDiscards()
    {
        return discards_;
    }
    
    public MeldsManager getMelds()
    {
        return melds_;
    }
    
    // Sets the board up for the next round to start
    public int initialiseRound(List<Player> players)
    {
        stopListening();
        deck_     = new Deck(DECK);
        discards_ = new CardBank(DISCARDS);
        deal(players);
        startListening(deck_);
        
        melds_          = new MeldsManager(getJokerRank());
        startingPlayer_ = (startingPlayer_ + 1) % players.size();
        roundCount_++;
        
        return startingPlayer_;
    }
    
    // Deals {@code HAND_SIZE} cards to all {@code players} then takes the top
    // card of the deck and places it on the discard pile.
    private void deal(List<Player> players)
    {
        deck_.shuffle();
        int nPlayers = players.size();
        for (int i = 0; i < nPlayers; i++) {
            CardBank hand = players.get(i).findCardBank(CardBanks.HAND);
            hand.transferFrom(deck_, 0, HAND_SIZE);
        }
        discards_.transferFrom(deck_, 0, 1);
    }
    
    // Implementation of EventListener
    // Shuffles the discard pile into the deck when it is out of cards, leaving
    // the last discarded card on the discard pile for the next players turn.
    public void update()
    {
        if (deck_.isEmpty()) {
            deck_.transferFrom(discards_, 1, discards_.size() - 1);
            deck_.shuffle();
        }
    }
    
    // Starts listening to {@code source}
    public void startListening(EventSource source)
    {
        source.addListener(this);
        eventSources_.add(source);
    }
    
    // Stops listening to {@code source}
    public void stopListening(EventSource source)
    {
        source.removeListener(this);
        eventSources_.remove(source);
    }
    
    // Stops listening entirely
    public void stopListening()
    {
        for (int i = 0; i < eventSources_.size(); i++)
            stopListening(eventSources_.get(i));
    }
}