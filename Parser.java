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
import java.io.FileWriter;
import java.io.PrintWriter;

class Parser{

	int states;

	/**
	* Main Class to make the conversion
	* Created by João Castro - April 6
	*/

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
		String name = array[2].replaceAll("q","");
		double x = Double.parseDouble(array[3]);
		double y = Double.parseDouble(array[4]);
		char type = 'c';

		if(array.length > 5){
			if(array[5].equals("IF"))
				type = 'S';
			else{
				type = array[5].charAt(0);
			}

		}

		State e = new State(name, id, x, y, type);
		System.out.println(e);

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
		return t;
	}




	public static void main (String[]args){
		/*Input Variables*/
		BufferedReader br = null;
		FileReader fr = null;
		String line;
		FiniteAutomata fa;
		BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Conversor de Automatos Finitos Nao Deterministicos para Automatos Finitos Deterministicos!!");		
		System.out.println("Favor inserir o nome do arquivo incluindo sua extensão. Ex(afn.jff)");
		// Parse from a JFLAP File
		try{
		String nomeArq = io.readLine();
			fr = new FileReader(nomeArq);
			br = new BufferedReader(fr);
			int states = 0;
			fa = new FiniteAutomata();

			/*Reading the entire file*/
			while((line = br.readLine()) != null){
				/* parsing states */
				if(line.contains("<state ")){
					String s = line;
					while ((line = br.readLine()) != null && (!line.contains("</state>")))
						s = s+line;
					s = s.replaceAll("\t+", "").trim();
					State state = parseState(s);
					state.setName(""+(states++));
					fa.states.add(state);
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

			//Will be needed to conversion
				String alphabet = fa.getAlphabet();
				String[][] transitionTable = fa.getTransitionTable();
				/* if deterministic, give the user option to test words */
				if(fa.isDeterministic()){
					fa.setType("DFA");
					System.out.println("Este Automato ja e' deterministico! Deseja realizar testes ?");
					System.out.println("s/n ?");
					String test = io.readLine();
					if(test.toUpperCase().equals("S")){
						System.out.println("Favor entrar com a palavra desejada!");
						System.out.println("Caso deseja terminar, digite: TERMINAR");	
						System.out.print("Entrada: ");								
						String input = io.readLine();
						while(!input.toUpperCase().equals("TERMINAR")){
							fa.simulate(input);
							System.out.println("Favor entrar com a palavra desejada!");
							System.out.println("Caso deseja terminar, digite: TERMINAR");
							System.out.print("Entrada: ");
							input = io.readLine();
						}
					}
				}else{
					/* if not deterministic, convert and give the user oportunity to run tests */
					System.out.println("Automato nao deterministico! Realizando conversao...");
					fa.setType("NFA");
					// Convert to DFA
					System.out.println("Automato Nao deterministico: ");
					fa.printAutomata();
					fa.printTransTable();
					FiniteAutomata dfa = fa.convert();
					if(dfa.isDeterministic()){
						System.out.println("Automato convertido com sucesso ! ");
						System.out.println("Automato resultado: ");
						dfa.printAutomata();
						dfa.printTransTable();
						System.out.println();
						System.out.println("Deseja realizar testes ?");
						System.out.print("s/n? ");
						String test = io.readLine();
						if(test.toUpperCase().equals("S")){
							System.out.println("Favor entrar com a palavra desejada!");
							System.out.println("Caso deseja terminar, digite: TERMINAR");	
							System.out.print("Entrada: ");											
							String input = io.readLine();
							while(!input.toUpperCase().equals("TERMINAR")){
								dfa.simulate(input);
								System.out.println("Favor entrar com a palavra desejada!");
								System.out.println("Caso deseja terminar, digite: TERMINAR");
								System.out.print("Entrada: ");
								input = io.readLine();
							}
						}
							String novoNomArq = "DFA_" + nomeArq;
							System.out.println("Automato salvo no arquivo: " + novoNomArq);
							FileWriter arq = new FileWriter(novoNomArq);
							PrintWriter gravarArq = new PrintWriter(arq);
							gravarArq.printf(dfa.toXML());
							gravarArq.close();
					}else{
						System.out.println("Nao foi possivel realizar a conversao!");
						System.out.println("Automato resultado continua nao deterministico!");
						dfa.printAutomata();
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
		}
}