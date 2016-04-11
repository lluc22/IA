package Domini;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import IA.DistFS.*;

/**
 * Classe utilitzada per representar l'estat.
 */
public class Estat {
	public int[] mPeticions;
	public int[] mTempsServidors;
	public Requests mRequests;
	public Servers mServers;

	/**
	 * Constructora utilitzada per a crear una instància d'estat inicial.
	 * @param requests peticions generades
	 * @param servers Servidors generats
	 * @param numGenIni numero de solució inicial(1:naif, 2: llindar màxim, 3:temps mínim)
     * @param nserv nombre de servidors generats.
     */
	public Estat(Requests requests,Servers servers, int numGenIni, int nserv){
		mRequests = requests;
		mServers = servers;
		mPeticions = new int[mRequests.size()];
		mTempsServidors = new int[nserv];
		//initArrays();
		if(numGenIni == 1){generaSolNaif(mRequests, mServers);}
		else if(numGenIni == 2) {generarSolMaxTime(mRequests, mServers);}
		else if(numGenIni == 3) {generarSolMinTime(mRequests, mServers);}
	}

	/**
	 * Constructora que s'usa per a guardar-nos un estat qualssevol que no és inicial.
	 * @param requests peticions generades
	 * @param servers Servidors generats
	 * @param peticions vector de peticions actuals
	 *  @param tempsServidors vector de temps actuals
     */
	public Estat(Requests requests, Servers servers, int[] peticions, int[] tempsServidors){
		mRequests = requests;
		mServers = servers;
		mPeticions = peticions.clone();
		mTempsServidors = tempsServidors.clone();
	}

	//Operadors

	/**
	 * Operador que assigna una petició amb id=pet a un servidor amb id=serv. Actualitza els temps dels servidors.
	 * @param pet id de la petició.
	 * @param serv id del servidor.
     */
	public void assigna (int pet, int serv) {
		int usuari = mRequests.getRequest(pet)[0];
		int servAntic = mPeticions[pet];
		mTempsServidors[servAntic] -= mServers.tranmissionTime(servAntic, usuari);
		mPeticions[pet] = serv;
		mTempsServidors[serv] += mServers.tranmissionTime(serv, usuari);
	}

	/**
	 * Operador que intercanvia l'assignació d'una petició amb id=pet1 amb una altra amb id=pet2.
	 * @param pet1 id de la primera petició
	 * @param pet2 id de la segona petició
     */
	public void intercanvia (int pet1, int pet2) {
		int tempsAntic1 = mServers.tranmissionTime(mPeticions[pet1], mRequests.getRequest(pet1)[0]);
		int tempsAntic2 = mServers.tranmissionTime(mPeticions[pet2], mRequests.getRequest(pet2)[0]);
		int tempsNou1 = mServers.tranmissionTime(mPeticions[pet2], mRequests.getRequest(pet1)[0]);
		int tempsNou2 = mServers.tranmissionTime(mPeticions[pet1], mRequests.getRequest(pet2)[0]);
		mTempsServidors[mPeticions[pet1]] += tempsNou2 - tempsAntic1;
		mTempsServidors[mPeticions[pet2]] += tempsNou1 - tempsAntic2;
		int aux = mPeticions[pet1];
		mPeticions[pet1] = mPeticions[pet2];
		mPeticions[pet2] = aux;
	}

	/**
	 * Funció que comprova si es pot emprar l'operador assignar donats una petició i un servidor.
	 * @param pet id de la petició
	 * @param serv id del servidor.
     * @return cert, si l'assignació entre petició amb id=pet i el servior amb id=serv es pot realitzar.
     */
	public boolean potAssignar (int pet, int serv) {
		int fileId = mRequests.getRequest(pet)[1];
		Set<Integer> fileLocations = mServers.fileLocations(fileId);
		int sO = mPeticions[pet];
		if(sO == serv) return false;
		boolean found = false;
		for(Iterator it = fileLocations.iterator(); it.hasNext() && !found;){
				if (serv == (int)it.next()) found = true;
			}
		return found;
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
			mPeticions[i] = servId; //s'assigna el servidor a la peticio i-esima.

			int userId = req.getRequest(i)[0];
			int transTime = ser.tranmissionTime(servId,userId);
			mTempsServidors[servId]+=transTime; //sumem el temps de transmissio de la peticio assignada.
		}
	}

	/**
	 * Genera una soluci'inicial amb un temps màxim llindar, que totes les assignacions han de ser inferiors a aquest llindar.
	 * @param req peticions demanades
	 * @param ser servidors actuals
     */
	public void generarSolMaxTime(Requests req, Servers ser){
		int fileId0 = req.getRequest(0)[1];
		int userId0 = req.getRequest(0)[0];
		Set<Integer> fileLocations0 = ser.fileLocations(fileId0);
		int[] firstIt = cercaMin(fileLocations0,ser,userId0);
		int firstSer = firstIt[0];
		mPeticions[0] = firstSer;
		mTempsServidors[firstSer] = firstIt[1];
		int maxTime = firstIt[1]; //Iniciem el temps màxim amb l'assignacio de la primera peticio.
		for(int i = 1; i < req.size(); ++i){
			int fileId = req.getRequest(i)[1];
			int userId = req.getRequest(i)[0];
			Set<Integer> fileLocations = ser.fileLocations(fileId);
			int[] values = cercaPrimer(fileLocations,ser,fileId,userId,maxTime);
			mPeticions[i] = values[0];
			int serV = values[0];
			mTempsServidors[serV] += values[1];
			maxTime = values[2];
		}
	}

	/**
	 * Genera una solució assignant cada petició al servidor més proper que conté el fitxer
	 * @param req peticions demandades
	 * @param ser servidors actuals
     */
	public void generarSolMinTime(Requests req, Servers ser){
		for(int i = 0; i < req.size(); ++i){
			int[] values = cercaMin(ser.fileLocations(req.getRequest(i)[1]),ser,req.getRequest(i)[0]);
			mPeticions[i] = values[0];
			mTempsServidors[values[0]] += values[1];
		}
	}

	//Funcions auxiliars.
	/**
	 * Inicialitza l'array de peticions i el de temps.
	 */
	private void initArrays(){
		for(int i = 0; i < mPeticions.length; ++i){
			mPeticions[i] = -1;
		}
		for(int i = 0; i < mTempsServidors.length; ++i){
			mTempsServidors[i] = 0;
		}
	}

	/**
	 *
	 * @param fileLocations
	 * @param ser
     * @return un parell on el primer és el temps minim i l'altre el servidor més proper.
     */
	private int[] cercaMin(Set<Integer> fileLocations, Servers ser, int UserId){
		int minTime = Integer.MAX_VALUE;
		int serMin = -1;
		for(Iterator it = fileLocations.iterator(); it.hasNext();){
            int serAct = (int) it.next();
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
		int timeAct = 0;
		int timeSerAux = Integer.MAX_VALUE;
		for(Iterator it = fileLocations.iterator(); it.hasNext();){
			int serAct = (int) it.next();
			int timeSer = mTempsServidors[serAct];
			timeAct = ser.tranmissionTime(serAct,UserId);
			if((timeAct + timeSer) <= tempsMax){
				int[] retValues = new int[3];
				retValues[0] = serAct;
				retValues[1] = timeAct;
				retValues[2] = tempsMax;
				return retValues;
			}
			else{
					if((timeAct + timeSer) < tempsAux) {
						timeSerAux = timeAct;
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
	/*public int getTime(int idServer){
		return mTempsServidors[idServer];
	}*/



}
