package com.tourism.Cultoura.ResponseModels;

import java.math.BigDecimal;
import java.util.List;

public class BillDetailsResponse {
    private List<BillItem> billItems;
    private BigDecimal totalCost;
    private BigDecimal discountApplied;
    private BigDecimal finalPayableAmount;
	public List<BillItem> getBillItems() {
		return billItems;
	}
	public void setBillItems(List<BillItem> billItems) {
		this.billItems = billItems;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	public BigDecimal getDiscountApplied() {
		return discountApplied;
	}
	public void setDiscountApplied(BigDecimal discountApplied) {
		this.discountApplied = discountApplied;
	}
	public BigDecimal getFinalPayableAmount() {
		return finalPayableAmount;
	}
	public void setFinalPayableAmount(BigDecimal finalPayableAmount) {
		this.finalPayableAmount = finalPayableAmount;
	}
}