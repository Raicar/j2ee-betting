package es.udc.isg011.apuestas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.isg011.apuestas.model.bet.Bet;
import es.udc.isg011.apuestas.model.betservice.BetService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class BetsGridDataSource implements GridDataSource{
	private List<Bet> bets;
	private int totalNumberOfBets;
	private int startIndex;
	private Long userProfileId;
	private BetService betService;
	
	public BetsGridDataSource(Long userProfileId,BetService betService) {
		this.userProfileId=userProfileId;
		this.betService=betService;
		try {
			this.totalNumberOfBets=betService.getNumberOfBets(userProfileId);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public Class<Bet> getRowType() {
        return Bet.class;
    }
	
	public Object getRowValue(int index) {
	    return bets.get(index-this.startIndex);
	}
	
	public void prepare(int startIndex, int endIndex,
	    	List<SortConstraint> sortConstraints) {
		try {
			bets=betService.obtainBets(userProfileId, startIndex, endIndex-startIndex+1);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
		}
		this.startIndex = startIndex;
	}

	@Override
	public int getAvailableRows() {
		return totalNumberOfBets;
	}
}
