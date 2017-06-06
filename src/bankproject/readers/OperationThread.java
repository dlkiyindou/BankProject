package bankproject.readers;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import bankproject.entities.Account;
import bankproject.entities.CountryEnum;
import bankproject.entities.Customer;
import bankproject.entities.Operation;
import bankproject.entities.TypeOperationEnum;
import bankproject.services.SrvAccount;
import bankproject.services.SrvCustomer;
import bankproject.services.db.SQLiteManager;

public class OperationThread extends Thread {
	private static int INTERVAL = 3;
	public void run() {
		
		do {
			try {
				File file = new File("/tmp/bank/input/operations.txt");
				
				if (file.exists()) {				
					Scanner sc = new Scanner(file);
					int i = 0;
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						if (i > 0) {
							Scanner lsc = new Scanner(line);
							Integer amount = lsc.nextInt();
							String fullAccountNumber= lsc.next().trim();
							String customerFullName = lsc.next();
							lsc.close();
							
							saveOperation(amount, fullAccountNumber, customerFullName);
						}
						i++;
					}
					
					System.out.println("Operation : Nombre de lignes ajoutées : "+  (i-1));
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

		
	private void saveOperation(Integer amount, String fullAccountNumber, String customerFullName) throws Exception {
		Integer accountNumber = Integer.decode(fullAccountNumber.substring(2));
		String shortCodeCountry = fullAccountNumber.substring(-6);
		String firstname = customerFullName.split("\\s+")[0];
		String lastname = customerFullName.split("\\s+")[1];
		
		Customer cu = SrvCustomer.getInstance(SQLiteManager.getInstance()).getByName(firstname, lastname);
		Account ac = SrvAccount.getInstance(SQLiteManager.getInstance()).getByAccountNumberAndCountry(accountNumber, CountryEnum.getByShortCode(shortCodeCountry));
		if (ac.getCustomer().getId() == cu.getId()) {
			ac.addAmount(new Double(amount));
			
			Operation op = new Operation();
			op.setAccount(ac);
			op.setTypeOperation(amount > 0 ? TypeOperationEnum.CREDIT : TypeOperationEnum.CREDIT);
			op.setAmount(new Double(amount));
		}
	}
}
