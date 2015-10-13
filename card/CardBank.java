package cardgame.card;

import cardgame.event.EventSource;
import cardgame.event.EventListener;
import cardgame.player.Selectable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardBank
    implements Selectable, EventSource
{
    private final String              name_;
    private final List<Card>          cards_;
    private final List<EventListener> listeners_;
    
    // Constructor
    public CardBank(String name)
    {
        name_      = name;
        cards_     = new ArrayList<Card>();
        listeners_ = new ArrayList<EventListener>();
    }
    
    // Accessors
    @Override
    public String toString()
    {
        return name_;
    }
    
    public Card getCard(int i)
    {
        return cards_.get(i);
    }
    
    // Implementation of Selectable
    // Used by Selector to convey the option to the player
    public String getMessage()
    {
        return toString(); // Code duplication here, consider other options
    }
    
    // Implementation of EventSource
    // Adds {@code listener} to the list of EventListeners
    public void addListener(EventListener listener)
    {
        listeners_.add(listener);
    }
    
    // Removes {@code listener} from the list of EventListeners
    public void removeListener(EventListener listener)
    {
        listeners_.remove(listener);
    }
    
    // Notifies any EventListeners of an event
    public void notifyListeners()
    {
        for (int i = 0; i < listeners_.size(); i++)
            listeners_.get(i).update();
    }
    
    // Other methods
    // Transfers {@code amount} cards from {@code source}, beginning with the
    // card at {@code firstIndex}.
    public void transferFrom(CardBank source, int firstIndex, int amount)
    {
        for (int i = 0; i < amount; i++)
            add(source.discard(firstIndex));
        
        notifyListeners();
    }
    
    // Shuffles the CardBank
    public void shuffle()
    {
        Random rng = new Random();
        Collections.shuffle(cards_, rng);
    }
    
    // ArrayList method shortcuts
    public void add(Card card)
    {
        cards_.add(card);
        notifyListeners();
    }
    
    public void add(int index, Card card)
    {
        cards_.add(index, card);
        notifyListeners();
    }
    
    public Card discard(int index)
    {
        Card card = cards_.remove(index);
        notifyListeners();
        return card;
    }
    
    public boolean discard(Card card)
    {
        boolean cardRemoved = cards_.remove(card);
        notifyListeners();
        return cardRemoved;
    }
    
    public int size()
    {
        return cards_.size();
    }
    
    public boolean isEmpty()
    {
        return cards_.isEmpty();
    }
    
    public Card[] toArray()
    {
        Card[] cards = new Card[size()];
        return cards_.toArray(cards);
    }
}