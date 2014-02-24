package es.udc.isg011.apuestas.model.event;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import es.udc.isg011.apuestas.model.bettype.BetType;
import es.udc.isg011.apuestas.model.category.Category;

@Entity

public class Event {
	private Long eventId;
	private String name;
	private Calendar date;
	private List<BetType> betTypes = new ArrayList<BetType>();
	private Category category;
	private long version;
	
	
	public Event() {
		super();
	}
	public Event(String name, Calendar date) {
		super();
		this.name = name;
		this.date = date;
	}
	@Column(name = "eventId")
	@SequenceGenerator(
	name = "EventIdGenerator", 
	sequenceName = "EventSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "EventIdGenerator")
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "eventDate")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	@OneToMany(mappedBy="event")
	public List<BetType> getBetTypes() {
		return betTypes;
	}
	public void setBetTypes(List<BetType> betTypes) {
		this.betTypes = betTypes;
	}
	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name="categoryId")
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public void addBetType(BetType e){
		this.betTypes.add(e);
		e.setEvent(this);
	}
	@Version
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	
	

}
