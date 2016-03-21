package Domini;

import java.util.ArrayList;
import java.util.Set;

import IA.DistFS.*;
public class Estat {
	private int[] peticions;
	private int[] tempsServidors;

	public Estat(Requests req,Servers ser, int numGenIni){
		peticions = new int[req.size()];
		tempsServidors = new int[ser.size()];
		initArrays();
		if(numGenIni == 1){generaSolNaif(req, ser);}
		else if(numGenIni == 2) {generarSolMaxTime(req, ser);}
		else if(numGenIni == 3) {generarSolMinTime(req, ser);}
	}
	//Operadors
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


	//Generadors de solucions inicials.
	/**
	 *
	 * @param req peticions demandades.
	 * @param ser servidors actuals.
	 *  Generador de solucio inicial naif: s'assigna sempre al primer servidor que conté el fitxer demandat.
     */
	private void generaSolNaif(Requests req, Servers ser){
		for(int i = 0; i < req.size(); ++i){
			int fileId = req.getRequest(i)[1];
			Set<Integer> fileLocations = ser.fileLocations(fileId);
			int servId = fileLocations.iterator().next();
			peticions[i] = servId; //s'assigna el servidor a la peticio i-esima.

			int userId = req.getRequest(i)[0];
			int transTime = ser.tranmissionTime(servId,userId);
			tempsServidors[servId]+=transTime; //sumem el temps de transmissio de la peticio assignada.
		}
	}

	public void generarSolMaxTime(Requests req, Servers ser){
		int fileId0 = req.getRequest(0)[1];
		int userId0 = req.getRequest(0)[0];
		Set<Integer> fileLocations0 = ser.fileLocations(fileId0);
		int[] firstIt = cercaMin(fileLocations0,ser,fileId0,userId0);
		int firstSer = firstIt[0];
		peticions[0] = firstSer;
		tempsServidors[firstSer] = firstIt[1];
		int maxTime = firstIt[1]; //Iniciem el temps màxim amb l'assignacio de la primera peticio.
		for(int i = 1; i < req.size(); ++i){
			int fileId = req.getRequest(i)[1];
			int userId = req.getRequest(i)[0];
			Set<Integer> fileLocations = ser.fileLocations(fileId);
			int[] values = cercaPrimer(fileLocations,ser,fileId,userId,maxTime);
			peticions[i] = values[0];
			int serV = values[0];
			tempsServidors[serV] += values[1];
			maxTime = values[2];
		}
	}
	private void generarSolMinTime(Requests req, Servers ser){
		for(int i = 0; i < req.size(); ++i){
			int fileId = req.getRequest(i)[1];
			int userId = req.getRequest(i)[0];
			Set<Integer> fileLocations = ser.fileLocations(fileId);
			int[] values = cercaMin(fileLocations,ser,fileId,userId);
			int serAct = values[0];
			int timeAct = values[1];
			peticions[i] = serAct;
			tempsServidors[serAct] = values[1];
		}
	}

	//Funcions auxiliars.
	/**
	 * Inicialitza l'array de peticions i el de temps.
	 */
	private void initArrays(){
		for(int i = 0; i < peticions.length; ++i){
			peticions[i] = -1;
		}
		for(int i = 0; i < tempsServidors.length; ++i){
			tempsServidors[i] = 0;
		}
	}

	/**
	 *
	 * @param fileLocations
	 * @param ser
	 * @param fileId
     * @return un parell on el primer és el temps minim i l'altre el servidor més proper.
     */
	private int[] cercaMin(Set<Integer> fileLocations, Servers ser, int fileId, int UserId){
		int minTime = Integer.MAX_VALUE;
		int serMin = -1;
		while (fileLocations.iterator().hasNext()){
			int serAct = fileLocations.iterator().next();
			int timeAct = ser.tranmissionTime(serAct,UserId);
			if(timeAct < minTime){
				minTime = timeAct;
				serMin = serAct;
			}
		}
		int[] retValues = new int[2];
		retValues[0] = serMin;
		retValues[1] = minTime;
		return retValues;
	}

	/**
	 *
	 * @param fileLocations
	 * @param ser
	 * @param fileId
	 * @param UserId
	 * @param tempsMax
     * @return Primer paràmetre: servidor assignat, segon paràmetre: temps de trans de l'assignacio, tercer: nou temps max.
     */
	private int[] cercaPrimer(Set<Integer> fileLocations,Servers ser, int fileId,int UserId, int tempsMax){
		int tempsAux = Integer.MAX_VALUE;
		int serAux = -1;
		int timeSerAux = Integer.MAX_VALUE;
		while (fileLocations.iterator().hasNext()){
			int serAct = fileLocations.iterator().next();
			int timeSer = tempsServidors[serAct];
			int timeAct = ser.tranmissionTime(serAct,UserId);
			if((timeAct + timeSer) <= tempsMax){
				int[] retValues = new int[3];
				retValues[0] = serAct;
				retValues[1] = timeAct;
				retValues[2] = tempsMax;
				return retValues;
			}
			else{
					if((timeAct + timeSer) < tempsAux) {
						timeSerAux = timeSer;
						tempsAux = timeAct + timeSer;
						serAux = serAct;
					}
				}
			}
		int[] retValues = new int[3];
		retValues[0] = serAux;
		retValues[1] = timeSerAux;
		retValues[2] = tempsAux;
		return retValues;
	}

	/**
	 *
	 * @param idServer id del servidor sol·licitat.
	 * @return el temps total de transmissio del servidor sol·licitat.
	 */
	public int getTime(int idServer){
		return tempsServidors[idServer];
	}
	/**
	 *
	 * @return el nombre de servidors.
	 */
	public int serversNum(){return tempsServidors.length;}

}
