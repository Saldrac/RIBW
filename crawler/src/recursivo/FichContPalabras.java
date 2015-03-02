/******************************************************************************** 
 * FichContPalabras.java														*
 *  Contabiliza palabras contenidas en un fichero								*
 * (i) F�lix R. Rodr�guez, GIM, Universidad de Extremadura, 2009				*
 * 						   http://gim.unex.es/felixr							*
 *******************************************************************************/

package recursivo;

import java.io.*;
import java.util.*;

public class FichContPalabras {

	public static void contar(String entrada, String salida) throws IOException {
		Map map = new TreeMap();
		BufferedReader br = new BufferedReader(new FileReader(entrada));
		String linea;

		while ((linea = br.readLine()) != null) {

			String pattern = "[-]*[\"]*[,]*[.]*[¿]*[?]*[¡]*[!]*";
			linea = linea.replaceAll(pattern, "");

			StringTokenizer st = new StringTokenizer(linea);
			while (st.hasMoreTokens()) {
				String s = st.nextToken();
				Object o = map.get(s);
				if (o == null)
					map.put(s, new Integer(1));
				else {
					Integer cont = (Integer) o;
					map.put(s, new Integer(cont.intValue() + 1));
				}
			}
		}
		br.close();

		List claves = new ArrayList(map.keySet());
		Collections.sort(claves);

		PrintWriter pr = new PrintWriter(new FileWriter(salida));
		Iterator i = claves.iterator();
		while (i.hasNext()) {
			Object k = i.next();
			pr.println(k + " : " + map.get(k));
		}
		pr.close();

	}
}