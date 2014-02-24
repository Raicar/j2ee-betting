package es.udc.isg011.apuestas.model.betoption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;

import es.udc.isg011.apuestas.model.bettype.BetType;
import javax.persistence.*;

@Entity
public class BetOption {
	private Long betOptionId;
	private String option;
	private float rateProfit;
	private Boolean winner;
	private BetType betType;
	private long version;
		
	public BetOption() {
		super();
	}

	public BetOption(String option, float rateProfit) {
		super();
		this.option = option;
		this.rateProfit = rateProfit;
		this.winner = null;
	}
	
	@Column(name = "betOptionId")
	@SequenceGenerator(
	name = "BetOptionIdGenerator", 
	sequenceName = "BetOptionSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BetOptionIdGenerator")
	public Long getBetOptionId() {
		return betOptionId;
	}
	public void setBetOptionId(Long betOptionId) {
		this.betOptionId = betOptionId;
	}
	@Column(name = "betAnswer")
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	
	public float getRateProfit() {
		return rateProfit;
	}
	public void setRateProfit(float rateProfit) {
		this.rateProfit = rateProfit;
	}
	
	public Boolean getWinner() {
		return winner;
	}
	public void setWinner(Boolean winner) {
		this.winner = winner;
	}
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="betTypeId")
	public BetType getBetType() {
		return betType;
	}
	public void setBetType(BetType betType) {
		this.betType = betType;
	}
	
	@Version
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	

}
