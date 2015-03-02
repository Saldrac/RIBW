/******************************************************************************** 
 * FichContPalabras.java														*
 *  Contabiliza palabras contenidas en un fichero								*
 * (i) F�lix R. Rodr�guez, GIM, Universidad de Extremadura, 2009				*
 * 						   http://gim.unex.es/felixr							*
 *******************************************************************************/

import java.io.*;
import java.util.*;

public class FichContPalabras {
        public static void contar (String args[]) throws IOException {
                if (args.length < 2) {
                        System.out.println ("ERROR=>Utilice: java FichContPalabras fichero_entrada fichero_salida");
                        return;
                }

                String fichEntrada = args[0];
                String fichSalida = args[1];

                Map map = new TreeMap ();
                BufferedReader br = new BufferedReader (new FileReader (fichEntrada));
                String linea;

                while ( (linea = br.readLine () ) != null) {
                	//XXX: Tiene que haber un cierre de Kleene (*) al final.
                	String pattern = "[-]*[\"]*[,]*[.]*[!]*[�]*[?]*[�]*";
                	//String pattern = "^[a-zA-Z0-9_ ]*$";
                	linea = linea.replaceAll(pattern, "");
                	
                        StringTokenizer st = new StringTokenizer (linea);
                        while (st.hasMoreTokens () ) {
                                String s = st.nextToken();
                                Object o = map.get(s);
                                if (o == null) map.put (s, new Integer (1));
                                else {
                                        Integer cont = (Integer) o;
                                        map.put (s, new Integer (cont.intValue () + 1));
                                }
                        }
                }
                br.close ();

                List claves = new ArrayList (map.keySet ());
                Collections.sort (claves);

                PrintWriter pr = new PrintWriter (new FileWriter (fichSalida));
                Iterator i = claves.iterator ();
                while (i.hasNext ()) {
                        Object k = i.next ();
                        pr.println (k + " : " + map.get (k));
                }
                pr.close ();

        }
}