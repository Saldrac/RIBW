package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Thesauro {
	private String fichero = null;
	private HashSet<String> palabras = null;

	public static String normalizar(String input) {
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\P{ASCII}");
		return pattern.matcher(normalized).replaceAll("");
	}

	public Thesauro(String fichero) {
		this.fichero = fichero;
		this.palabras = new HashSet<String>();
		crear();
	}

	public boolean buscar(String palabra) {
		return palabras.contains(palabra);
	}

	public void crear() {
		File thesauroFile = new File(this.fichero);
		BufferedReader br = null;
		String linea = null;
		try {
			if (thesauroFile.isFile()) {
				br = new BufferedReader(new FileReader(
						thesauroFile.getAbsolutePath()));

				while ((linea = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(normalizar(linea),
							";");

					while (st.hasMoreTokens()) {
						this.palabras.add(st.nextToken());
					}
				}
			} else {
				System.out.println("El fichero de thesauro no es correcto");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al procesar el fichero del thesauro");
		}
	}

}
