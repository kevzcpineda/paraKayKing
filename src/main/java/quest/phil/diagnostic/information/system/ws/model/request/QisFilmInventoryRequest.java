package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

public class QisFilmInventoryRequest implements Serializable {

	private static final long serialVersionUID = -7331655913464310506L;
	private int film11x14;
	private int film14x17;
	private int film14x14;
	private int film10x12;
	private int film8x10;
	
	public QisFilmInventoryRequest(){
		super();
	}

	public int getFilm11x14() {
		return film11x14;
	}

	public void setFilm11x14(int film11x14) {
		this.film11x14 = film11x14;
	}

	public int getFilm14x17() {
		return film14x17;
	}

	public void setFilm14x17(int film14x17) {
		this.film14x17 = film14x17;
	}

	public int getFilm14x14() {
		return film14x14;
	}

	public void setFilm14x14(int film14x14) {
		this.film14x14 = film14x14;
	}

	public int getFilm10x12() {
		return film10x12;
	}

	public void setFilm10x12(int film10x12) {
		this.film10x12 = film10x12;
	}

	public int getFilm8x10() {
		return film8x10;
	}

	public void setFilm8x10(int film8x10) {
		this.film8x10 = film8x10;
	}
	
	
}
