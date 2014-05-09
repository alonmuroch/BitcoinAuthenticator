import java.io.File;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This the main class for the test wallet. You can execute commands from the command line. 
 * At the moment you can't do multi-in/multi-out transactions.
 */
public class Main {

	/**Launches the application*/
	public static void main(String[] args) throws Exception {
		Date dNow = new Date(0);
		SimpleDateFormat ft =  new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("Testwallet v. 0.01 (" + ft.format(dNow) + ")");
		//Gets the balance of the wallet if a wallet file exists
		String filePath = new java.io.File( "." ).getCanonicalPath() + "/wallet.json";
		File f = new File(filePath);
		long balance = 0;
		if(f.exists() && !f.isDirectory()) {
			WalletOperation wallet = new WalletOperation();
			WalletFile file2 = new WalletFile();
			if (file2.getKeyNum()!=0){
			ArrayList<String> addrs2 = file2.getAddresses();
			for (int i=0; i<addrs2.size(); i++){
				addrs2.get(i);
			}
			balance = wallet.getBalance(addrs2);
			}
			System.out.println("Wallet loaded successfully. Balance = " + balance + " satoshi");
		}
		else {
			System.out.println("No wallet file exists. Pair a new wallet.");
		}
		System.out.println("Type 'help()' for a list of commands");
		inputCommand();
	}

	/**Lets user enter commands and executes them*/
	@SuppressWarnings("static-access")
	static void inputCommand() throws Exception {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		  while (true){
			  String s = "";
			  System.out.print(">>> ");
			  s = in.nextLine().toLowerCase();
			  String u = s;
			  String command = s.substring(0,s.indexOf("("));
			  //List of supported commands
			  ArrayList<String> cmd = new ArrayList<String>();
			  cmd.add("pairwallet");  
			  cmd.add("send");  
			  cmd.add("newaddress");   
			  cmd.add("mktx");  
			  cmd.add("openport");  
			  cmd.add("listaddresses");
			  cmd.add("getbalance");
			  cmd.add("help");  
			  //Switch for executing the commands
			  WalletOperation op = new WalletOperation();
			  switch (cmd.indexOf(command)){
			  case 0:
				  String walletType = s.substring(s.indexOf("(")+1, s.indexOf(")"));
				  PairingProtocol pair = new PairingProtocol();
				  pair.run(walletType);
				  break;
			  case 1:
				  op.sendTX();
				  break;
			  case 2:
				  String addr = op.genAddress();
				  System.out.println(addr);
				  break;
			  case 3:
				  ArrayList<String> to = new ArrayList<String>();
				  ArrayList<String> amount = new ArrayList<String>();
				  String output = "";
				  boolean stop = false;
				  while (!stop) {
					  System.out.print("Output Address: ");
					  to.add(in.nextLine());
					  System.out.print("Amount: ");
					  amount.add(in.nextLine());
					  System.out.print("Add another output? (y/n): ");
					  String response = in.nextLine().toLowerCase();
					  if (response.equals("n")){stop = true;}
				  }
				  op.mktx(amount, to);  
				  break;
			  case 4:
				  OpenPort.main(null);
				  break;
			  case 5:
				  WalletFile file = new WalletFile();
				  ArrayList<String> addrs = file.getAddresses();
				  for (int i=0; i<addrs.size(); i++){
					  System.out.println(addrs.get(i));
				  }				  
				  break;
			  case 6:
				  WalletFile file2 = new WalletFile();
				  ArrayList<String> addrs2 = file2.getAddresses();
				  for (int i=0; i<addrs2.size(); i++){
					  addrs2.get(i);
				  }
				  long balance = op.getBalance(addrs2);
				  System.out.println(balance + " Satoshi");
				  break;
			  case 7:
				  System.out.println("Usage:");
				  System.out.println("  command(parameter)");
				  System.out.println("");
				  System.out.println("Commands:");
				  System.out.println("  pairwallet(logo)           Displays QR code to scan with the Authenticator. Supported logos include: bitcoincore, electrum, armory, blockchain, multibit, hive, and darkwallet.");
				  System.out.println("  openport()                 If the connection between devices is lost. Run this first, then open the Authenticator to reestablish connection.");
				  System.out.println("  newaddress()               Generates a new multisig address using a key from the wallet and a public key derived from the Authenticator Master Public Key.");            
				  System.out.println("  listaddresses()            Lists all the addresses in the wallet.");
				  System.out.println("  getbalance()               Returns the balance of the wallet in satoshi.");
				  System.out.println("  mktx()                     Builds a new unsigned raw transaction. Inputs are add in cronological order until inputs >= outputs. A fee of .1 mbtc is applied.");
				  System.out.println("  send()                     Sends the raw transaction over to the authenticator for signing.");
				  break;
			  }
		   }
	}
	
}
