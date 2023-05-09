package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicUpdate
@Table(name = "markers" , uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class QisMarker implements Serializable {

	private static final long serialVersionUID = 1814663910445016767L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@Column(name = "patient_id", nullable = false)
	private Long patientId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisPatient patient;
	
	@Column(name = "qis_transaction_id", nullable = false)
	private String qisTransaction;
	
	@NotNull(message = "Film Size is required")
	@Column(name = "film_size", nullable = false)
	private String filmSize;
	
	@NotNull(message = "Xray Type is required")
	@Column(name = "xray_type", nullable = false)
	private String xrayType;
	
	@NotNull(message = "radTech is required")
	@Column(name = "rad_tech", nullable = false)
	private String radTech;
	
	@Column(name = "spoiled", nullable = false, columnDefinition = "boolean default false")
	private boolean spoiled;
	
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt = Calendar.getInstance();
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id")
	@JsonManagedReference
	private QisMarkerInventory markersInventory;
	
	public QisMarker() {
		super();
		this.createdAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public Long getPatientId() {
		return patientId;
	}

	public QisPatient getPatient() {
		return patient;
	}

	public String getQisTransaction() {
		return qisTransaction;
	}

	public void setQisTransaction(String qisTransaction) {
		this.qisTransaction = qisTransaction;
	}

	public String getFilmSize() {
		return filmSize;
	}

	public String getXrayType() {
		return xrayType;
	}

	public String getRadTech() {
		return radTech;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public void setPatient(QisPatient patient) {
		this.patient = patient;
	}

	public void setFilmSize(String filmSize) {
		this.filmSize = filmSize;
	}

	public void setXrayType(String xrayType) {
		this.xrayType = xrayType;
	}

	public void setRadTech(String radTech) {
		this.radTech = radTech;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isSpoiled() {
		return spoiled;
	}

	public void setSpoiled(boolean spoiled) {
		this.spoiled = spoiled;
	}

	public QisMarkerInventory getMarkersInventory() {
		return markersInventory;
	}

	public void setMarkersInventory(QisMarkerInventory markersInventory) {
		this.markersInventory = markersInventory;
	}

	
	
	
}
