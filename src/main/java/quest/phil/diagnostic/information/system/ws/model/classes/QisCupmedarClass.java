package quest.phil.diagnostic.information.system.ws.model.classes;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class QisCupmedarClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "send", nullable = true, columnDefinition = "0")
	private String send;
	
	@Column(name = "print", nullable = true, columnDefinition = "0")
	private String print;
}
