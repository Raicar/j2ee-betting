package es.udc.isg011.apuestas.web.pages.bet;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.bet.BetStateEnum;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betservice.BetNotBelongUserException;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.USERS)
public class DetailsBet {
	
	@Property
	private Bet bet;
	
	@Property
	private BetOption betOption;
	
	@Property
	private BetType betType;
	
	@Property
	private Event event;
	
	private String betId;
	
	@Inject
    private BetService betService;

	@Inject
	private Locale locale;
	
	@SessionState(create=false)
    private UserSession userSession;
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.FULL, locale);
	}
	
	public DateFormat getHourFormat() {
		return DateFormat.getTimeInstance(DateFormat.FULL, locale);
	}
	
	public boolean isBetNotEstablished(){
		return (bet.getBetState()==BetStateEnum.NOTESTABLISHED);
	}
	
	public boolean isBetWinner(){
		return (bet.getBetState()==BetStateEnum.WINNER);
	}
	
	public boolean isBetLost(){
		return (bet.getBetState()==BetStateEnum.LOST);
	}
	
	public Format getNumberFormat(){
		   return NumberFormat.getInstance(locale);
		}

	void onActivate(String betId) {
		Long myBetId;
		this.betId=betId;
		try{
			myBetId = Long.parseLong(betId);
		} catch (NumberFormatException e) {
			bet = null;
			return;
		}
		try {
			bet = betService.obtainBet(myBetId, userSession.getUserProfileId());
			betOption=bet.getBetOption();
			betType=betOption.getBetType();
			event=betType.getEvent();
		} catch (InstanceNotFoundException e) {
			bet = null;
			return;
		} catch (BetNotBelongUserException e) {
			bet = null;
			return;
		}
	}
	
	String onPassivate() {
		return betId;
	}

	public String getBetId() {
		return betId;
	}

	public void setBetId(String betId) {
		this.betId = betId;
	}
	
	public boolean isWinners(){
		return !betType.getWinners().isEmpty();
	}
	
	public boolean isStarted(){
		return event.getDate().before(Calendar.getInstance());
	}
}