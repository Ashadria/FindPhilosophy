package com.dasha.philosophy.model;

public class ClickData {
	private Integer id;
	private String startUrl;
	private Integer numberOfClicksToPhilosophy;
	
	public ClickData() {}

	public ClickData(String startUrl, Integer numberOfClicksToPhilosophy) {
		this.setStartUrl(startUrl);
		this.setNumberOfClicksToPhilosophy(numberOfClicksToPhilosophy);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getStartUrl() {
		return startUrl;
	}
	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	
	public Integer getNumberOfClicksToPhilosophy() {
		return numberOfClicksToPhilosophy;
	}
	public void setNumberOfClicksToPhilosophy(Integer numberOfClicksToPhilosophy) {
		this.numberOfClicksToPhilosophy = numberOfClicksToPhilosophy;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((numberOfClicksToPhilosophy == null) ? 0
						: numberOfClicksToPhilosophy.hashCode());
		result = prime * result
				+ ((startUrl == null) ? 0 : startUrl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClickData other = (ClickData) obj;
		if (numberOfClicksToPhilosophy == null) {
			if (other.numberOfClicksToPhilosophy != null)
				return false;
		} else if (!numberOfClicksToPhilosophy
				.equals(other.numberOfClicksToPhilosophy))
			return false;
		if (startUrl == null) {
			if (other.startUrl != null)
				return false;
		} else if (!startUrl.equals(other.startUrl))
			return false;
		return true;
	}
}
