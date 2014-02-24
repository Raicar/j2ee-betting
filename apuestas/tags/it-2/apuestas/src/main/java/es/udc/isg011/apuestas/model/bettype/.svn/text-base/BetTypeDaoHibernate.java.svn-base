package es.udc.isg011.apuestas.model.bettype;


import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("betTypeDao")
public class BetTypeDaoHibernate extends GenericDaoHibernate<BetType, Long> implements BetTypeDao {

	@Override
	public boolean existEventIdQuestion(Long eventId, String question) {
		long numberMatched = (Long) getSession().createQuery(
                "SELECT COUNT(b) FROM BetType b "+
        		"WHERE b.event.eventId = :eventId " +
        		"AND b.question LIKE :question").
        		setParameter("eventId", eventId).
        		setParameter("question", question).
        		uniqueResult();
		return (numberMatched!=0);
	}

}
