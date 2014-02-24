package es.udc.isg011.apuestas.model.adminservice;

import java.util.List;

import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.event.Event;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface AdminService {
	
	public Event addEvent(Event event, Long categoryId ) throws InstanceNotFoundException, InvalidDateOfEventException ;
	
	public BetType addBetType(Long eventId, BetType betType) throws InstanceNotFoundException, 
	WinnerAlreadySetException, BetTypeWithEmptyBetOptionsListException, DuplicateBetTypeQuestionException, DuplicateBetOptionException, EventStartedException; 
	
	public void establishWinner(Long betTypeId, List<Long> winnerOptionIdList) throws InstanceNotFoundException, 
	MismatchedBetTypeBetOptionException, NonMultipleChoiceBetTypeException, WinnerAlreadySetException;
	
	public List<Event> findEvents(String keyWords, Long categoryId, int startIndex, int count) throws InstanceNotFoundException;
	
	public int getNumberOfEvents(String keyWords, Long categoryId) throws InstanceNotFoundException;

}
