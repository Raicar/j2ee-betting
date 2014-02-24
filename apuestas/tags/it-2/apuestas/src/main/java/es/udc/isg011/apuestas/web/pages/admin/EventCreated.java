package es.udc.isg011.apuestas.web.pages.admin;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINS)
public class EventCreated {

	@Property
	private Event event;
	
	@Inject
	private BetService betService;
	
	@Inject
	private Locale locale;
	
	private String eventId;
	
	
	public DateFormat getDateFormat(){
		return DateFormat.getDateInstance(DateFormat.FULL, locale);
	}
	
	public DateFormat getHourFormat() {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
	}
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	
	void onActivate(String eventId) {
		Long myEventId;
		this.eventId=eventId;
		try{
			myEventId = Long.parseLong(eventId);
		} catch (NumberFormatException e) {
			event = null;
			return;
		}
		try {
			event = betService.obtainEvent(myEventId);
		} catch (InstanceNotFoundException e) {
			event = null;
			return;
		}
	}

	String onPassivate() {
		return eventId;
	}

}
