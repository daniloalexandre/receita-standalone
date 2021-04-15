package br.com.daniloalexandre.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
	
	private String agency;
	private String accountNumber;
	private double balance;
	private String status;
	private boolean updated;
	
	public String getAccountNumberOnlyDigits() {
		return accountNumber.replace("-", "");
	}
	
	public String balanceToString() {
		return String.valueOf(balance).replace(".", ",");
	}
	
	public void setBalanceString(String balanceString) {
		balance = Double.valueOf(balanceString.replace(",", "."));
	}

}
