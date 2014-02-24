package es.udc.isg011.apuestas.model.bettype;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;



import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.event.Event;

@Entity
public class BetType {
	private Long betTypeId;
	private String question;
	private boolean multipleWinner;
	private Event event;
	private List<BetOption> betOptions = new ArrayList<BetOption>();
	private boolean establishedWinner;
	private long version;

	public BetType() {
		super();
	}
	
	
	public BetType(String question, boolean multipleWinner) {
		super();
		this.question = question;
		this.multipleWinner = multipleWinner;
		this.establishedWinner = false;
	}


	@Column(name = "betTypeId")
	@SequenceGenerator(
	name = "BetTypeIdGenerator", 
	sequenceName = "BetTypeSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BetTypeIdGenerator")
	public Long getBetTypeId() {
		return betTypeId;
	}
	public void setBetTypeId(Long betTypeId) {
		this.betTypeId = betTypeId;
	}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	@Column(name = "multChoice")
	public boolean getMultipleWinner() {
		return multipleWinner;
	}
	public void setMultipleWinner(boolean multipleChoice) {
		this.multipleWinner = multipleChoice;
	}
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="eventId")
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	@OneToMany(mappedBy="betType")
	public List<BetOption> getBetOptions() {
		return betOptions;
	}
	public void setBetOptions(List<BetOption> betOptions) {
		this.betOptions = betOptions;
	}

	@Column(name = "estWinner")
	public boolean getEstablishedWinner() {
		return establishedWinner;
	}
	public void setEstablishedWinner(boolean establishedWinner) {
		this.establishedWinner = establishedWinner;
	}
	
	public void addBetOption(BetOption betOption) {
		this.betOptions.add(betOption);
		betOption.setBetType(this);
	}
	
	@Transient
	public List<BetOption> getWinners(){
		List<BetOption> winners = new ArrayList<BetOption>();
		for (BetOption betOption : getBetOptions()) {
			if((betOption.getWinner()!=null)&&(betOption.getWinner())) {
				winners.add(betOption);
			}
		}
		return winners;
	}

	@Version
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}	
}
