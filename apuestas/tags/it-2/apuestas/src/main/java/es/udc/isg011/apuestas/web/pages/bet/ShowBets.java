package es.udc.isg011.apuestas.web.pages.bet;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betservice.BetNotBelongUserException;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.userservice.UserService;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.isg011.apuestas.web.util.BetsGridDataSource;
import es.udc.isg011.apuestas.web.util.UserSession;
import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.bet.BetStateEnum;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.USERS)
public class ShowBets {
	private final static int ROWS_PER_PAGE = 10;
	
	@Property
	private Bet bet;
	
	@Inject
	private BetService betService;
	
	@Inject
	private UserService userService;
	
	@SessionState(create=false)
    private UserSession userSession;

	@Property
	private BetsGridDataSource betsGridDataSource;

	@Property
	private BetOption betOption;
	
	@Inject
	private Request request;

	@InjectComponent
	private Zone thingsZone;
	
	@InjectPage
	private DetailsBet detailsBet;
	
	@Property
	private boolean visibleZone=false;
	
	@Property
	private Bet betToShow;
	
	@Inject
	private Locale locale;
	
	private boolean newSearch=false;
	
	@InjectComponent
	private Grid grid;
	
	public boolean getNewSearch() {
		return newSearch;
	}

	public void setNewSearch(boolean newSearch) {
		this.newSearch = newSearch;
	}
	
	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
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
	
	public boolean isBetToShowNotEstablished(){
		return (betToShow.getBetState()==BetStateEnum.NOTESTABLISHED);
	}
	
	public boolean isBetToShowWinner(){
		return (betToShow.getBetState()==BetStateEnum.WINNER);
	}
	
	public boolean isBetToShowLost(){
		return (betToShow.getBetState()==BetStateEnum.LOST);
	}
	

	public String getWinners(Long id){
		Bet bet;
		try {
			bet = betService.obtainBet(id, userSession.getUserProfileId());
			String result="";
			for(BetOption bOp:bet.getBetOption().getBetType().getWinners()){
				result+=bOp + " - ";
			}
			if(result.length()>0) return result.substring(0, result.length()-3);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (BetNotBelongUserException e) {
			// TODO Auto-generated catch block
		}
		return "";
	}
	
	public Format getNumberFormat(){
	   return NumberFormat.getInstance(locale);
	}
	
	public Format getDateFormat(){
		return DateFormat.getDateInstance(DateFormat.FULL,locale);
	}
	
	public Format getBetDateFormat(){
		return DateFormat.getDateInstance(DateFormat.SHORT,locale);
	}
	
	public DateFormat getHourFormat() {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
	}

	Object onShowThings(String betId) {
		visibleZone=true;
		detailsBet.setBetId(betId);
		try {
			betToShow = betService.obtainBet(Long.parseLong(betId), userSession.getUserProfileId());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (BetNotBelongUserException e) {
			// TODO Auto-generated catch block
		}
		return request.isXHR() ? thingsZone.getBody() :detailsBet;
	}
	
	public boolean isVisibleZone(){
		return visibleZone;
	}
	
	Object onActionFromHideZone(){
		 visibleZone=false;
		return request.isXHR() ? thingsZone.getBody() :null;
	}
	
	public boolean isWinners(){
		return !betToShow.getBetOption().getBetType().getWinners().isEmpty();
	}
	
	public boolean isStarted(){
		return betToShow.getBetOption().getBetType().getEvent().getDate().before(Calendar.getInstance());
	}
	
	void onActivate(boolean newSearch){
		Long userProfileId = userSession.getUserProfileId();
		betsGridDataSource=new BetsGridDataSource(userProfileId,betService);
		
		this.newSearch=newSearch;
		if(newSearch){
			this.newSearch=false;
			grid.setCurrentPage(1);
		}		
	}
	
	Object[] onPassivate() {
		return new Object[] {newSearch};
	}

}
