package quest.phil.diagnostic.information.system.ws.model.request;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class QisTransactionAuthorizeRefundRequest extends QisTransactionAuthorizeRequest{

	private static final long serialVersionUID = 3020475061057382943L;

	@NotNull(message = "Refind Items are required.")
	@NotEmpty(message = "Refind Items should not be empty.")
	@Valid
	private Set<Long> refundItems;
	
	public QisTransactionAuthorizeRefundRequest() {
		super();
	}

	public Set<Long> getRefundItems() {
		return refundItems;
	}

	public void setRefundItems(Set<Long> refundItems) {
		this.refundItems = refundItems;
	}
	
}
