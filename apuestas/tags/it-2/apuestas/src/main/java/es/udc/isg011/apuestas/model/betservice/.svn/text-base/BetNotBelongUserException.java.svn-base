package es.udc.isg011.apuestas.model.betservice;

@SuppressWarnings("serial")
public class BetNotBelongUserException extends Exception {
	private Long betId;
    private Long userProfileId;

    public BetNotBelongUserException(Long betId, Long userProfileId) {
        
        super("Bet Not Belong User => " +
            "bettId = " + betId + " | " +
            "userProfileID = " + userProfileId);
            
        this.betId = betId;
        this.userProfileId = userProfileId;
        
    }
    
    public Long getBetId() {
        return betId;
    }
    
    public Long getUserProfileId() {
        return userProfileId;
    }

}
