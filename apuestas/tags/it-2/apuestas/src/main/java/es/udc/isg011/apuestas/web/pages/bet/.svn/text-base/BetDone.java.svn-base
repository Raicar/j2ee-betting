package es.udc.isg011.apuestas.web.pages.bet;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.betservice.BetNotBelongUserException;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.USERS)
public class BetDone {
	
	private String betId;
	
	@Property
	private Bet bet;
	
	@Inject
	private BetService betService;
	@Inject
	private Locale locale;
	
	@SessionState(create=false)
    private UserSession userSession;	
	
	public String getBetId() {
		return betId;
	}

	public void setBetId(String betId) {
		this.betId = betId;
	}

	public Format getNumberFormat() {
		return NumberFormat.getInstance(locale);
	}

	String onPassivate(){
		return betId;
	}
	
	void onActivate(String betId){
		Long id;
		this.betId = betId;
		try {
			id = Long.parseLong(betId);
		} catch (NumberFormatException e) {
			bet = null;
			return;
		}
		
		try {
			bet = betService.obtainBet(id, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			bet = null;
			return;
		} catch (BetNotBelongUserException e) {
			bet = null;
			return;
		}
		
	}
	
}
