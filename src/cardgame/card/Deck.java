package cardgame.card;

public class Deck extends CardBank
{
    public Deck(String name)
    {
        this(name, 1);
    }
    
    public Deck(String name, int nClassicalDecks)
    {
        super(name);
        for (int i = 0; i < nClassicalDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values())
                    add(new Card(rank, suit));
            }
        }
    }
}