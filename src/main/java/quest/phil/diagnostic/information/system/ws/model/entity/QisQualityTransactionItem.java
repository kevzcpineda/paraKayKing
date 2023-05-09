package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionItemClass;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_items")
public class QisQualityTransactionItem extends QisTransactionItemClass implements Serializable {

	private static final long serialVersionUID = 8095855752378062908L;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "qis_transaction_item_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Set<QisQualityTransactionLaboratory> itemLaboratories = new HashSet<>();

	public QisQualityTransactionItem() {
		super();
	}

	public Set<QisQualityTransactionLaboratory> getItemLaboratories() {
		return itemLaboratories;
	}

	public void setItemLaboratories(Set<QisQualityTransactionLaboratory> itemLaboratories) {
		this.itemLaboratories = itemLaboratories;
	}

	@Override
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}
}
