package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisSOAPaymentClass;

@Entity
@DynamicUpdate
@Table(name = "qis_soa_payment")
public class QisSOAPaymentData extends QisSOAPaymentClass implements Serializable {

	private static final long serialVersionUID = 4169272659398259829L;

	public QisSOAPaymentData() {
		super();
	}

}
