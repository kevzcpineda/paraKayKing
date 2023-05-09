package quest.phil.diagnostic.information.system.ws.model.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisTransactionDiscountRequest {
	@NotNull(message = "Authorization Id is required.")
	@Size(max = 20, message = "Authorization Id should not exceed {max} characters.")
	private String authorizeId;

	@NotNull(message = "Discount Amount is required.")
	@Digits(integer = 10, fraction = 4)
	private String discountAmount;

	@NotNull(message = "Description is required.")
	@NotEmpty(message = "Description is required.")
	@Size(max = 250, message = "Description should not exceed {max} characters.")
	private String description;

	public QisTransactionDiscountRequest() {
		super();
	}

	public String getAuthorizeId() {
		return authorizeId;
	}

	public void setAuthorizeId(String authorizeId) {
		this.authorizeId = authorizeId;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
