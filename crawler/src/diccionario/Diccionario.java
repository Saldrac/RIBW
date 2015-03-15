package diccionario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Diccionario implements Serializable {

	private static final long serialVersionUID = 1L;
	private static TreeMap<String, Ocurrencia> diccionario = new TreeMap<String, Ocurrencia>();

	public static void getArchivos(String directorio, List<File> archivos) {
		File fichero = new File(directorio);

		if (fichero.isDirectory()) {
			String[] listaFicheros = fichero.list();
			for (int i = 0; i < listaFicheros.length; i++) {
				getArchivos(fichero.getPath() + "//" + listaFicheros[i],
						archivos);
			}
		} else {
			if (fichero.getPath().endsWith(".txt")) {
				archivos.add(fichero);
			}
		}
	}

	public static void guardarPalabras(List<File> archivos) {

		BufferedReader br = null;
		String linea = "";
		String palabra = "";
		String pattern = "[-]*[\"]*[,]*[.]*[¿]*[?]*[¡]*[!]*";
		Object ret = null;
		try {
			for (File fichero : archivos) {
				br = new BufferedReader(new FileReader(
						fichero.getAbsolutePath()));

				while ((linea = br.readLine()) != null) {

					linea = linea.replaceAll(pattern, "");
					StringTokenizer st = new StringTokenizer(linea);

					while (st.hasMoreTokens()) {
						palabra = st.nextToken();
						ret = diccionario.get(palabra);
						if (ret == null) {
							diccionario.put(palabra,
									new Ocurrencia(fichero.getAbsolutePath()));
						} else {
							diccionario.put(palabra,
									new Ocurrencia(fichero.getAbsolutePath(),
											(Ocurrencia) ret));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void crearDiccionario(String directorio) {
		List<File> listaArchivos = new ArrayList<File>();
		getArchivos(directorio, listaArchivos);
		guardarPalabras(listaArchivos);
	}

	public static void mostrar() {

		List<String> claves = new ArrayList<String>(diccionario.keySet());
		Collections.sort(claves);
		Iterator<String> i = claves.iterator();
		System.out.println("**********************");
		while (i.hasNext()) {
			Object k = i.next();
			System.out.println("Token: " + k);
			diccionario.get(k).mostrar();
			System.out.println("**********************");
		}
		
	}

	public static void guardarDiccionario(String nombre) {
		try {
			FileOutputStream fos = new FileOutputStream(nombre);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(diccionario);
			oos.close();
		} catch (Exception e) {
			System.out.println("No se ha podido guardar");
		}
	}

	public static void cargarDiccionario(String nombre) {
		try {
			FileInputStream fis = new FileInputStream(nombre);
			ObjectInputStream ois = new ObjectInputStream(fis);
			diccionario = (TreeMap<String, Ocurrencia>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			System.out.println("No se ha podido cargar");
		}
	}

	public static void main(String[] args) {

		boolean salir = false;
		int selec = 0;
		Scanner in = new Scanner(System.in);

		while (!salir) {
			System.out.println("-----------------");
			System.out.println("1. Crear");
			System.out.println("2. Cargar");
			System.out.println("3. Guardar");
			System.out.println("4. Mostrar");
			System.out.println("5. Salir");
			System.out.println("-----------------");

			selec = in.nextInt();
			switch (selec) {
			case 1:
				System.out.print("Introduzca directorio raíz: ");
				crearDiccionario(in.next());
				break;
			case 2:
				System.out.print("Introduzca el nombre: ");
				cargarDiccionario(in.next());
				break;
			case 3:
				System.out.print("Introduzca el nombre: ");
				guardarDiccionario(in.next());
				break;
			case 4:
				mostrar();
				break;
			case 5:
				salir = true;
				break;
			default:
				break;
			}

		}

		in.close();

	}
}
