package quest.phil.diagnostic.information.system.ws.model.classes;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;

@Entity
@DynamicUpdate
@Table(name = "qis_reports_denomination")
public class QisDenominationReportsClass implements Serializable {

	private static final long serialVersionUID = -458465884031248734L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NotNull(message = "Cashier ID is required.")
	@Column(name = "cashier_id", nullable = false)
	private Long cashierId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "cashier_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUser cashier;

	@JsonIgnore
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt = Calendar.getInstance();

	@JsonIgnore
	@Column(name = "updated_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedAt = Calendar.getInstance();

	@Column(name = "verify_by", nullable = true)
	private Long verifyBy;
	@ManyToOne(optional = false)
	@JoinColumn(name = "verify_by", insertable = false, updatable = false, nullable = true)
	@JsonManagedReference
	private QisUser verify;
	
	@JsonIgnore
	@Column(name = "verify_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar verifyAt;

	@Column(name = "noted_by", nullable = true)
	private Long notedBy;
	@ManyToOne(optional = false)
	@JoinColumn(name = "noted_by", insertable = false, updatable = false, nullable = true)
	@JsonManagedReference
	private QisUser noted;
	
	@JsonIgnore
	@Column(name = "noted_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar notedAt;

	@Column(name = "report_name", nullable = false)
	@Size(min = 1, max = 120, message = "reportName must between {min} and {max} characters.")
	private String reportName;

	@Column(name = "coverage_date_and_time_from", nullable = false)
	private Calendar coverageDateAndTimeFrom;

	@Column(name = "coverage_date_and_time_to", nullable = false)
	private Calendar coverageDateAndTimeTo;

	@JsonIgnore
	@Column(name = "branch_id", nullable = false, columnDefinition = "bigint default 1")
	private Long branchId = 1L;
	@ManyToOne(optional = false)
	@JoinColumn(name = "branch_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisBranch branch;

	@Column(name = "thousands", columnDefinition = "int DEFAULT 0")
	private int thousands;

	@Column(name = "five_hundreds", columnDefinition = "int DEFAULT 0")
	private int fiveHundreds;

	@Column(name = "two_hundreds", columnDefinition = "int DEFAULT 0")
	private int twoHundreds;

	@Column(name = "one_hundreds", columnDefinition = "int DEFAULT 0")
	private int oneHundreds;

	@Column(name = "fifties", columnDefinition = "int DEFAULT 0")
	private int fifties;

	@Column(name = "twenties", columnDefinition = "int DEFAULT 0")
	private int twenties;

	@Column(name = "tens", columnDefinition = "int DEFAULT 0")
	private int tens;

	@Column(name = "five", columnDefinition = "int DEFAULT 0")
	private int five;

	@Column(name = "one", columnDefinition = "int DEFAULT 0")
	private int one;

	@Column(name = "cents", columnDefinition = "int DEFAULT 0")
	private int cents;

	public Long getId() {
		return id;
	}

	public Long getVerifyBy() {
		return verifyBy;
	}

	public QisUser getVerify() {
		return verify;
	}

	public Long getNotedBy() {
		return notedBy;
	}

	public QisUser getNoted() {
		return noted;
	}

	public void setVerifyBy(Long verifyBy) {
		this.verifyBy = verifyBy;
	}

	public void setVerify(QisUser verify) {
		this.verify = verify;
	}

	public void setNotedBy(Long notedBy) {
		this.notedBy = notedBy;
	}

	public void setNoted(QisUser noted) {
		this.noted = noted;
	}

	public Long getCashierId() {
		return cashierId;
	}

	public QisUser getCashier() {
		return cashier;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public int getThousands() {
		return thousands;
	}

	public int getFiveHundreds() {
		return fiveHundreds;
	}

	public int getTwoHundreds() {
		return twoHundreds;
	}

	public int getOneHundreds() {
		return oneHundreds;
	}

	public int getFifties() {
		return fifties;
	}

	public int getTwenties() {
		return twenties;
	}

	public int getTens() {
		return tens;
	}

	public int getFive() {
		return five;
	}

	public int getOne() {
		return one;
	}

	public int getCents() {
		return cents;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}

	public void setCashier(QisUser cashier) {
		this.cashier = cashier;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setThousands(int thousands) {
		this.thousands = thousands;
	}

	public void setFiveHundreds(int fiveHundreds) {
		this.fiveHundreds = fiveHundreds;
	}

	public void setTwoHundreds(int twoHundreds) {
		this.twoHundreds = twoHundreds;
	}

	public void setOneHundreds(int oneHundreds) {
		this.oneHundreds = oneHundreds;
	}

	public void setFifties(int fifties) {
		this.fifties = fifties;
	}

	public void setTwenties(int twenties) {
		this.twenties = twenties;
	}

	public void setTens(int tens) {
		this.tens = tens;
	}

	public void setFive(int five) {
		this.five = five;
	}

	public void setOne(int one) {
		this.one = one;
	}

	public void setCents(int cents) {
		this.cents = cents;
	}

	public Calendar getCoverageDateAndTimeFrom() {
		return coverageDateAndTimeFrom;
	}

	public Calendar getCoverageDateAndTimeTo() {
		return coverageDateAndTimeTo;
	}

	public void setCoverageDateAndTimeFrom(Calendar coverageDateAndTimeFrom) {
		this.coverageDateAndTimeFrom = coverageDateAndTimeFrom;
	}

	public void setCoverageDateAndTimeTo(Calendar coverageDateAndTimeTo) {
		this.coverageDateAndTimeTo = coverageDateAndTimeTo;
	}

	public Long getBranchId() {
		return branchId;
	}

	public QisBranch getBranch() {
		return branch;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public void setBranch(QisBranch branch) {
		this.branch = branch;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Calendar getVerifyAt() {
		return verifyAt;
	}

	public Calendar getNotedAt() {
		return notedAt;
	}

	public void setVerifyAt(Calendar verifyAt) {
		this.verifyAt = verifyAt;
	}

	public void setNotedAt(Calendar notedAt) {
		this.notedAt = notedAt;
	}
	

}
