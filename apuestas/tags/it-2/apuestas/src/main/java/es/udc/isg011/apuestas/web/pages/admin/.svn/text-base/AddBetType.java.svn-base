package es.udc.isg011.apuestas.web.pages.admin;


import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.adminservice.BetTypeWithEmptyBetOptionsListException;
import es.udc.isg011.apuestas.model.adminservice.DuplicateBetOptionException;
import es.udc.isg011.apuestas.model.adminservice.DuplicateBetTypeQuestionException;
import es.udc.isg011.apuestas.model.adminservice.EventStartedException;
import es.udc.isg011.apuestas.model.adminservice.WinnerAlreadySetException;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.web.pages.EventInformation;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINS)
public class AddBetType {

	private String eventId;

	@Property
	private String question;

	@Property
	private String option;
	
	@Property
	private String rateProfit;
	
	@Property
	private boolean multipleWinner;
	
	@Property
	private Event event;
	
	@Component
	private Form addBetTypeForm;
	@Component(id="question")
	private TextField questionField;
	@Component(id="option")
	private TextField optionField;
	@Component(id="rateProfit")
	private TextField rateProfitField;

	@Inject
	private BetService betService;
	
	@Inject
	private AdminService adminService;

    @Inject
    private Messages messages;
	
	@InjectPage
	private EventInformation eventInformation;
	
	@Inject
	private Locale locale;
		
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	

    private Boolean newTime;
    
	public Boolean getNewTime() {
		return newTime;
	}

	public void setNewTime(Boolean newTime) {
		this.newTime = newTime;
	}
    
	/********AJAXFORMLOOP**************/

	@Property
	private IdOptionRate idOptionRate;
	
	@Property
	private List<IdOptionRate> idOptionRates = new ArrayList<IdOptionRate>();

	public static class IdOptionRate{
		private int id;
		private String option;
		private String rateProfit;
		private static int count = 1; 
		
		public IdOptionRate(){
			setId(count);
			count++;
		}

		public String getOption(){
			return option;
		}
		public void setOption(String option){
			this.option=option;
		}
		public String getRateProfit(){
			return rateProfit;
		}
		public void setRateProfit(String rateProfit){
			this.rateProfit=rateProfit;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
	}
	IdOptionRate onAddRow() {
		IdOptionRate a =new IdOptionRate();
		idOptionRates.add(a);
		return a;
	}

	void onRemoveRow(IdOptionRate idOptionRate) {
		idOptionRates.remove(idOptionRate);
	}
	
    public ValueEncoder<IdOptionRate> getBetOptionEncoder() {
    	return new ValueEncoder<IdOptionRate>(){
			public String toClient(IdOptionRate idOptionRate) {
				return Integer.toString(idOptionRate.getId());
			}
	
			public IdOptionRate toValue(String id) {
				int i = Integer.parseInt(id);
				for (IdOptionRate idOptionRate : idOptionRates) {
					if(idOptionRate.getId()==i) return idOptionRate;
				}
				return null;
			}
    	};
    }


	/******************************/
	
	public boolean isNotStarted(){
	      return (event.getDate().after(Calendar.getInstance()));
	}
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.FULL, locale);
	}
	public DateFormat getHourFormat() {
		return DateFormat.getTimeInstance(DateFormat.FULL, locale);
	}

    void onValidateFromAddBetTypeForm() {
    	
    	if (!addBetTypeForm.isValid())
			return;
    	
    	BetType betType = new BetType(question, multipleWinner);

		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position;
		Number number;
		Float rateProfit;
    	
    	
    	BetOption betOption;
    	for (IdOptionRate idOptionRate : idOptionRates) {
    		position = new ParsePosition(0);
    		number = numberFormatter.parse(idOptionRate.getRateProfit(), position);
    		if (position.getIndex() != idOptionRate.getRateProfit().length()) {
    			addBetTypeForm.recordError(rateProfitField, messages.format("error-incorrectNumberFormat", idOptionRate.getRateProfit()));
    			return;
    		} else {
    			rateProfit = number.floatValue();
    		}
			betOption = new BetOption(idOptionRate.getOption(), rateProfit);
			betType.addBetOption(betOption);
		}
    	try {
			adminService.addBetType(event.getEventId(), betType);
		} catch (WinnerAlreadySetException e) {
			//Never occurs, just created objects
		} catch (BetTypeWithEmptyBetOptionsListException e) {
    		addBetTypeForm.recordError(optionField, messages.format("error-emptyBetOptionsList"));
		} catch (InstanceNotFoundException e) {
			//Never occurs: checked event on activate
		} catch (DuplicateBetTypeQuestionException e) {
    		addBetTypeForm.recordError(questionField, messages.format("error-duplicateBetTypeQuestion"));
		} catch (DuplicateBetOptionException e) {
    		addBetTypeForm.recordError(optionField, messages.format("error-duplicateBetOption"));
		} catch (EventStartedException e) {
			//Never occurs: checked on tml, no chance to submit
		}
		

    }
    

	void onActivate(String eventId,boolean newTime) {

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
		if(newTime==true){
			idOptionRates.clear();
		}
		this.newTime=false;


	}
	
	Object[] onPassivate() {
		return new Object[] {eventId, newTime};
	}
    Object onSuccess() {	
    	eventInformation.setEventId(eventId);
        return eventInformation;
    }
	
	
}
