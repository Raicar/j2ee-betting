package es.udc.isg011.apuestas.model.bet;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.virtualaccount.VirtualAccount;

@Entity
@Immutable
public class Bet {
	

	private Long betId;
	private float money;
	private Calendar date;
	private VirtualAccount virtualAccount;
	private BetOption betOption;
	
	public Bet() {
		super();
	}

	public Bet(float money, Calendar date, VirtualAccount virtualAccount, BetOption betOption) {
		super();
		this.money = money;
		this.date = date;
		this.virtualAccount = virtualAccount;
		this.betOption=betOption;
		
	}
	
	@Column(name = "betId")
	@SequenceGenerator(
	name = "BetIdGenerator", 
	sequenceName = "BetSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BetIdGenerator")
	public Long getBetId() {
		return betId;
	}
	public void setBetId(Long betId) {
		this.betId = betId;
	}
	
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	
	@Column(name = "betDate")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="virtAccId")
	public VirtualAccount getVirtualAccount() {
		return virtualAccount;
	}
	public void setVirtualAccount(VirtualAccount virtualAccount) {
		this.virtualAccount = virtualAccount;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="betOptionId")
	public BetOption getBetOption() {
		return betOption;
	}
	public void setBetOption(BetOption betOption) {
		this.betOption = betOption;
	}
	
	@Transient
	public BetStateEnum getBetState(){
		if(betOption.getWinner() == null) return BetStateEnum.NOTESTABLISHED;
		else if(betOption.getWinner() == false) return BetStateEnum.LOST;
		else return BetStateEnum.WINNER;
	}

}
