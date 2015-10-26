package cardgame.events;

public interface EventSource
{
    // Adds {@code listener} to the list of EventListeners
    public void addListener(EventListener listener);
    
    // Removes {@code listener} from the list of EventListeners
    public void removeListener(EventListener listener);
    
    // Notifies any EventListeners of an event
    public void notifyListeners();
}