/******************************************************************************** 
 * ListIt.java																	*
 *  Imprime caracter�sticas de ficheros											*
 * (c) F�lix R. Rodr�guez, GIM, Universidad de Extremadura, 2009				*
 * 						   http://gim.unex.es/felixr							*
 *******************************************************************************/


import java.io.*;

class ListIt {
public static String [] arguments;
	private void crawlDir(String dir, String output) {
		File fichero = new File(dir);
		
		 if (!fichero.exists() || !fichero.canRead()) {
		 System.out.println("No puedo leer " + fichero);
		
		 }
		if (fichero.isDirectory()) {
			String[] listaFicheros = fichero.list();
			for (int i = 0; i < listaFicheros.length; i++) {
				System.out.println(listaFicheros[i]);
				//En el iterativo estructura lineal sin apendear / (se va onstruyendo solo)
				crawlDir(fichero.getAbsolutePath() + "\\" + listaFicheros[i], output);
			}
		} else {
			if(fichero.getPath().endsWith(".txt"))
			{
				try {
				FileReader fr = new FileReader(fichero);
				BufferedReader br = new BufferedReader(fr);
				String [] vector = new String[2];
				vector[0] = new String(fichero.getAbsolutePath());
				vector[1] = new String(output);
				FichContPalabras.contar(vector);
			} catch (FileNotFoundException fnfe) {
				System.out.println("Fichero desaparecido en combate  ;-)");
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

		// dir File fichero = new File(args[0]);
		// if (!fichero.exists() || !fichero.canRead()) {
		// System.out.println("No puedo leer " + fichero);
		// return;
		// }
		// if (fichero.isDirectory()) {
		// String [] listaFicheros = fichero.list();
		// for (int i=0; i<listaFicheros.length; i++){
		// System.out.println(listaFicheros[i]);
		// }
		// }
		//
		// try {
		// FileReader fr = new FileReader(fichero);
		// BufferedReader br = new BufferedReader(fr);
		// String linea;
		// while ((linea=br.readLine()) != null)
		// System.out.println(linea);
		// }
		// catch (FileNotFoundException fnfe) {
		// System.out.println("Fichero desaparecido en combate  ;-)");
		// }
		// }
	}
}
