package es.udc.isg011.apuestas.model.adminservice;

@SuppressWarnings("serial")
public class BetTypeWithEmptyBetOptionsListException extends Exception {
	private String betType;

	public BetTypeWithEmptyBetOptionsListException(String betType){
		super("BetType with empty betOptions  list exception => "+
				"betType = "+ betType);
		
		this.betType=betType;
	}
		
	public String getBetType(){
		return this.betType;
	}
}
