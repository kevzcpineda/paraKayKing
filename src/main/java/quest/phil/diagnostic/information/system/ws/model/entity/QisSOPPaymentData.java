package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisSOPPaymentClass;

@Entity
@DynamicUpdate
@Table(name = "qis_sop_payment")
public class QisSOPPaymentData extends QisSOPPaymentClass implements Serializable{

	private static final long serialVersionUID = 1101228009301826817L;

	public QisSOPPaymentData() {
		super();
	}
}
