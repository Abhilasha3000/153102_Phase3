package com.capgemini.paytm.beans;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

public class Transaction implements Serializable{
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)

	private int transaction_Id;
	
	public Transaction(int transaction_Id, String mobileNo, String transactionDate, String transaction_type,
			Float transaction_amount, String transaction_status) {
		super();
		this.transaction_Id = transaction_Id;
		this.mobileNo = mobileNo;
		this.transactionDate = transactionDate;
		this.transaction_type = transaction_type;
		this.transaction_amount = transaction_amount;
		this.transaction_status = transaction_status;
	}
	public int getTransaction_Id() {
		return transaction_Id;
	}
	public void setTransaction_Id(int transaction_Id) {
		this.transaction_Id = transaction_Id;
	}
	
	private String mobileNo;
	private String transactionDate;
	private String transaction_type;
	private Float transaction_amount;
	private String transaction_status;
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	public Float getTransaction_amount() {
		return transaction_amount;
	}
	public void setTransaction_amount(Float transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	public String getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}
	
	
	
	
	@Override
	public String toString() {
		return "Transaction [transaction_Id=" + transaction_Id + ", mobileNo=" + mobileNo + ", transactionDate="
				+ transactionDate + ", transaction_type=" + transaction_type + ", transaction_amount="
				+ transaction_amount + ", transaction_status=" + transaction_status + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + transaction_Id;
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
		Transaction other = (Transaction) obj;
		if (transaction_Id != other.transaction_Id)
			return false;
		return true;
	}
	public Transaction() {
		// TODO Auto-generated constructor stub
	}
	
	

}
