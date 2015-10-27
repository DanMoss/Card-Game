package cardgame.card;

/**
 * A source of events relating to decks of cards.
 */
public interface DeckEventSource
{
    /**
     * Adds {@code listener} to the list of {@code DeckEventListener}s.
     * 
     * @param listener the {@code DeckEventListener} to add
     */
    public void addListener(DeckEventListener listener);
    
    /**
     * Removes {@code listener} from the list of {@code DeckEventListener}s.
     * 
     * @param listener the {@code DeckEventListener} to remove
     */
    public void removeListener(DeckEventListener listener);
    
    /**
     * Notifies all {@code DeckEventListener}s of an event.
     */
    public void notifyListeners();
}
