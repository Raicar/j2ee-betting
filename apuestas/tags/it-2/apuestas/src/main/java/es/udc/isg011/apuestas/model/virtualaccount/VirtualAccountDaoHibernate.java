package es.udc.isg011.apuestas.model.virtualaccount;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("virtualAccountDao")
public class VirtualAccountDaoHibernate extends
GenericDaoHibernate<VirtualAccount, Long> implements VirtualAccountDao{

}
