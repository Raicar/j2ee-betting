package es.udc.isg011.apuestas.model.betservice;

@SuppressWarnings("serial")
public class InsufficientBalanceException extends Exception {
    private long virtualAccountId;
    private double currentBalance;
    private double amountToBet;

    public InsufficientBalanceException(long virtualAccountId,
        double currentBalance, double amountToBet) {
        
        super("Insufficient balance exception => " +
            "virtualAccountId = " + virtualAccountId + " | " +
            "currentBalance = " + currentBalance + " | " +
            "amountToBet = " + amountToBet);
            
        this.virtualAccountId = virtualAccountId;
        this.currentBalance = currentBalance;
        this.amountToBet = amountToBet;
        
    }
    
    public long getVirtualAccountId() {
        return virtualAccountId;
    }
    
    public double getCurrentBalance() {
        return currentBalance;
    }
    
    public double getAmountToBet() {
        return amountToBet;
    }
}
