package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

public class QisReferencePackageRequest implements Serializable{

	private static final long serialVersionUID = 8501893254012907953L;
	
	private String packageId;
	private double originalPrice;
	private double molePrice;
	
	public String getPackageId() {
		return packageId;
	}
	public double getOriginalPrice() {
		return originalPrice;
	}
	public double getMolePrice() {
		return molePrice;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}
	public void setMolePrice(double molePrice) {
		this.molePrice = molePrice;
	}

}
