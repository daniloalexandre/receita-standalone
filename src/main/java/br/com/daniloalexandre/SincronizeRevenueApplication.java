package br.com.daniloalexandre;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.com.daniloalexandre.domain.Account;
import br.com.daniloalexandre.service.CSVRevenueReaderService;
import br.com.daniloalexandre.service.CSVRevenueWriterService;
import br.com.daniloalexandre.service.RevenueReaderService;
import br.com.daniloalexandre.service.RevenueService;
import br.com.daniloalexandre.service.RevenueWriterService;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class SincronizeRevenueApplication implements CommandLineRunner {

	private static final int TOTAL_ARGS = 2;

	public static void main(String[] args) {
		SpringApplication.run(SincronizeRevenueApplication.class, args);
	}

	@Bean
	public RevenueService getRevenueService() {
		return new RevenueService();
	}

	@Bean
	public RevenueReaderService getRevenueReaderService() {
		return new CSVRevenueReaderService();
	}

	@Bean
	public RevenueWriterService getRevenueWriterService() {
		return new CSVRevenueWriterService();
	}

	@Override
	public void run(String... args) throws Exception {

		if (args.length != TOTAL_ARGS) {
			log.error(
					"Sintaxe: java -jar SincronizeRevenueApplication <input-file> <dest-path>\n"
					+ " input-file - Caminho para o arquivo CSV de entrada\n"
					+ "   Exemplo LINUX - /home/danilo/contas.csv | WINDOWS - C:\\usuarios\\danilo\\contas.csv\n"
					+ " dest-path  - Diretório de destino para o arquivo de resposta\n"
					+ "   Exemplo LINUX - /home/danilo | WINDOWS - C:\\\\usuarios\\\\danilo");
			return;
		}

		List<Account> readAccounts = getRevenueReaderService().read(args[0]);

		List<Account> updatedAccounts = updateAccounts(readAccounts);

		getRevenueWriterService().write(updatedAccounts, args[1]);
	}

	private List<Account> updateAccounts(List<Account> readAccounts) {

		RevenueService revenueService = getRevenueService();

		List<Account> updatedAccounts = new ArrayList<>();

		for (Account account : readAccounts) {
			boolean updated = false;
			try {
				updated = revenueService.atualizarConta(account.getAgency(), account.getAccountNumberOnlyDigits(),
						account.getBalance(), account.getStatus());
			} catch (Exception e) {
				log.error(String.format("Não foi possível atualizar o status da conta %s", account.toString()));
			}
			account.setUpdated(updated);
			updatedAccounts.add(account);
		}

		return updatedAccounts;
	}

}
