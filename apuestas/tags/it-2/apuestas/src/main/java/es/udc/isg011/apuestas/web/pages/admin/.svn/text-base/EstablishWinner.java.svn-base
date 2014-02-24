package es.udc.isg011.apuestas.web.pages.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checklist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.AbstractSelectModel;

import es.udc.isg011.apuestas.model.adminservice.AdminService;
import es.udc.isg011.apuestas.model.adminservice.MismatchedBetTypeBetOptionException;
import es.udc.isg011.apuestas.model.adminservice.NonMultipleChoiceBetTypeException;
import es.udc.isg011.apuestas.model.adminservice.WinnerAlreadySetException;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicy;
import es.udc.isg011.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINS)
public class EstablishWinner {

	@Property
	private Event event;

	private String betTypeId;
   
	@Property
	private BetType betType;    
   
	@Inject
	private BetService betService;

	@Inject
	private AdminService adminService;

	@Property
	private List<Long> selectedBetOptionIds;
   
	@Component
	private Form establishWinnerForm;
   
	@Inject
	private Messages messages;

	@Component
	private Checklist checklist;

	@InjectPage
	private WinnerEstablished winnerEstablished;
	
	public SelectModel getBetOptionIdsModel() {
	
	    final List<OptionModel> options = new ArrayList<OptionModel>();
	
	    for(BetOption betOpt : betOptions) {
	
	        options.add(new OptionModelImpl(betOpt.getOption(), betOpt.getBetOptionId()));
	    }
	
	    return new AbstractSelectModel() {
	
	        public List<OptionModel> getOptions() {
	            return options;
	        }
	
	        public List<OptionGroupModel> getOptionGroups() {
	            return null;
	        }
	    };
	}
   
	public ValueEncoder<Long> getBetOptionIdsEncoder(){
	    return new ValueEncoder<Long>() {

	        public String toClient(Long value) {
	            return value.toString();
	        }

	        public Long toValue(String clientValue) {
	            return Long.parseLong(clientValue);
	        }
	    };
	}
	public boolean isStarted(){
      return (event.getDate().before(Calendar.getInstance()));
   }

    @Property
    private List<BetOption> betOptions;


    void onValidateFromEstablishWinnerForm() {
		if(selectedBetOptionIds.size()==0){
    		establishWinnerForm.recordError(checklist, messages.format("error-minimumOneSelected"));
    		return;
		}

		try {
			adminService.establishWinner(betType.getBetTypeId(), selectedBetOptionIds);
		} catch (InstanceNotFoundException e) {
    		establishWinnerForm.recordError(checklist, messages.format("error-betTypeNotFound",betTypeId));
		} catch (MismatchedBetTypeBetOptionException e) {
    		establishWinnerForm.recordError(checklist, messages.format("error-betTypeMismatchedBetOption"));
		} catch (NonMultipleChoiceBetTypeException e) {
    		establishWinnerForm.recordError(checklist, messages.format("error-nonMultipleChoiceBetType"));
		} catch (WinnerAlreadySetException e) {
    		establishWinnerForm.recordError(checklist, messages.format("error-winnerAlreadySet"));
		}

    }

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
		
		betOptions = betType.getBetOptions();
   }
   
   String onPassivate() {
	   return betTypeId;
   }

	Object onSuccess(){
		winnerEstablished.setBetTypeId(betTypeId);
		return winnerEstablished;
	}
   
}

