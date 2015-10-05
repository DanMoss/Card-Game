package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardBank
{
    private String          name_;
    private ArrayList<Card> cards_;
        
    // Constructors
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
    
    public int getSize()
    {
        return cards_.size();
    }
    
    // Other methods
    public void add(Card card)
    {
        cards_.add(card);
    }
    
    public Card discard(int index)
    {
        return cards_.remove(index);
    }
    
    public void transfer(CardBank source, int firstIndex, int amount)
    {
        int i;
        for (i = 0; i < amount; i++) {
            add(source.discard(firstIndex));
        }
    }
    
    public void shuffle()
    {
        Random rng = new Random();
        Collections.shuffle(cards_, rng);
    }
    
    public boolean isEmpty()
    {
        return cards_.isEmpty();
    }
}