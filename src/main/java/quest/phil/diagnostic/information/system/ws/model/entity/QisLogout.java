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

@Entity
@Table(name = "qis_logouts")
public class QisLogout implements Serializable {

	private static final long serialVersionUID = -8687841513801066329L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "qis_user_id", nullable = false)
	private Long qisUserId;

	@Column(name = "log_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar logDate = Calendar.getInstance();

	@Column(name = "token", nullable = false)
	private String token;

	public QisLogout() {
		super();
	}

	public QisLogout(Long qisUserId, String token) {
		super();
		this.qisUserId = qisUserId;
		this.logDate = Calendar.getInstance();
		this.token = token;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
