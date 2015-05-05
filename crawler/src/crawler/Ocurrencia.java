package crawler;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class Ocurrencia implements Serializable {

	private static final long serialVersionUID = 1L;
	private int num_frec = 0;
	private TreeMap<String, Integer> ocurrencia = new TreeMap<String, Integer>();
	private List<Ranking> ranking = new ArrayList<Ranking>();

	private static final float PARCIAL_TOTAL = (float) 1.8; // +80%
	private static final float ULTIMO_ACCS = (float) -1.25; // -25%

	private static final float UMBRAL_SPAM_1 = (float) 0.3;
	private static final float UMBRAL_SPAM_2 = (float) 0.5;
	private static final float UMBRAL_SPAM_3 = (float) 0.9;

	private static final float UMBRAL_MOD_1 = 15;
	private static final float UMBRAL_MOD_2 = 60;
	private static final float UMBRAL_MOD_3 = 365;

	private static final float UMBRAL_OCUR_1 = (float) 0.3;
	private static final float UMBRAL_OCUR_2 = (float) 0.5;
	private static final float UMBRAL_OCUR_3 = (float) 0.7;

	public Ocurrencia(String url) {
		insOcurrencia(url);
	}

	public Ocurrencia(String url, Ocurrencia oc) {
		this.num_frec = oc.num_frec;
		this.ocurrencia = oc.ocurrencia;
		insOcurrencia(url);
	}

	public void insOcurrencia(String url) {
		Object ret = null;
		Integer cont = null;
		if ((ret = ocurrencia.get(url)) == null) {
			ocurrencia.put(url, new Integer(1));
		} else {
			cont = (Integer) ret;
			ocurrencia.put(url, new Integer(cont.intValue() + 1));
		}
		num_frec++;
	}

	private void updateRanking(String url) {
		boolean enc = false;

		for (int i = 0; i < ranking.size() && !enc; i++) {
			if (ranking.get(i).getPath().compareTo(url) == 0) {
				enc = true;
				ranking.remove(i);
			}
		}
	}

	private int rankingSpam(float valor) {
		int calculo = 0;
		if (valor <= UMBRAL_SPAM_1) {
			calculo += 4;
		} else if (valor <= UMBRAL_SPAM_2) {
			calculo += 2;
		} else if(valor <= UMBRAL_SPAM_3 || valor > UMBRAL_SPAM_3){
			calculo -= 7;
		}

		return calculo;
	}

	private int rankingModif(int valor) {
		int calculo = 0;
		if (valor <= UMBRAL_MOD_1) {
			calculo += 4;
		} else if (valor <= UMBRAL_MOD_2) {
			calculo += 2;
		} else if(valor <= UMBRAL_MOD_3 || valor > UMBRAL_MOD_3){
			calculo -= 3;
		}

		return calculo;
	}

	private int rankingApariciones(float valor) {
		int calculo = 0;
		if (valor <= UMBRAL_OCUR_1) {
			calculo += 1;
		} else if (valor <= UMBRAL_OCUR_2) {
			calculo += 2;
		} else if(valor <= UMBRAL_OCUR_3 || valor > UMBRAL_OCUR_3){
			calculo += 3;
		}

		return calculo;
	}

	private float calcularValorRank(String url,
			TreeMap<String, Integer> contPalabras) {
		File fichero = new File(url);
		float parcial = (float) ocurrencia.get(url) / num_frec;
		float spam = (float) ocurrencia.get(url) / contPalabras.get(url);
		int diasMod = (int) TimeUnit.MILLISECONDS
				.toDays(fichero.lastModified());
		int diasHoy = (int) TimeUnit.MILLISECONDS.toDays(System
				.currentTimeMillis());
		return rankingApariciones(parcial) + rankingModif(Math.abs(diasHoy - diasMod))
				+ rankingSpam(spam);

	}

	public void mostrar(TreeMap<String, Integer> contPalabras) {
		System.out.println("Número de archivos: " + ocurrencia.size());
		System.out.println("Ocurrencias totales: " + num_frec);
		List claves = new ArrayList(ocurrencia.keySet());
		Collections.sort(claves);
		Iterator i = claves.iterator();

		while (i.hasNext()) {
			Object k = i.next();
			updateRanking((String) k);
			ranking.add(new Ranking((String) k, calcularValorRank((String) k,
					contPalabras)));
		}

		Collections.sort(ranking);
		for (Ranking r : ranking) {
			System.out.println("Fichero: " + r.getPath() + " --- Ocurrencias: "
					+ ocurrencia.get(r.getPath()) + " --- Ranking: "
					+ r.getResult());
		}
	}
	
	public List<Ranking> getRanking(TreeMap<String, Integer> contPalabras){
		List claves = new ArrayList(ocurrencia.keySet());
		Collections.sort(claves);
		Iterator i = claves.iterator();

		while (i.hasNext()) {
			Object k = i.next();
			updateRanking((String) k);
			ranking.add(new Ranking((String) k, calcularValorRank((String) k,
					contPalabras)));
		}

		Collections.sort(ranking);
		return ranking;
	}
	
	public int getTotalOcurrencias(){
		return ocurrencia.size();
	}
	
	public int getNumFrec(){
		return num_frec;
	}
	
	public TreeMap<String, Integer> getOcurrencias(){
		return ocurrencia;
	}
	
	
}