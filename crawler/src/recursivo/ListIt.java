/******************************************************************************** 
 * ListIt.java																	*
 *  Imprime caracter�sticas de ficheros											*
 * (c) F�lix R. Rodr�guez, GIM, Universidad de Extremadura, 2009				*
 * 						   http://gim.unex.es/felixr							*
 *******************************************************************************/

package recursivo;

import java.io.*;

class ListIt {
	private void crawlDir(String dir, String output) {
		File fichero = new File(dir);

		if (!fichero.exists() || !fichero.canRead()) {
			System.out.println("No puedo leer " + fichero);

		}
		if (fichero.isDirectory()) {
			String[] listaFicheros = fichero.list();
			for (int i = 0; i < listaFicheros.length; i++) {
				crawlDir(fichero.getPath() + "//" + listaFicheros[i], output);
			}
		} else {
			if (fichero.getPath().endsWith(".txt")) {
				try {
					FichContPalabras.contar(fichero.getAbsolutePath(), output);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("ERROR. formato: >java ListIt nombre_archivo");
			return;
		}
		ListIt lister = new ListIt();
		lister.crawlDir(args[0], args[1]);
	}
}
