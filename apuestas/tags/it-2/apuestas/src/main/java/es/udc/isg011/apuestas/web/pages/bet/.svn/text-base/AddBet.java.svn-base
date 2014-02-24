package es.udc.isg011.apuestas.web.pages.bet;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.RadioGroup;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betservice.BetAfterEventStartedException;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.betservice.IncorrectBalanceException;
import es.udc.isg011.apuestas.model.betservice.InsufficientBalanceException;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class AddBet {
	
	private String betTypeId;
	private float amountAsFloat;
	
	@Property
	private Bet bet;
	@Property
	private BetOption betOption;
	@Property
	private BetType betType;
	@Property
	private List<BetOption> betOptions;
	@Property
	private BetOption selectedBetOption;
	@Property
	private String amount;
	

	@Component
	private Form betForm;
	
	@Component(id="amount")
	private TextField amountField;
	@Component(id="betOptionRadioGroup")
	private RadioGroup radioGroup;
	
	
	@Inject
	private BetService betService;
	@Inject
	private Locale locale;
	@Inject
	private Messages messages;
	@Inject
	private Request request;
	
	@SessionState(create=false)
    @Property
    private UserSession userSession;	
	
	@InjectPage
	private BetDone betDone;
	
	@InjectComponent
	private Zone formZone;
	
		
	public Format getNumberFormat() {
		return NumberFormat.getInstance(locale);
	}
	
	public DateFormat getDateFormat(){
		return DateFormat.getDateInstance(DateFormat.FULL, locale);
	}
	
	public DateFormat getHourFormat() {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
	}
	
	public String getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(String betTypeId) {
		this.betTypeId = betTypeId;
	}
	
	String onPassivate(){
		return betTypeId;
	}
	
	void onActivate(String betTypeId){
		Long id;
		this.betTypeId = betTypeId;
		try {
			id = Long.parseLong(betTypeId);
		} catch (NumberFormatException e) {
			betType = null;
			return;
		}
		
		try {
			betType = betService.obtainBetType(id);
		} catch (InstanceNotFoundException e) {
			betType = null;
			return;
		}
		this.betOptions = betType.getBetOptions();
	}

	
	public boolean isNotBetEmpty(){
		return bet == null ? false : true;
	}
	
	void onValidateFromBetForm(){
		
		if (!betForm.isValid())
			return;
		
		if (selectedBetOption==null){
			betForm.recordError(radioGroup, messages.format("error-instanceNotFound"));
			return;
		}
		
		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = numberFormatter.parse(amount, position);
		
		if (position.getIndex() != amount.length()) {
			betForm.recordError(amountField, messages.format(
					"error-incorrectNumberFormat", amount));
			return;
		} else {
			amountAsFloat = number.floatValue();
		}
		
		
		try {
			bet = betService.bet(selectedBetOption.getBetOptionId(), userSession.getUserProfileId(), amountAsFloat);
		} catch (InstanceNotFoundException e) {
			betForm.recordError(radioGroup, messages.format("error-instanceNotFound"));
		} catch (InsufficientBalanceException e) {
			betForm.recordError(amountField, messages.format("error-insufficientBalance"));
		} catch (BetAfterEventStartedException e) {
			betForm.recordError(radioGroup, messages.format("error-betAfterEventStarted"));
		} catch (IncorrectBalanceException e){
			betForm.recordError(amountField, messages.format("error-incorrectBalance"));
		}
		
	}
		
	Object onSuccess() {	
		betDone.setBetId(bet.getBetId().toString());
		amount="";
		selectedBetOption=null;
		return request.isXHR() ? formZone.getBody() : betDone;
	}

	Object onFailure() {
		return request.isXHR() ? formZone.getBody() : null;
	}
	
	
	@Property
	private ValueEncoder<BetOption> betOptionEncoder = new ValueEncoder<BetOption> () {
	        public String toClient (BetOption betOption) {
	                return String.valueOf(betOptions.indexOf(betOption));
	        }
	        public BetOption toValue (String pId) {
	        	if (pId==null) 
	        		return null;
	        	return (betOptions.get(Integer.parseInt(pId)));
	        }
		
	}; 
	
	public boolean isStarted(){
		return betType.getEvent().getDate().before(Calendar.getInstance());
	}
	
	
	
}
