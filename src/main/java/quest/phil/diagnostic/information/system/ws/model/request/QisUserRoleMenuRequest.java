package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

public class QisUserRoleMenuRequest implements Serializable {

	private static final long serialVersionUID = -6169327311826370917L;

	@NotEmpty(message = "Menus should not be empty.")
	private Set<Long> menus;

	public QisUserRoleMenuRequest() {
		super();
	}

	public Set<Long> getMenus() {
		return menus;
	}

	public void setMenus(Set<Long> menus) {
		this.menus = menus;
	}
}
