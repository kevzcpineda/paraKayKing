package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisRoleClass;

@Entity
@DynamicUpdate
@Table(name = "qis_roles")
public class QisUserRole extends QisRoleClass implements Serializable {
	private static final long serialVersionUID = -953883066519263466L;

	@NotBlank(message = "Rolename is required.")
	@Size(max = 40, message = "Rolename has a maximum of {max} characters.")
	@Column(length = 40)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_user_role_menus", joinColumns = @JoinColumn(name = "qis_role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
	private Set<Menu> menus = new HashSet<>();
	
	public QisUserRole() {
		super();
	}

	public QisUserRole(
			@NotBlank(message = "Rolename is required.") @Size(max = 40, message = "Rolename has a maximum of {max} characters.") String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}	
}
