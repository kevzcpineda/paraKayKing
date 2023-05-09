package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSeTPHAWTRequest implements Serializable {

	private static final long serialVersionUID = -6492014956091968350L;

	private Boolean test1;
	private Boolean test2;
	private Boolean test3;
	private Boolean test4;
	private Boolean test5;
	private Boolean test6;
	private Boolean test7;
	private Boolean test8;
	private Boolean test9;
	
	private String referenceLabId;
	
	public QisTransactionLabSeTPHAWTRequest() {
		super();
	}

	public Boolean getTest1() {
		return test1;
	}

	public Boolean getTest2() {
		return test2;
	}

	public Boolean getTest3() {
		return test3;
	}

	public Boolean getTest4() {
		return test4;
	}

	public Boolean getTest5() {
		return test5;
	}

	public Boolean getTest6() {
		return test6;
	}

	public Boolean getTest7() {
		return test7;
	}

	public Boolean getTest8() {
		return test8;
	}

	public Boolean getTest9() {
		return test9;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setTest1(Boolean test1) {
		this.test1 = test1;
	}

	public void setTest2(Boolean test2) {
		this.test2 = test2;
	}

	public void setTest3(Boolean test3) {
		this.test3 = test3;
	}

	public void setTest4(Boolean test4) {
		this.test4 = test4;
	}

	public void setTest5(Boolean test5) {
		this.test5 = test5;
	}

	public void setTest6(Boolean test6) {
		this.test6 = test6;
	}

	public void setTest7(Boolean test7) {
		this.test7 = test7;
	}

	public void setTest8(Boolean test8) {
		this.test8 = test8;
	}

	public void setTest9(Boolean test9) {
		this.test9 = test9;
	}

	public void setReferenceLabId(String referenceLabId) {
		this.referenceLabId = referenceLabId;
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
