package es.udc.isg011.apuestas.web.pages.admin;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINS)
public class WinnerEstablished {

	@Property
	private Event event;

	private String betTypeId;
   
	public String getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(String betTypeId) {
		this.betTypeId = betTypeId;
	}

	@Property
	private BetType betType;

	@Property
	private BetOption betOption;
	
	@Inject
	private BetService betService;

	@Inject
	private AdminService adminService;

	@Property
	private List<Long> selectedBetOptionIds;
   

	void onActivate(String betTypeId) {
		this.betTypeId = betTypeId;
		Long myBetTypeId;
		try{
			myBetTypeId = Long.parseLong(betTypeId);
		} catch (NumberFormatException e) {
			betType = null;
			return;
		}
		
		try {
			betType = betService.obtainBetType(myBetTypeId);
		} catch (InstanceNotFoundException e) {
			betType = null;
			return;
		}
		
		event = betType.getEvent();
	}
   
   String onPassivate() {
	   return betTypeId;
   }

   
}

