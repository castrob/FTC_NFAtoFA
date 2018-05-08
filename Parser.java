/**
** Pontifícia Universidade Catolica de Minas Gerais
** Fundamentos Teoricos da Computacao - Ciencia da Computacao - ICEI
** Trabalho Pratico 01 - Professor Zenilton
** Alunos: Joao Paulo de Castro Bento Pereira
**		   Paulo Junio Reis Rodrigues
**Objetivo:
** Construir um conversor/Parser de Automato Finito Nao Deterministico para
** um Automato Finito Deterministico a ser tState no programa JFLAP V7.0
**
***/

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

class Parser{

	/**
	** Main Class to make the conversion
	** Created by João Castro - April 6
	** zzz 
	**  zzzz 
	**   zzzzz z zzz 
	***/

	/**
	 * Function to parse a String line into a State
	 * @param String line - input string from XML
	 * @return State e - State completed parsed
	 */
		public static State parseState(String line){

		/* Removing characters */
		line = line.replaceAll("state", "");
		line = line.replaceAll("/+", "");
		line = line.replaceAll("<+", "");
		line = line.replaceAll(">+", "");
		line = line.replaceAll(" +", "");
		line = line.replaceAll("=+", "/");
		line = line.replaceAll("id+", "");
		line = line.replaceAll("name+", "");
		line = line.replaceAll("initial+", "I");
		line = line.replaceAll("final+", "F");
		line = line.replaceAll("x+", "/");
		line = line.replaceAll("y+", "/");
		line = line.replaceAll("\"+", "");
		line = line.replaceAll("//", "/");
		line = line.replaceAll("\t+","");


		String[] array = line.split("/");

		int id = Integer.parseInt(array[1]);
		String name = array[2];
		double x = Double.parseDouble(array[3]);
		double y = Double.parseDouble(array[4]);
		char type = 'c';

		if(array.length > 5)
		  type = array[5].charAt(0);

		State e = new State(name, id, x, y, type);

		return e;
	}

	/**
	 * Function to parse a String line into a Transition
	 * @param String line - input string from XML
	 * @return Transition t - Transition completed parsed
	 */

	public static Transition parseTransition(String line, FiniteAutomata fa){

		/* Removing characters */
		line = line.replaceAll("transition", "");
		line = line.replaceAll("/+", "");
		line = line.replaceAll("<+", "");
		line = line.replaceAll(">+", "");
		line = line.replaceAll(" +", "");
		line = line.replaceAll("from+", "/");
		line = line.replaceAll("to+", "/");
		line = line.replaceAll("read+", "");
		line = line.replaceAll("//", "/");
		line = line.replaceAll("\t+","");

		String[] array = line.split("/");
		State from = fa.getState(Integer.parseInt(array[1]));
		State to = fa.getState(Integer.parseInt(array[2]));
		char read = array[3].charAt(0);

		Transition t = new Transition(from, to,read);
		System.out.println(t);

		return t;
	}




	public static void main (String[]args){
		/*Input Variables*/
		BufferedReader br = null;
		FileReader fr = null;
		String line;
		FiniteAutomata fa = new FiniteAutomata();

		// parse from FiniteAutomataN
		try{
			fr = new FileReader("afn.jff");
			br = new BufferedReader(fr);

			/*Reading the entire file*/
			while((line = br.readLine()) != null){

				/* parsing states */
				if(line.contains("<state ")){

					String s = line;

					while ((line = br.readLine()) != null && (!line.contains("</state>")))
						s = s+line;

					s = s.replaceAll("\t+", "").trim();
					fa.states.add(parseState(s));
					s = "";
				}

				/* parsing transitions */
				if(line.contains("<transition>")){
					
					String s = line;
					
					while ((line = br.readLine()) != null && (!line.contains("</transition>")))
						s = s+line;

					s = s.replaceAll("\t+", "").trim();
					fa.transitions.add(parseTransition(s, fa));
					s = "";
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
			if(br != null) br.close();
			if(fr != null) fr.close();
			}catch(Exception i){
				i.printStackTrace();
			}
		}

		// Convert to DFA



		}
}