package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "menus")
public class Menu implements Serializable {

	private static final long serialVersionUID = 2067536560110704837L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Menu Name is required.")
	@Size(min = 1, max = 20, message = "Menu Name must between {min} and {max} characters.")
	@Column(name = "menu_name", nullable = false, length = 20)
	private String menuName;

	@NotEmpty(message = "Menu Group is required.")
	@Size(min = 1, max = 8, message = "Menu Group must between {min} and {max} characters.")
	@Column(name = "menu_group", nullable = false, length = 8)
	private String menuGroup;

	@Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
	private boolean isActive;

	public Menu() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(String menuGroup) {
		this.menuGroup = menuGroup;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

}
