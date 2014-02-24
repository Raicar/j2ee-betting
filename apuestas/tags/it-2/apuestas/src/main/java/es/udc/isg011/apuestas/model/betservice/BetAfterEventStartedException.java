package es.udc.isg011.apuestas.model.betservice;

import java.util.Calendar;

@SuppressWarnings("serial")
public class BetAfterEventStartedException extends Exception {
	private Long eventId;
    private Calendar eventDate;
    private Calendar currentDate;

    public BetAfterEventStartedException(Long eventId, Calendar eventDate, Calendar currentDate) {
        
        super("Bet After Event Started => " +
            "eventId = " + eventId + " | " +
            "eventDate = " + eventDate + " | " +
            "currentDate = " + currentDate);
            
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.currentDate = currentDate;
        
    }
    
    public Long getEventId() {
        return eventId;
    }
    
    public Calendar getEventDate() {
        return eventDate;
    }
    
    public Calendar getCurrentDate() {
        return currentDate;
    }

}
