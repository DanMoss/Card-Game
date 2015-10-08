package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardBank
{
    private final String          name_;
    private final ArrayList<Card> cards_;
        
    // Constructor
    public CardBank(String name, int nCards)
    {
        name_  = name;
        cards_ = new ArrayList<Card>(nCards);
    }
    
    // Accessors
    public String getName()
    {
        return name_;
    }
    
    public Card getCard(int i)
    {
        return cards_.get(i);
    }
    
    // Other methods
    // Transfers amount cards from the source CardBank, beginning at firstIndex
    public void transferFrom(CardBank source, int firstIndex, int amount)
    {
        for (int i = 0; i < amount; i++) {
            add(source.discard(firstIndex));
        }
    }
    
    public void shuffle()
    {
        Random rng = new Random();
        Collections.shuffle(cards_, rng);
    }
    
    @Override
    public String toString()
    {
        return name_;
    }
    
    // ArrayList method shortcuts
    public void add(Card card)
    {
        cards_.add(card);
    }
    
    public void add(int index, Card card)
    {
        cards_.add(index, card);
    }
    
    public Card discard(int index)
    {
        return cards_.remove(index);
    }
    
    public boolean discard(Card card)
    {
        return cards_.remove(card);
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