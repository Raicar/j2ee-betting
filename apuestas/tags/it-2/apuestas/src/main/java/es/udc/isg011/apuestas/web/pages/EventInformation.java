package es.udc.isg011.apuestas.web.pages;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.web.pages.admin.AddBetType;
import es.udc.isg011.apuestas.web.pages.admin.NoJavascriptAddBetType;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class EventInformation {
	
	@Property
	private Event event;

	@Property
	private BetType betType;
	
    private String eventId;
    
    @Inject
    private BetService betService;

    @SessionState(create=false)
    @Property
    private UserSession userSession;
    
    @Property
    private List<BetType> betTypes;
    
	@Inject
	private Locale locale;
	
	@Inject
	private Request request;
	
	@InjectPage
	private NoJavascriptAddBetType noJavascriptAddBetType;
	
	@InjectPage
	private AddBetType addBetType;
		
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.FULL, locale);
	}
	public DateFormat getHourFormat() {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
	}

	public boolean isStarted(){
		return event.getDate().before(Calendar.getInstance());
	}
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@InjectComponent
	private Zone zone;	
	Object onAddBetType() {
		if(request.isXHR()){
			addBetType.setEventId(eventId);
			addBetType.setNewTime(true);
			return addBetType;
		}else{
			BetType a = new BetType();
			noJavascriptAddBetType.setBetType(a);
			noJavascriptAddBetType.setEventId(eventId);
			return noJavascriptAddBetType;
		}
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
		betTypes = event.getBetTypes();

	}
	
	String onPassivate() {
		return eventId;
	}
	

}
