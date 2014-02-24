package es.udc.isg011.apuestas.model.virtualaccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import es.udc.isg011.apuestas.model.userprofile.UserProfile;

@Entity
@org.hibernate.annotations.BatchSize(size=10)
public class VirtualAccount {
	private Long virtualAccountId;
	private float balance;
	private UserProfile userProfile;
	private long version;
	
	private final float INIT_BALANCE = 0;
	
	public VirtualAccount() {
		super();
	}

	public VirtualAccount(UserProfile userProfile) {
		super();
		this.balance = INIT_BALANCE;
		this.userProfile = userProfile;
	}

	@Column(name = "virtAccId")
	@SequenceGenerator(
	name = "VirtualAccIdGenerator", 
	sequenceName = "VirtualAccSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VirtualAccIdGenerator")
	public Long getVirtualAccountId() {
		return virtualAccountId;
	}
	
	public void setVirtualAccountId(Long virtualAccountId) {
		this.virtualAccountId = virtualAccountId;
	}
	
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	
	
	@OneToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="usrId")
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	@Version
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}	
	
	
}
