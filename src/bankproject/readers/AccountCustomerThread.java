package bankproject.readers;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import bankproject.entities.Account;
import bankproject.entities.CountryEnum;
import bankproject.entities.Customer;
import bankproject.services.SrvAccount;
import bankproject.services.SrvCustomer;
import bankproject.services.db.SQLiteManager;

public class AccountCustomerThread extends Thread {
	private static int INTERVAL = 2;
	public void run() {
		
		do {
			try {
				File file = new File("/tmp/bank/input/accounts_customers.txt");
				
				if (file.exists()) {				
					Scanner sc = new Scanner(file);
					int i = 0;
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						if (i > 0) {
							Scanner lsc = new Scanner(line);
							CountryEnum country = CountryEnum.getByLongName(lsc.next().trim());
							String lastName = lsc.next().trim();
							String firstName = lsc.next().trim();
							Integer amount = lsc.nextInt();
							lsc.close();
							
							saveAccountAndCustomer(firstName, lastName, country, amount);
						}
						i++;
					}
					
					System.out.println("AccountCustomer : Nombre de lignes ajoutées : " + (i-1));
					
					sc.close();
					file.delete();
				} else {
					Date date = new Date();
					String sDate = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.FRANCE).format(date);
					System.out.println(sDate + " : Pas de fichier " + file.getPath() + " à lire");
				}
						
				sleep(INTERVAL*60*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while(true);
	}


	private void saveAccountAndCustomer(String firstName, String lastName, CountryEnum country, Integer amount) throws Exception {
		Customer cu = SrvCustomer.getInstance(SQLiteManager.getInstance()).getOrCreateByName(firstName, lastName);
		Account ac = new Account();
		ac.setCustomer(cu);
		ac.setCountry(country);
		ac.generateAccountNumber();
		
		if (amount > 0) {
			ac.addAmount(new Double(amount));
		} else {
			throw new Exception("On ne peut créer un compte avec une somme inférieure ou égale à 0");
		}
		
		SrvAccount.getInstance(SQLiteManager.getInstance()).save(ac);

	}
}
