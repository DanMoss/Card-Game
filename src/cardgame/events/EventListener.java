package cardgame.events;

public interface EventListener
{
    // Used by an EventSource to update the listener
    public void update();
    
    // Starts listening to {@code source}
    public void startListening(EventSource source);
    
    // Stops listening to {@code source}
    public void stopListening(EventSource source);
    
    // Stops listening entirely
    public void stopListening();
}