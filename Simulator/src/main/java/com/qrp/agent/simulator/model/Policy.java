package com.qrp.agent.simulator.model;

import java.math.BigDecimal;

public class Policy {
	private String agentBreed;
	private long policyId;
	private int age;
	private int socialGrade;
	private BigDecimal paymentAtPurchase;
	private BigDecimal atributeBrand;
	private BigDecimal atributePrice;
	private BigDecimal atributePromotion;
	private byte autoRenew;
	private int inertiaForSwitch;
	
	public Policy() {
		super();
	}

	public Policy(String agentBreed, long policyId, int age, int socialGrade, BigDecimal paymentAtPurchase,BigDecimal atributeBrand, BigDecimal atributePrice, BigDecimal atributePromotion, byte autoRenew,int inertiaForSwitch) {
		super();
		this.agentBreed = agentBreed;
		this.policyId = policyId;
		this.age = age;
		this.socialGrade = socialGrade;
		this.paymentAtPurchase = paymentAtPurchase;
		this.atributeBrand = atributeBrand;
		this.atributePrice = atributePrice;
		this.atributePromotion = atributePromotion;
		this.autoRenew = autoRenew;
		this.inertiaForSwitch = inertiaForSwitch;
	}
	public String getAgentBreed() {
		return agentBreed;
	}

	public void setAgentBreed(String agentBreed) {
		this.agentBreed = agentBreed;
	}

	public long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(long policyId) {
		this.policyId = policyId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSocialGrade() {
		return socialGrade;
	}

	public void setSocialGrade(int socialGrade) {
		this.socialGrade = socialGrade;
	}

	public BigDecimal getPaymentAtPurchase() {
		return paymentAtPurchase;
	}

	public void setPaymentAtPurchase(BigDecimal paymentAtPurchase) {
		this.paymentAtPurchase = paymentAtPurchase;
	}

	public BigDecimal getAtributeBrand() {
		return atributeBrand;
	}

	public void setAtributeBrand(BigDecimal atributeBrand) {
		this.atributeBrand = atributeBrand;
	}

	public BigDecimal getAtributePrice() {
		return atributePrice;
	}

	public void setAtributePrice(BigDecimal atributePrice) {
		this.atributePrice = atributePrice;
	}

	public BigDecimal getAtributePromotion() {
		return atributePromotion;
	}

	public void setAtributePromotion(BigDecimal atributePromotion) {
		this.atributePromotion = atributePromotion;
	}

	public byte getAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(byte autoRenew) {
		this.autoRenew = autoRenew;
	}

	public int getInertiaForSwitch() {
		return inertiaForSwitch;
	}

	public void setInertiaForSwitch(int inertiaForSwitch) {
		this.inertiaForSwitch = inertiaForSwitch;
	}

	public void incrementAge(){
		this.age = this.age +1;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (policyId ^ (policyId >>> 32));
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
		Policy other = (Policy) obj;
		if (policyId != other.policyId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Policy [agentBreed=" + agentBreed + ", policyId=" + policyId + ", age=" + age + ", socialGrade="
				+ socialGrade + ", paymentAtPurchase=" + paymentAtPurchase + ", atributeBrand=" + atributeBrand
				+ ", atributePrice=" + atributePrice + ", atributePromotion=" + atributePromotion + ", autoRenew="
				+ autoRenew + ", inertiaForSwitch=" + inertiaForSwitch + "]";
	}
}