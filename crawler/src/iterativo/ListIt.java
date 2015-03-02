/******************************************************************************** 
 * ListIt.java																	*
 *  Imprime caracter�sticas de ficheros											*
 * (c) F�lix R. Rodr�guez, GIM, Universidad de Extremadura, 2009				*
 * 						   http://gim.unex.es/felixr							*
 *******************************************************************************/

package iterativo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ListIt {
	private void crawlDir(String dir, List<File> listArchivos) {
		File fichero = new File(dir);

		if (!fichero.exists() || !fichero.canRead()) {
			System.out.println("No puedo leer " + fichero);

		}
		if (fichero.isDirectory()) {
			String[] listaFicheros = fichero.list();
			for (int i = 0; i < listaFicheros.length; i++) {
				crawlDir(fichero.getPath() + "//" + listaFicheros[i],listArchivos);
			}
		} else {
			if (fichero.getPath().endsWith(".txt")) {
				listArchivos.add(fichero);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("ERROR. formato: >java ListIt nombre_archivo");
			return;
		}
		ListIt lister = new ListIt();
		List<File> lista = new ArrayList<File>();
		lister.crawlDir(args[0],lista);
		
		for(File f: lista){
			FichContPalabras.contar(f.getAbsolutePath(), args[1]);
		}
	
	}
}
