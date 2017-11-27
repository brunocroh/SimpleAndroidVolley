
package br.com.senaigo.mobile.northwindtrandersrest.entities;

public class Shipper {

	private String shipperID;
	private String companyName;
	private String phone;

	@Override
	public String toString() {
		return getShipperID() + " - " + getCompanyName() + " " + getPhone();
	}

	public String getShipperID() {
		return shipperID;
	}

	public void setShipperID(String shipperID) {
		this.shipperID = shipperID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
