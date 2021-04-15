package br.com.daniloalexandre.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.daniloalexandre.domain.Account;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CSVRevenueReaderService implements RevenueReaderService{

	private static final String HEADER = "agencia;conta;saldo;status";
	private static final int EXPECTED_TOTAL_DATA = 4;
	private static final String SEPARATOR = ";";
	private static final int AGENCY_INDEX = 0;
	private static final int ACCOUNT_NUMBER_INDEX = 1;
	private static final int BALANCE_INDEX = 2;
	private static final int STATUS_INDEX = 3;
	

	@Override
	public List<Account> read(String inputFile) {
		
		List<Account> accounts = new ArrayList<>();
		
		try {
			Path path = Paths.get(inputFile);
			InputStream in = Files.newInputStream(path);
			InputStreamReader streamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
			BufferedReader bufferReader = new BufferedReader(streamReader);
			
			bufferReader.lines().forEach((line) -> {
				if(!line.equals(HEADER)) accounts.add(stringToAccount(line));
			});
			
			bufferReader.close();
			
		} catch (Exception e) {
			log.error(String.format("Não foi possível ler o arquivo: %s", inputFile));
			log.error(e.getMessage());
		}
		
		
		
		return accounts;
	}
	
	private Account stringToAccount(String stringAccount) throws RuntimeException {
		Account account = new Account();
		String[] accountData = stringAccount.split(SEPARATOR);
		
		if(accountData.length != EXPECTED_TOTAL_DATA) 
			throw new RuntimeException(String.format("Formato de dados inválido %s", stringAccount));
			
		account.setAgency(accountData[AGENCY_INDEX]);
		account.setAccountNumber(accountData[ACCOUNT_NUMBER_INDEX]);
		
		try {
			account.setBalanceString(accountData[BALANCE_INDEX]);
		}catch(NumberFormatException e) {
			throw new RuntimeException(String.format("Formato de dados inválido %s", stringAccount));
		}
		account.setStatus(accountData[STATUS_INDEX]);
		
		return account;
	}
	
	

}
