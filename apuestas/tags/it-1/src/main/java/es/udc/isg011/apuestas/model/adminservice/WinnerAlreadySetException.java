package es.udc.isg011.apuestas.model.adminservice;

@SuppressWarnings("serial")
public class WinnerAlreadySetException extends Exception {

    private boolean winnerValue;

    public WinnerAlreadySetException(boolean winnerValue) {
        super("Winner already set exception => betOption.winner = " + winnerValue+ ". Must be null.");
        this.winnerValue = winnerValue;
    }

    public boolean getWinnerValue() {
        return winnerValue;
    }
}
