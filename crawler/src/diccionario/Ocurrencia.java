package diccionario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class Ocurrencia implements Serializable {

	private static final long serialVersionUID = 1L;
	private int num_frec = 0;
	private TreeMap<String, Integer> ocurrencia = new TreeMap<String, Integer>();

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

	public void mostrar() {
		List<String> claves = new ArrayList<String>(ocurrencia.keySet());
		Collections.sort(claves);

		Iterator<String> i = claves.iterator();
		System.out.println("Número de archivos: " + ocurrencia.size());
		System.out.println("Ocurrencias totales: " + num_frec);
		while (i.hasNext()) {
			Object k = i.next();
			System.out.println("Fichero: " + k + " --- Ocurrencias: " + ocurrencia.get(k));
		}
	}

}