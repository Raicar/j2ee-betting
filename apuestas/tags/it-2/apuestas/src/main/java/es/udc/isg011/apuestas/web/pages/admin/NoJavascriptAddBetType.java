package es.udc.isg011.apuestas.web.pages.admin;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
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
public class NoJavascriptAddBetType {


	private String eventId;	

	@Property
	private String question;
	
	@Property
	private boolean multipleWinner;

	@Property
	private boolean finish;	
	
	@Property
	private Event event;
	
    @Inject
    private Messages messages;
	@Inject
	private BetService betService;
	
	@Persist
	private BetType betType;
	
	@Property
	private String option;
	
	@Property
	private String rateProfit;
	
	@Property 
	private BetOption betOption;

	
	@Component(id="option")
	private TextField optionField;
	
	@Component(id="rateProfit")
	private TextField rateProfitField;

	@Component
	private Form addBetTypeForm;
	
	@Component(id="question")
	private TextField questionField;
	
	@Inject
	private AdminService adminService;

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
	
	public BetType getBetType() {
		return betType;
	}

	public void setBetType(BetType betType) {
		this.betType = betType;
	}

	public boolean isNotStarted(){
	      return (event.getDate().after(Calendar.getInstance()));
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.FULL, locale);
	}
	
	public DateFormat getHourFormat() {
		return DateFormat.getTimeInstance(DateFormat.FULL, locale);
	}

	@Property
	private ValueEncoder<BetOption> betOptionEncoder = new ValueEncoder<BetOption> () {
	        public String toClient (BetOption betOption) {
	                return String.valueOf(betType.getBetOptions().indexOf(betOption));
	        }
	        public BetOption toValue (String pId) {
	        	if (pId==null) 
	        		return null;
	        	return (betType.getBetOptions().get(Integer.parseInt(pId)));
	        }
	}; 
	
    void onValidateFromAddBetTypeForm() {
    	
    	if (!addBetTypeForm.isValid())
			return;
    	
    	if(betType.getQuestion()!=question) betType.setQuestion(question);
    	if(betType.getMultipleWinner()!=multipleWinner) betType.setMultipleWinner(multipleWinner);
    	
		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		Float rateProfitFloat;
    	ParsePosition position = new ParsePosition(0);
    	Number number = numberFormatter.parse(rateProfit, position);
		if (position.getIndex() != rateProfit.length()) {
			addBetTypeForm.recordError(rateProfitField, messages.format("error-incorrectNumberFormat", rateProfit));
			return;
		} else {
			rateProfitFloat = number.floatValue();
		}
		for (BetOption betOption : betType.getBetOptions()) {
			if(betOption.getOption().equals(option)){
	    		addBetTypeForm.recordError(optionField, messages.format("error-duplicateBetOption"));
	    		return;
			}
		}
		BetOption betOption = new BetOption(option, rateProfitFloat);
		betType.addBetOption(betOption);
		if(finish){	
	    	try {
				adminService.addBetType(event.getEventId(), betType);
				return;//EVERYTHING OK
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
	    	
	    	
	    	//IN THE CASE OF AN EXCEPTION: remove that betOption, in order to keep the same state and not ask for a new one
    		List<BetOption>a = betType.getBetOptions();
    		a.remove(betOption);
    		betType.setBetOptions(a);

		}
    }
    
    Object onSuccess() {
    	if(finish){
        	eventInformation.setEventId(eventId);
            return eventInformation;	
    	}else{
    		return this;
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
		question = betType.getQuestion();
		multipleWinner = betType.getMultipleWinner();
	}
	
	String onPassivate() {
		return eventId;
	}

}