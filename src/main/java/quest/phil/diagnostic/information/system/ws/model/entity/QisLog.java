package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "qis_logs")
public class QisLog implements Serializable {

	private static final long serialVersionUID = 1116016263319308790L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "qis_user_id", nullable = false)
	private Long qisUserId;

	@Column(name = "log_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar logDate = Calendar.getInstance();

	@Column(name = "logger", nullable = false, length = 60)
	@Size(max = 60)
	private String logger;

	@Column(name = "level", nullable = false, length = 12)
	@Size(max = 12)
	private String level;

	@Column(name = "action", nullable = true, length = 20)
	@Size(max = 20)
	private String action;

	@Column(name = "message", nullable = false, columnDefinition = "TEXT")
	private String message;

	@Column(name = "reference_id", nullable = true)
	private Long referenceId;

	@Column(name = "reference", nullable = true, length = 40)
	@Size(max = 40)
	private String reference;

	@Column(name = "client_source", nullable = true)
	private String clientSource;

	public QisLog() {
		super();
	}

	public QisLog(Long qisUserId, @Size(max = 60) String logger, @Size(max = 12) String level,
			@Size(max = 20) String action, String message, Long referenceId, @Size(max = 40) String reference,
			String clientSource) {
		super();
		this.qisUserId = qisUserId;
		this.logDate = Calendar.getInstance();
		this.logger = logger;
		this.level = level;
		this.action = action;
		this.message = message;
		this.referenceId = referenceId;
		this.reference = reference;
		this.clientSource = clientSource;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQisUserId() {
		return qisUserId;
	}

	public void setQisUserId(Long qisUserId) {
		this.qisUserId = qisUserId;
	}

	public Calendar getLogDate() {
		return logDate;
	}

	public void setLogDate(Calendar logDate) {
		this.logDate = logDate;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getClientSource() {
		return clientSource;
	}

	public void setClientSource(String clientSource) {
		this.clientSource = clientSource;
	}
}
