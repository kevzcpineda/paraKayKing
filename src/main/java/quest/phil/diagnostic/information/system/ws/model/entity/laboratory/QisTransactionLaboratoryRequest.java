package quest.phil.diagnostic.information.system.ws.model.entity.laboratory;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionLaboratoryRequestClass;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisTransactionLaboratoryRequest extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = 8213495165215945244L;

	public QisTransactionLaboratoryRequest() {
		super();
	}

	public QisTransactionLaboratoryRequest(@NotNull(message = "Transaction Item is required.") Long transactionItemId,
			@NotNull(message = "Item is required.") Long itemId,
			@NotEmpty(message = "Item Laboratory is required.") @Size(min = 1, max = 12, message = "Item Laboratory must between {min} and {max} characters.") String itemLaboratory) {
		super(transactionItemId, itemId, itemLaboratory);
	}

}
