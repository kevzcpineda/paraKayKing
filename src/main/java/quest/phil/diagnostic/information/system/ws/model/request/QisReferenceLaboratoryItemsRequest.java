package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

public class QisReferenceLaboratoryItemsRequest implements Serializable  {

	private static final long serialVersionUID = 309195457547095177L;
	
	private String itemId;
	private double originalPrice;
	private double molePrice;
	public String getItemId() {
		return itemId;
	}
	public double getOriginalPrice() {
		return originalPrice;
	}
	public double getMolePrice() {
		return molePrice;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}
	public void setMolePrice(double molePrice) {
		this.molePrice = molePrice;
	}
	
	
}
