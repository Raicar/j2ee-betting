package es.udc.isg011.apuestas.model.betoption;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("betOptionDao")
public class BetOptionDaoHibernate extends GenericDaoHibernate<BetOption, Long> implements BetOptionDao {

}
