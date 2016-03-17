package Domini;

import IA.DistFS.*;
import IA.DistFS.Servers.WrongParametersException;
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Requests req = new Requests(10,5,1234);
			Servers ser = new Servers(10,3,1234);
			int numGenIni = 1;
			Estat estat = new Estat(req,ser,numGenIni);
		} catch (WrongParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
