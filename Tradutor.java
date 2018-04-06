/**
** Pontifícia Universidade Catolica de Minas Gerais
** Fundamentos Teoricos da Computacao - Ciencia da Computacao - ICEI
** Trabalho Pratico 01 - Professor Zenilton
** Alunos: Joao Paulo de Castro Bento Pereira
**		   Paulo Junio Reis Rodrigues
**Objetivo:
** Construir um conversor/tradutor de Automato Finito Nao Deterministico para
** um Automato Finito Deterministico a ser testado no programa JFLAP V7.0
**
***/

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

class Estado{
	String name;
	int id;
	double x; /* x Position ( Standard 0.0 )*/
	double y; /* y Position ( Standard 0.0 )*/
	char type; /* C = Commom, I = Initial, F = Final */
	/**
	* Standard Constructor
	*/
	public Estado(){
		this.name = "q";
		this.id = 99;
		this.x = 0.0;
		this.y = 0.0;
		this.type = 'c';
	}
	/**
	* Alternative Constructor
	*/
	public Estado(String name, int id, double x, double y, char type){
		this.name = name;
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
	}




	/*Getters and setters*/
	public String getName(){return this.name;}
	public int getId(){return this.id;}
	public double getX(){return this.x;}
	public double getY(){return this.y;}
	public char getType(){return this.type;}


	public void setName(String name){this.name=name;}
	public void setId(int id){this.id=id;}
	public void setX(double x){this.x=x;}
	public void setY(double y){this.y=y;}
	public void setType(char type){this.type=type;}

	public String toString(){
		return "State: " + this.name + "\n" +
			   " id: " + this.id + "\n" +
			   " x: " + this.x + "\n" +
			   " y: " + this.y + "\n" +
			   " Type: " + this.type + "\n";
	}

	public String toXML(){
		if (this.type == 'I'){
			return "\t\t<state id=\"" + this.id + "\"" +  " name=\"" + this.name + "\"" + ">\n\t\t\t<x>" + this.x + "</x>\n\t\t\t<y>" + this.y + "</y>\n\t\t\t<initial/>\n\t\t</state>";
			} else if (this.type == 'F'){
				return "\t\t<state id=\"" + this.id + "\"" +  " name=\"" + this.name + "\"" + ">\n\t\t\t<x>" + this.x + "</x>\n\t\t\t<y>" + this.y + "</y>\n\t\t\t<final/>\n\t\t</state>";
			}else{
				return "\t\t<state id=\"" + this.id + "\"" +  " name=\"" + this.name + "\"" + ">\n\t\t\t<x>" + this.x + "</x>\n\t\t\t<y>" + this.y + "</y>\n\t\t</state>";
			}
		}
}

class Transicao{
	Estado from; /* Current State*/
	Estado to; /* Destination State */
	char read; /* Input */

	/**
	  * Standard Constructor
	  */
	public Transicao(){
		this.from = null;
		this.to = null;
		this.read = '9';
	}
	/**
	  * Alternative Constructor
	  */
	public Transicao(Estado from, Estado to, char read){
		this.from = from;
		this.to = to;
		this.read = read;
	}
	/* Getters and Setters */
	public Estado getFrom(){return this.from;}
	public Estado getTo(){return this.to;}
	public char getRead(){return this.read;}

	public void setFrom(Estado from){this.from = from;}
	public void setTo(Estado to){this.to = to;}
	public void setRead(char read){this.read = read;}

	public String toString(){
		return "Transition from " + this.from.getId() + " to " + this.to.getId() +  " when input = " + this.read + "\n";
	}

	public String toXML(){
		return 	"\t\t<transition>\n\t\t\t<from>" + this.from.getId() + "</from>\n\t\t\t<to>" + this.to.getId() + "</to>\n\t\t\t<read>" + this.read + "</read>\n\t\t</transition>";
	}
}

class AF{
	List<Estado> states; /*List of States*/
	List<Transicao> transitions; /*List of transitions*/
	String type; /* AF = Standard, AFN = Nondeterministic Finite Automata, AFD = Deterministic Finite Automata */

	public AF(){
		this.states = new ArrayList<>();
		this.transitions = new ArrayList<>();
		type = "AF";
	}

	public AF(List<Estado> states, List<Transicao> transitions, String type){
		this.states = states;
		this.transitions = transitions;
		this.type = type;
	}

	public Estado getState(int id){
		for(Estado e : states)
			if(e.getId() == id)
				return e;
		return null;
	}


}
class Tradutor{

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
	 * @return Estado e - State completed parsed
	 */
		public static Estado parseState(String line){

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

		Estado e = new Estado(name, id, x, y, type);

		return e;
	}

	/**
	 * Function to parse a String line into a Transition
	 * @param String line - input string from XML
	 * @return Transicao t - Transition completed parsed
	 */

	public static Transicao parseTransition(String line, AF af){

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
		Estado from = af.getState(Integer.parseInt(array[1]));
		Estado to = af.getState(Integer.parseInt(array[2]));
		char read = array[3].charAt(0);

		Transicao t = new Transicao(from, to,read);
		System.out.println(t);

		return t;
	}




	public static void main (String[]args){
		/*Input Variables*/
		BufferedReader br = null;
		FileReader fr = null;
		String line;
		AF af = new AF();
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
					af.states.add(parseState(s));
					s = "";
				}

				/* parsing transitions */
				if(line.contains("<transition>")){
					
					String s = line;
					
					while ((line = br.readLine()) != null && (!line.contains("</transition>")))
						s = s+line;

					s = s.replaceAll("\t+", "").trim();
					af.transitions.add(parseTransition(s, af));
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
		


		}
}