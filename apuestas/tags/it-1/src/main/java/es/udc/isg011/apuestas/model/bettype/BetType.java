package es.udc.isg011.apuestas.model.bettype;

import java.util.HashSet;
import java.util.Set;

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

import org.hibernate.annotations.Immutable;


import es.udc.isg011.apuestas.model.betoption.BetOption;
import es.udc.isg011.apuestas.model.event.Event;

@Entity
@Immutable
public class BetType {
	private Long betTypeId;
	private String question;
	private boolean multipleChoice;
	private Event event;
	private Set<BetOption> betOptions = new HashSet<BetOption>();
	
	
	public BetType() {
		super();
	}
	
	
	public BetType(String question, boolean multipleChoice) {
		super();
		this.question = question;
		this.multipleChoice = multipleChoice;
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
	public boolean getMultipleChoice() {
		return multipleChoice;
	}
	public void setMultipleChoice(boolean multipleChoice) {
		this.multipleChoice = multipleChoice;
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
	public Set<BetOption> getBetOptions() {
		return betOptions;
	}
	public void setBetOptions(Set<BetOption> betOptions) {
		this.betOptions = betOptions;
	}

	public void addBetOption(BetOption betOption) {
		this.betOptions.add(betOption);
		betOption.setBetType(this);
	}

}
