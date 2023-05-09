package quest.phil.diagnostic.information.system.ws.model.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class QisReferenceLaboratoryTransaction {

//	@JsonIgnore
//	@Id
//	private Long id;
//	
//	@JsonIgnore
//	@Column(name = "referral_id", nullable = true)
//	private Long referralId;
//	@ManyToOne(optional = true)
//	@JoinColumn(name = "referral_id", insertable = false, updatable = false)
//	@JsonManagedReference
//	private QisReferral referral;
}
