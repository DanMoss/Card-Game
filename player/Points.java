package cardgame.player;

public class Points
{
    private int       amount_;
    private final int minAmount_;
    
    // Constructors
    public Points(int amount, int minAmount)
    {
        minAmount_ = minAmount;
        setAmount(amount);
    }
    
    // Accessors
    public int getAmount()
    {
        return amount_;
    }
    
    private void setAmount(int amount)
    {  
        amount_ = (amount < minAmount_) ? minAmount_ : amount;
    }
    
    // Other methods
    public void add(int amount)
    {
        int total = getAmount() + amount;
        setAmount(total);
    }
}