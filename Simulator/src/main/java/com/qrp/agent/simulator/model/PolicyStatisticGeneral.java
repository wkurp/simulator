package com.qrp.agent.simulator.model;

public class PolicyStatisticGeneral {
	private int year;
	private int lostCount;
	private int gainedCount;
	private int reGainedCount;
	
	public PolicyStatisticGeneral(int year, int lostCount, int gainedCount, int reGainedCount) {
		super();
		this.year = year;
		this.lostCount = lostCount;
		this.gainedCount = gainedCount;
		this.reGainedCount = reGainedCount;
	}
	public int getYear() {
		return year;
	}
	public int getLostCount() {
		return lostCount;
	}
	public int getGainedCount() {
		return gainedCount;
	}
	public int getReGainedCount() {
		return reGainedCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gainedCount;
		result = prime * result + lostCount;
		result = prime * result + reGainedCount;
		result = prime * result + year;
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
		PolicyStatisticGeneral other = (PolicyStatisticGeneral) obj;
		if (gainedCount != other.gainedCount)
			return false;
		if (lostCount != other.lostCount)
			return false;
		if (reGainedCount != other.reGainedCount)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PolicyStatisticGeneral [year=" + year + ", lostCount=" + lostCount + ", gainedCount=" + gainedCount
				+ ", reGainedCount=" + reGainedCount + "]";
	}	
}