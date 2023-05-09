package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "countries")
public class Country implements Serializable {

	private static final long serialVersionUID = 8086548677051071288L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Country Name is required.")
	@Size(min = 1, max = 80, message = "Country Name must between {min} and {max} characters.")
	@Column(name = "country_name", nullable = false, length = 80)
	private String countryName;
	
	@Size(max = 4, message = "2 Letter Code must not exceed to {max} characters.")
	@Column(name = "letter_code_2", nullable = true, length = 4)
	private String letterCode2;
	
	@Size(max = 4, message = "3 Letter Code must not exceed to {max} characters.")
	@Column(name = "letter_code_3", nullable = true, length = 4)
	private String letterCode3;

	@Size(max = 40, message = "Currency must not exceed to {max} characters.")
	@Column(name = "currency", nullable = true, length = 40)
	private String currency;

	@Size(max = 8, message = "Currency Symbol must not exceed to {max} characters.")
	@Column(name = "currency_symbol", nullable = true, length = 8)
	private String currencSymbol;

	@Size(max = 8, message = "Currency Code must not exceed to {max} characters.")
	@Column(name = "currency_code", nullable = true, length = 8)
	private String currencyCode;

	@Size(max = 4, message = "Country Code must not exceed to {max} characters.")
	@Column(name = "country_code", nullable = true, length = 4)
	private String countryCode;
	
	@Size(max = 120, message = "Nationality must not exceed to {max} characters.")
	@Column(name = "nationality", nullable = true, length = 120)
	private String nationality;
	
	@Column(name = "is_active", columnDefinition = "boolean default true")
	private Boolean isActive;
	
	public Country() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getLetterCode2() {
		return letterCode2;
	}

	public void setLetterCode2(String letterCode2) {
		this.letterCode2 = letterCode2;
	}

	public String getLetterCode3() {
		return letterCode3;
	}

	public void setLetterCode3(String letterCode3) {
		this.letterCode3 = letterCode3;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencSymbol() {
		return currencSymbol;
	}

	public void setCurrencSymbol(String currencSymbol) {
		this.currencSymbol = currencSymbol;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
