package br.com.daniloalexandre.service;

import java.util.List;

import br.com.daniloalexandre.domain.Account;

public interface RevenueReaderService {

	
	List<Account> read(String inputFile);
}
