package com.capgemini.paytm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InsufficientBalanceException;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.repo.WalletRepo;
import com.capgemini.paytm.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {


public WalletRepo repo;

	public WalletServiceImpl(){
		repo= new WalletRepoImpl();
	}
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}
	
	
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		
		
		Customer cust1=new Customer();
		cust1=repo.findOne(mobileNo);
		if(cust1!=null)
			throw new InvalidInputException("Account already exists");
		
		Customer cust=new Customer();
		 validate( name, mobileNo,cust);
		 validate(amount);	//amount should be positive
		 cust.setWallet(new Wallet(amount));
		 
		 repo.beginTransaction();
		boolean result=repo.save(cust);
		
		if(result==true)
		{
			repo.commitTransaction();
			return cust;
		}
			
		else
			
			return null;
				//create object of customer, and call dao save layer
		}

	public Customer showBalance(String mobileNo) {
		
		Customer customer=repo.findOne(mobileNo);		
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {	
		if(sourceMobileNo.equals(targetMobileNo))
		{
			throw new InvalidInputException("Source and target mobile numbers cannot be same");
		}
		 validate(amount);
		Customer scust=new Customer();
		Customer tcust=new Customer();
		scust=repo.findOne(sourceMobileNo);
		if(scust==null|tcust==null)
			throw new InvalidInputException("Account does not exists");
		
		tcust=repo.findOne(targetMobileNo);
		Transaction strans=new Transaction();
		Transaction ttrans=new Transaction();
		
		Wallet sw=scust.getWallet();
		Wallet tw=tcust.getWallet();
		
		strans.setMobileNo(sourceMobileNo);
		ttrans.setMobileNo(targetMobileNo);
		strans.setTransaction_amount(amount.floatValue());
		ttrans.setTransaction_amount(amount.floatValue());
		strans.setTransactionDate(new Date().toString());
		ttrans.setTransactionDate(new Date().toString());
		strans.setTransaction_type("Fund Transfer");
		ttrans.setTransaction_type("Fund Transfer");
		
		repo.beginTransaction();
		if(scust!=null && tcust!=null )
		{	
			if(scust.getWallet().getBalance().compareTo(amount)==1)
			{
			BigDecimal amtSub=scust.getWallet().getBalance();
			BigDecimal diff=amtSub.subtract(amount);
			sw.setBalance(diff);
			scust.setWallet(sw);
			
			BigDecimal amtAdd=tcust.getWallet().getBalance();
			BigDecimal sum=amtAdd.add(amount);			
			tw.setBalance(sum);
			tcust.setWallet(tw);
			
			strans.setTransaction_status("successfull");
			ttrans.setTransaction_status("successfull");
			
			
			repo.Update(targetMobileNo, tcust);
			repo.Update(sourceMobileNo, scust);
			
			}
			else
				{
				strans.setTransaction_status("failed");
				ttrans.setTransaction_status("failed");
				repo.saveTransaction(strans);
				repo.saveTransaction(ttrans);
				repo.commitTransaction();
				
				throw new InsufficientBalanceException("Amount is more than available balance");
				
				}
		}
		
		
		repo.saveTransaction(strans);
		
		repo.saveTransaction(ttrans);
		repo.commitTransaction();
		return tcust;
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		validate(amount);
		
		Customer cust=new Customer();
		cust=repo.findOne(mobileNo);
		if(cust==null)
			throw new InvalidInputException("Account does not exists");
		Wallet wallet=cust.getWallet();
		Transaction strans=new Transaction();
		
		strans.setMobileNo(mobileNo);
		strans.setTransaction_amount(amount.floatValue());
		strans.setTransactionDate(new Date().toString());
		strans.setTransaction_type("Deposit");
		
		
		
			BigDecimal amtAdd=cust.getWallet().getBalance().add(amount);
			wallet.setBalance(amtAdd);
			cust.setWallet(wallet);
			strans.setTransaction_status("success");
			
			repo.beginTransaction();
			repo.saveTransaction(strans);
			repo.Update(mobileNo, cust);
		
		repo.commitTransaction();
		
		return cust;
		
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		
		 validate(amount);
		Customer cust=new Customer();
		cust=repo.findOne(mobileNo);
		if(cust==null)
			throw new InvalidInputException("Account does not exists");
		Wallet wallet=cust.getWallet();
		
		Transaction strans=new Transaction();
		
		strans.setMobileNo(mobileNo);
		strans.setTransaction_amount(amount.floatValue());
		strans.setTransactionDate(new Date().toString());
		strans.setTransaction_type("Withdraw");
		
		repo.beginTransaction();
			if(cust.getWallet().getBalance().compareTo(amount)==1)
			{
			BigDecimal amtSub=cust.getWallet().getBalance().subtract(amount);
			wallet.setBalance(amtSub);
			cust.setWallet(wallet);
			
			repo.Update(mobileNo, cust);
		
			strans.setTransaction_status("success");
			
			}
			else
				{
				strans.setTransaction_status("failed");
				repo.saveTransaction(strans);
				repo.commitTransaction();
				throw new InsufficientBalanceException("Sorry cannot withdraw,amount to be withdrawn is more than available balance");
				}
		
		
		
			repo.saveTransaction(strans);
		repo.commitTransaction();
		return cust;
	}
	
	
	public List<Transaction> printTransaction(String mobileNo)
	{
		Customer cust=new Customer();
		cust=repo.findOne(mobileNo);
		if(cust==null)
			throw new InvalidInputException("Account does not exists");
		
		return repo.findTransaction(mobileNo);
		
	}
	
	public boolean validate(String name,String phoneno,Customer cust)  {
		Scanner sc=new Scanner(System.in);
		while(true)
		{Pattern pa=Pattern.compile("[a-zA-Z]+\\.?");
		Matcher ma=pa.matcher(name);
		if(ma.matches())
		{
			break;
		}
		else
		{
			System.err.println("Enter valid name: ");
			name=sc.next();
		}
			
		}
		
		 //check if phone no is valid
		while(true)
		{Pattern p=Pattern.compile("(0/91)?[7-9][0-9]{9}");
		Matcher m=p.matcher(phoneno);
		if(m.matches())
		{
			break;
		}
		else
		{
			System.err.println("Enter valid 10 digit phone no: ");
			phoneno=sc.next();
		}
			
		}
		cust.setMobileNo(phoneno);
		cust.setName(name);
		return true;
	}
	
	public boolean validate(BigDecimal amount)
	{

		if(Math.abs(amount.floatValue())==amount.floatValue())
		{
			
		}
		else
		{
			throw new InvalidInputException("Enter positive amount");
			
		}
			
	return true;
	
		
	}
}
