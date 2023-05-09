package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisMarkerRequest implements Serializable {

	private static final long serialVersionUID = -7789015218750910819L;
	
	private String filmSize;
	private String radTech;
	private String xrayType;
	private Long id;

	public QisMarkerRequest() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilmSize() {
		return filmSize;
	}

	public String getRadTech() {
		return radTech;
	}

	public String getXrayType() {
		return xrayType;
	}

	public void setFilmSize(String filmSize) {
		this.filmSize = filmSize;
	}

	public void setRadTech(String radTech) {
		this.radTech = radTech;
	}

	public void setXrayType(String xrayType) {
		this.xrayType = xrayType;
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
