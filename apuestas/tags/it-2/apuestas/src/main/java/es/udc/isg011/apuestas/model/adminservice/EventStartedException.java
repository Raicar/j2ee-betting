package es.udc.isg011.apuestas.model.adminservice;

@SuppressWarnings("serial")
public class EventStartedException extends Exception {
	private String event;

	public EventStartedException(String event){
		super("Event started exception => "+
				"event = "+ event);
		
		this.event=event;
	}
		
	public String getEvent(){
		return this.event;
	}
}
