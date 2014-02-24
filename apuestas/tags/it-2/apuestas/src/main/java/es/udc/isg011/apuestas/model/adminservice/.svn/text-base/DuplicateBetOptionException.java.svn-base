package es.udc.isg011.apuestas.model.adminservice;

@SuppressWarnings("serial")
public class DuplicateBetOptionException extends Exception {
	private String betType;

	public DuplicateBetOptionException(String betType){
		super("BetType question exception => "+
				"betType = "+ betType);
		
		this.betType=betType;
	}
		
	public String getBetType(){
		return this.betType;
	}
}
