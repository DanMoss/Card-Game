package cardgame.acestokings.melds;

import cardgame.Card;

class AbstractMeld<Type>
{
    public static final int MELD_PLAY_SIZE = 3;
    
    private Type      meldType_;
    private Card.Face roundFace_;
    
    private AbstractMeld(Type meldType, Card.Face roundFace)
    {
        meldType_  = meldType;
        roundFace_ = roundFace;
    }
    
    public Type getMeldType()
    {
        return meldType_;
    }
    
    public static void main(String[] args)
    {
        AbstractMeld<Card.Face> test = new AbstractMeld<Card.Face>(Card.Face.ACE, Card.Face.ACE);
    }
}