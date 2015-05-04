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
	private static final float PARCIAL_TOTAL = (float) 1.8; //+80%
	private static final float TOTAL_TAM = (float) 1.30; //+30%
	private static final float ULTIMO_ACCS = (float) -0.25; //25%

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

	private float calcularTiempoModificacion(File fichero) {
		int diasHoy = (int) TimeUnit.MILLISECONDS.toDays(System
				.currentTimeMillis());
		int diasMod = (int) TimeUnit.MILLISECONDS.toDays(fichero
				.lastModified());
		return (float) (diasMod / diasHoy) / 365;
	}

	private float calcularValorRank(String url) {
		File fichero = new File(url);
		float parcial = (float) ocurrencia.get(url) / num_frec;
		float tam = (float) num_frec / (fichero.length() / 1024 * 1024);
		
		return parcial * PARCIAL_TOTAL + tam * TOTAL_TAM
				+ calcularTiempoModificacion(fichero) * ULTIMO_ACCS;

	}

	public void mostrar() {
		long startTime = System.currentTimeMillis();
		System.out.println("Número de archivos: " + ocurrencia.size());
		System.out.println("Ocurrencias totales: " + num_frec);
		List claves = new ArrayList(ocurrencia.keySet());
		Collections.sort(claves);
		Iterator i = claves.iterator();

		while (i.hasNext()) {
			Object k = i.next();
			updateRanking((String) k);
			ranking.add(new Ranking((String) k, calcularValorRank((String) k)));
		}

		Collections.sort(ranking);
		for (Ranking r : ranking) {
			System.out.println("Fichero: " + r.getPath() + " --- Ocurrencias: "
					+ ocurrencia.get(r.getPath()) + " --- Ranking: "
					+ r.getResult());
		}
		
		long endTime   = System.currentTimeMillis();
		System.out.println("Tiempo transcurrido: " + (endTime - startTime) + " ms");

	}
}