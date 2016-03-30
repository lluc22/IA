package Domini;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import IA.DistFS.*;
public class Estat {
	public int[] mPeticions;
	public int[] mTempsServidors;
	public Requests mRequests;
	public Servers mServers;

	public Estat(Requests requests,Servers servers, int numGenIni){
		mRequests = requests;
		mServers = servers;
		mPeticions = new int[mRequests.size()];
		mTempsServidors = new int[mServers.size()];
		initArrays();
		if(numGenIni == 1){generaSolNaif(mRequests, mServers);}
		else if(numGenIni == 2) {generarSolMaxTime(mRequests, mServers);}
		else if(numGenIni == 3) {generarSolMinTime(mRequests, mServers);}
	}

	public Estat(Requests requests, Servers servers, int[] peticions, int[] tempsServidors){
		mRequests = requests;
		mServers = servers;
		mPeticions = peticions.clone();
		mTempsServidors = tempsServidors.clone();
	}

	//Operadors
	public void assigna (int pet, int serv) {
		int usuari = mRequests.getRequest(pet)[0];
		int servAntic = mPeticions[pet];
		mTempsServidors[servAntic] -= mServers.tranmissionTime(servAntic, usuari);
		mPeticions[pet] = serv;
		mTempsServidors[serv] += mServers.tranmissionTime(serv, usuari);
	}

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


	public boolean potAssignar (int pet, int serv) {
		int fileId = mRequests.getRequest(pet)[1];
		Set<Integer> fileLocations = mServers.fileLocations(fileId);
		int servId = fileLocations.iterator().next();
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

	public void generarSolMaxTime(Requests req, Servers ser){
		int fileId0 = req.getRequest(0)[1];
		int userId0 = req.getRequest(0)[0];
		Set<Integer> fileLocations0 = ser.fileLocations(fileId0);
		int[] firstIt = cercaMin(fileLocations0,ser,fileId0,userId0);
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
	private void generarSolMinTime(Requests req, Servers ser){
		for(int i = 0; i < req.size(); ++i){
			int fileId = req.getRequest(i)[1];
			int userId = req.getRequest(i)[0];
			Set<Integer> fileLocations = ser.fileLocations(fileId);
			int[] values = cercaMin(fileLocations,ser,fileId,userId);
			int serAct = values[0];
			int timeAct = values[1];
			mPeticions[i] = serAct;
			mTempsServidors[serAct] = values[1];
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
	 * @param fileId
     * @return un parell on el primer és el temps minim i l'altre el servidor més proper.
     */
	private int[] cercaMin(Set<Integer> fileLocations, Servers ser, int fileId, int UserId){
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
		int timeSerAux = Integer.MAX_VALUE;
		for(Iterator it = fileLocations.iterator(); it.hasNext();){
			int serAct = (int) it.next();
			int timeSer = mTempsServidors[serAct];
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
		return mTempsServidors[idServer];
	}



}
