package br.com.daniloalexandre.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import br.com.daniloalexandre.domain.Account;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CSVRevenueWriterService implements RevenueWriterService {

	private static final String FILENAME = "account-update-result.csv";
	private static final String HEADER = "agencia;conta;saldo;status;atualizado";

	@Override
	public void write(List<Account> accounts, String destPath) {

		try {
			FileWriter fileWriter = new FileWriter(destPath + File.separator + FILENAME);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.printf(HEADER);
			accounts.stream().forEach((account) -> {
				log.info(String.format("%s;%s;%s;%s;%s", account.getAgency(), account.getAccountNumber(),
						account.getBalance(), account.getStatus(), account.isUpdated()));
				printWriter.printf("%s;%s;%s;%s;%s\n", account.getAgency(), account.getAccountNumberOnlyDigits(),
						account.balanceToString(), account.getStatus(), account.isUpdated());
			});

			printWriter.close();
		} catch (IOException e) {
			log.error(String.format("Não foi possível gerar o arquivo de resultado."));
			log.error(e.getMessage());
		}

	}

}
