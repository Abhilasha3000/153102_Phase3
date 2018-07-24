package com.capgemini.paytm.repo;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;

public class WalletRepoImpl implements WalletRepo{

	
public WalletRepoImpl() {
	entityManager = JPAUtil.getEntityManager();
	}
	
private EntityManager entityManager;







@Override
public void beginTransaction() {
	entityManager.getTransaction().begin();
}

@Override
public void commitTransaction() {
	
	entityManager.getTransaction().commit();
	
}

@Override
public boolean save(Customer customer) {
	
	entityManager.persist(customer);
	
	
	return true;
}

@Override
public Customer findOne(String mobileNo) {
	Customer cust = entityManager.find(Customer.class, mobileNo);
	
	return cust;
}

@Override
public Customer Update(String mobileNo, Customer custm) {
	entityManager.merge(custm);
	return custm;
}

@Override
public boolean saveTransaction(Transaction transaction) {
	entityManager.persist(transaction);
	
	return true;
}


@Override
public List<Transaction> findTransaction(String mobileNo) {
	
	
	String qStr = "SELECT t FROM Transaction t WHERE t.mobileNo =:mobileno";
	TypedQuery<Transaction> query= entityManager.createQuery(qStr, Transaction.class);
	query.setParameter("mobileno", mobileNo);
	
	 
	return	query.getResultList();
	
	
	

	
}
}
