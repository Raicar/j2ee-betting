package es.udc.isg011.apuestas.model.bet;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("betDao")
public class BetDaoHibernate extends GenericDaoHibernate<Bet, Long> implements
		BetDao {

	@Override
	public int getNumberOfBets(Long userProfileId) {
		
		long numberOfBets = (Long) getSession().createQuery(
				"SELECT Count(b) FROM Bet b JOIN b.virtualAccount v JOIN v.userProfile u " + 
				"WHERE u.userProfileId = :userProfileId").
				setParameter("userProfileId", userProfileId).
				uniqueResult();

		return (int) numberOfBets; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bet> obtainBets(Long userProfileId, int startIndex, int count) {
		return getSession().createQuery(
				"SELECT b FROM Bet b JOIN b.virtualAccount v JOIN v.userProfile u " +
				"WHERE u.userProfileId = :userProfileId " +
				"ORDER BY b.date DESC").
				setParameter("userProfileId", userProfileId).
				setFirstResult(startIndex).
				setMaxResults(count).list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Bet> findBetsByBetOptionId(Long betOptionId, int startIndex, int count) {
		List<Bet> list=(List<Bet>)getSession().createQuery(
    			"SELECT b FROM Bet b WHERE b.betOption.betOptionId = :betOptionId")
    			.setParameter("betOptionId", betOptionId)
				.setFirstResult(startIndex)
				.setMaxResults(count).list();
		return list;
	}
	
}
