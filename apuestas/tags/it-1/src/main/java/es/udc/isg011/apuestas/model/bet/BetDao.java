package es.udc.isg011.apuestas.model.bet;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface BetDao extends GenericDao<Bet, Long> {

	public int getNumberOfBets(Long userProfileId);
	

	public List<Bet> obtainBets(Long userProfileId, int startIndex, int count);

	public int getNumberOfBetsByBetOptionId(Long betOptionId);
	
	public List<Bet> findBetsByBetOptionId(Long betOptionId, int startIndex, int count);

}
