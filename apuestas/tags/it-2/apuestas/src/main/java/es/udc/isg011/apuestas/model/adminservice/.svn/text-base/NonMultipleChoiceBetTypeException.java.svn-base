package es.udc.isg011.apuestas.model.adminservice;

@SuppressWarnings("serial")
public class NonMultipleChoiceBetTypeException extends Exception {

    private Long  betTypeId;

    public NonMultipleChoiceBetTypeException(Long  betTypeId) {
        super("Non multiple choice betType exception => BetType "+betTypeId+" doesn't accept more than one betOption as winner.");
        this.betTypeId = betTypeId;
    }

    public Long getBetTypeId() {
        return betTypeId;
    }
}
