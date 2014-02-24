package es.udc.isg011.apuestas.model.adminservice;

@SuppressWarnings("serial")
public class MismatchedBetTypeBetOptionException extends Exception {
	private long betType;
	private long betOption;

	public MismatchedBetTypeBetOptionException(long betType, long betOption){
		super("Mismatched betType betOption exception => "+
				"betType = "+ betType+ " | " +
				"betOption = "+ betOption);
		
		this.betType=betType;
		this.betOption=betOption;
	}
	
	public long getBetType(){
		return this.betType;
	}
	
	public long getBetOption(){
		return this.betOption;
	}
}
