package cardgame;

public class Deck extends CardBank
{
    public Deck(String name)
    {
        this(name, 1);
    }
    
    public Deck(String name, int nClassicalDecks)
    {
        super(name, nClassicalDecks * Card.Suit.values().length
                                    * Card.Face.values().length);
        
        for (int i = 0; i < nClassicalDecks; i++) {
            for (Card.Suit suit : Card.Suit.values()) {
                for (Card.Face face : Card.Face.values()) {
                    Card card = new Card(suit, face);
                    add(card);
                }
            }
        }
    }
}