package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionClass;

@Entity
@DynamicUpdate
@Table(name = "qis_transactions", uniqueConstraints = { @UniqueConstraint(columnNames = { "transaction_id" }) })
public class QisTransactionInfo extends QisTransactionClass implements Serializable {

	private static final long serialVersionUID = 7184788948104821137L;

	private String biller = null;
	
	public QisTransactionInfo() {
		super();
	}
	
	public String getBiller() {
		return biller;
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
