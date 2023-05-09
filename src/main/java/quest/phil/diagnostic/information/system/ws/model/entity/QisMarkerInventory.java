package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;


@Entity
@DynamicUpdate
@Table(name = "markers_inventory" , uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class QisMarkerInventory implements Serializable{
	
	private static final long serialVersionUID = -7657607524044596870L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "film_11x14", nullable = true, columnDefinition = "int default 0")
	private int film11x14;
	
	@Column(name = "film_14x17", nullable = true, columnDefinition = "int default 0")
	private int film14x17;
	
	@Column(name = "film_14x14", nullable = true, columnDefinition = "int default 0")
	private int film14x14;
	
	@Column(name = "film_10x12", nullable = true, columnDefinition = "int default 0")
	private int film10x12;
	
	@Column(name = "film_8x10", nullable = true, columnDefinition = "int default 0")
	private int film8x10;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
