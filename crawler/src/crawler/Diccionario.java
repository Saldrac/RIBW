package crawler;

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
	private static Thesauro thesauro = new Thesauro("thesauro");
	private static TreeMap<String, Integer> contPalabras = new TreeMap<String, Integer>();

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
		Object ret = null;

		int palabras = 0;
		String path = "";
		try {
			for (File fichero : archivos) {
				br = new BufferedReader(new FileReader(
						fichero.getAbsolutePath()));
				path = fichero.getAbsolutePath();
				while ((linea = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(linea);
					while (st.hasMoreTokens()) {
						palabra = Thesauro.normalizar(st.nextToken());
						if (thesauro.buscar(palabra)) {
							ret = diccionario.get(palabra);
							if (ret == null) {
								diccionario.put(
										palabra,
										new Ocurrencia(fichero
												.getAbsolutePath()));
							} else {
								diccionario.put(
										palabra,
										new Ocurrencia(fichero
												.getAbsolutePath(),
												(Ocurrencia) ret));
							}

							palabras++;
						}

					}
				}
				contPalabras.put(path, new Integer(palabras));
				palabras = 0;
				path = "";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void crearDiccionario(String directorio) {
		long startTime = System.currentTimeMillis();
		List<File> listaArchivos = new ArrayList<File>();
		getArchivos(directorio, listaArchivos);
		guardarPalabras(listaArchivos);
		long endTime = System.currentTimeMillis();
		System.out.println("Tiempo transcurrido: " + (endTime - startTime)
				+ " ms");
	}

	public static void mostrar() {

		List<String> claves = new ArrayList<String>(diccionario.keySet());
		Collections.sort(claves);
		Iterator<String> i = claves.iterator();
		System.out.println("**********************");
		while (i.hasNext()) {
			Object k = i.next();
			System.out.println("Token: " + k);
			diccionario.get(k).mostrar(contPalabras);
			System.out.println("**********************");
		}

	}

	public static void guardarDiccionario(String nombre) {
		try {
			FileOutputStream fos = new FileOutputStream(nombre);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(diccionario);
			oos.close();

			fos = new FileOutputStream(nombre + "_wc");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(contPalabras);
			oos.close();
		} catch (Exception e) {
			System.out.println("No se ha podido guardar");
		}
	}

	@SuppressWarnings("unchecked")
	public static void cargarDiccionario(String nombre) {
		try {
			FileInputStream fis = new FileInputStream(nombre);
			ObjectInputStream ois = new ObjectInputStream(fis);
			diccionario = (TreeMap<String, Ocurrencia>) ois.readObject();
			ois.close();

			fis = new FileInputStream(nombre+"_wc");
			ois = new ObjectInputStream(fis);
			contPalabras = (TreeMap<String, Integer>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			System.out.println("No se ha podido cargar");
		}
	}

	public static void buscar(String palabra) {
		Ocurrencia ret = null;
		System.out.println("Token: " + palabra);
		if ((ret = diccionario.get(palabra)) != null) {
			ret.mostrar(contPalabras);

		} else {
			System.out.println("La palabra " + palabra
					+ " no se encuentra en el diccionario");
		}
	}
	
	public static void busqueda_mult(String palabra1, String palabra2) {
		System.out.println("Tokens: " + palabra1 + " " + palabra2);
		Ocurrencia ret1 = diccionario.get(palabra1);
		Ocurrencia ret2 = diccionario.get(palabra2);
		List<Ranking> listRet1 = null;
		List<Ranking> listRet2 = null;
		TreeMap<String, Integer> ocurrencia1 = null;
		TreeMap<String, Integer> ocurrencia2 = null;
		Ranking r1 = null;
		Ranking r2 = null;

		if ((ret1 != null) && (ret2 != null)) {
			listRet1 = ret1.getRanking(contPalabras);
			listRet2 = ret2.getRanking(contPalabras);
			ocurrencia1 = ret1.getOcurrencias();
			ocurrencia2 = ret2.getOcurrencias();

			for(int i=0,j=0;i<listRet1.size()&&j<listRet2.size();i++,j++){
				r1 = listRet1.get(i);
				r2 = listRet2.get(j);
				if(r1.getPath().compareTo(r1.getPath()) == 0){
					System.out.println("Fichero: " + r1.getPath() + " --- Ocurrencias: "
							+ (ocurrencia1.get(r1.getPath()) + ocurrencia2.get(r2.getPath())) + " --- Ranking: "
							+ (r2.getResult() + r1.getResult()));
				}
				
			}
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
			System.out.println("5. Buscar");
			System.out.println("6. Búsqueda múltiple");
			System.out.println("7. Salir");
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
				System.out.print("Introduzca palabra: ");
				buscar(in.next());
				break;
			case 6:
				System.out.print("Introduzca palabras (máximo 2): ");
				busqueda_mult(in.next(), in.next());
				in.nextLine();
				break;
			case 7:
				salir = true;
				break;
			default:
				break;
			}

		}

		in.close();

	}
}
