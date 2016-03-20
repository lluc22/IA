package Domini;

import java.util.ArrayList;
import IA.DistFS.*;
public class Estat {
	private int[] peticions;
	private int [] tempsServidors;
	
	public Estat(Requests req, Servers ser, int numGenIni){
		peticions = new int[req.size()];
		tempsServidors = new int[ser.size()];
	}

	public void assigna (int pet, int serv, Requests req, Servers ser) {
		int usuari = req.getRequest(pet)[0];
		int servAntic = peticions[pet];
		tempsServidors[servAntic] -= ser.tranmissionTime(servAntic, usuari);
		peticions[pet] = serv;
		tempsServidors[serv] += ser.tranmissionTime(serv, usuari);
	}

	public void intercanvia (int pet1, int pet2, Requests req, Servers ser) {
		int tempsAntic1 = ser.tranmissionTime(peticions[pet1], req.getRequest(pet1)[0]);
		int tempsAntic2 = ser.tranmissionTime(peticions[pet2], req.getRequest(pet2)[0]);
		int tempsNou1 = ser.tranmissionTime(peticions[pet2], req.getRequest(pet1)[0]);
		int tempsNou2 = ser.tranmissionTime(peticions[pet1], req.getRequest(pet2)[0]);
		tempsServidors[peticions[pet1]] += tempsNou2 - tempsAntic1;
		tempsServidors[peticions[pet2]] += tempsNou1 - tempsAntic2;
		int aux = peticions[pet1];
		peticions[pet1] = peticions[pet2];
		peticions[pet2] = aux;
	}
}
