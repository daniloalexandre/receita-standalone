package br.com.daniloalexandre.service;

import java.util.List;

import br.com.daniloalexandre.domain.Account;

public interface RevenueWriterService {
	
	public void write(List<Account> accounts, String destPath);

}
