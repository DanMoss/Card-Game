package cardgame.card;

public class Deck extends CardBank
{
    public Deck(String name)
    {
        this(name, 1);
    }
    
    public Deck(String name, int nClassicalDecks)
    {
        super(name, nClassicalDecks * Suit.values().length
                                    * Rank.values().length);
        
        for (int i = 0; i < nClassicalDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    Card card = new Card(rank, suit);
                    add(card);
                }
            }
        }
    }
}