package es.udc.isg011.apuestas.model.betservice;

import java.util.List;

import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.category.Category;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BetService {
	 
	public Bet bet(Long betOptionId, Long userProfileId, float money) throws InsufficientBalanceException, InstanceNotFoundException, BetAfterEventStartedException;
	
	public int getNumberOfBets(Long userProfileId) throws InstanceNotFoundException;
	
	public List<Bet> obtainBets(Long userProfileId, int startIndex, int count) throws InstanceNotFoundException; 
	
	public int getNumberOfEvents(String keyWords, Long categoryId) throws InstanceNotFoundException;
	
	public List<Event> findEvents(String keyWords, Long categoryId, int startIndex, int count) throws InstanceNotFoundException;
	
	public Event obtainEvent(Long eventId) throws InstanceNotFoundException;
	
	public BetOption obtainBetOption(Long betOptionId) throws InstanceNotFoundException;
	
	public float obtainBalance(Long userProfileId) throws InstanceNotFoundException;
	
	public List<Category> obtainCategories();
}
