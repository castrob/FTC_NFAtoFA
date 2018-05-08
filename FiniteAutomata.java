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

class State{
	String name;
	int id;
	double x; /* x Position ( Standard 0.0 ) */
	double y; /* y Position ( Standard 0.0 ) */
	char type; /* C = Commom, I = Initial, F = Final */
	/**
	* Standard Constructor
	*/
	public State(){
		this.name = "q";
		this.id = 99;
		this.x = 0.0;
		this.y = 0.0;
		this.type = 'c';
	}
	/**
	* Alternative Constructor
	*/
	public State(String name, int id, double x, double y, char type){
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

class Transition{
	State from; /* Current State*/
	State to; /* Destination State */
	char read; /* Input */

	/**
	  * Standard Constructor
	  */
	public Transition(){
		this.from = null;
		this.to = null;
		this.read = '9';
	}
	/**
	  * Alternative Constructor
	  */
	public Transition(State from, State to, char read){
		this.from = from;
		this.to = to;
		this.read = read;
	}
	/* Getters and Setters */
	public State getFrom(){return this.from;}
	public State getTo(){return this.to;}
	public char getRead(){return this.read;}

	public void setFrom(State from){this.from = from;}
	public void setTo(State to){this.to = to;}
	public void setRead(char read){this.read = read;}

	public String toString(){
		return "Transition from " + this.from.getId() + " to " + this.to.getId() +  " when input = " + this.read + "\n";
	}

	public String toXML(){
		return 	"\t\t<transition>\n\t\t\t<from>" + this.from.getId() + "</from>\n\t\t\t<to>" + this.to.getId() + "</to>\n\t\t\t<read>" + this.read + "</read>\n\t\t</transition>";
	}
}

class FiniteAutomata{
	List<State> states; /*List of States*/
	List<Transition> transitions; /*List of transitions*/
	String alphabet;
	String type; /* FA = Standard, NFA = Nondeterministic Finite Automata, DFA = Deterministic Finite Automata */

	public FiniteAutomata(){
		this.states = new ArrayList<>();
		this.transitions = new ArrayList<>();
		this.type = "FA";
		this.alphabet = "";
	}

	public FiniteAutomata(List<State> states, List<Transition> transitions, String type, String alphabet){
		this.states = states;
		this.transitions = transitions;
		this.type = type;
		this.alphabet = alphabet;
	}


	public void generateAlphabet(){
		for(Transition t : transitions){
			if(!(this.alphabet.contains(t.read+"")))
				alphabet += t.read;
		}
	}


	public String getAlphabet(){
		if(this.alphabet.equals("")){
			this.generateAlphabet();
			// this.stateTable();
			return this.alphabet;
		}else{
			return this.alphabet;
		}
	}

	Transition getTransition(State s){
		for(Transition t : transitions){
			if(t.from.name.equals(s.name))
				return t;
		}
		return null;
	}


	Transition getTransition(State s, char c){
		for(Transition t :transitions){
			if(t.from.name.equals(s.name) && t.read == c)
				return t;
		}
		return null;
	}

	// public void stateTable(){
	// 	String[][] table = new String[this.states.size()][alphabet.length()];
	// 	for (int i = 0; i < this.states.size(); i++ ) {
	// 		State e = this.states.get(i);
	// 		for(int j = 0; j < alphabet.length(); j++){
	// 			Transition t = this.getTransition(e, alphabet.charAt(j));
	// 			table[i][j] = t.to;
	// 		}
	// 	}
	// }


	public State getState(int id){
		for(State e : states)
			if(e.getId() == id)
				return e;
		return null;
	}

	public List<State> getInitialStates(){
		List<State> initials = new ArrayList<State>();
		for(State e : states){
			if(e.getType() == 'I'){
				initials.add(e);
			}
		}
		return initials;
	}


	// public FiniteAutomata convertToFiniteAutomataD( ){
	// 	if(this.type.equals("FiniteAutomataN")){
	// 		List<State> new_i = this.getInitialStates();
	// 		List<Transition> new_t = new ArrayList<Transition>();
	// 		FiniteAutomata dfa = new FiniteAutomata(new_i, new_t, "DFA", this.getAlphabet());
	// 		for(State x : dfa.states){
	// 			for(char a : dfa.alphabet){
	// 				// gera States
	// 				State y;
	// 				if(dfa.states.contains(y) == false){
	// 					dfa.states.add(y);
	// 				}
	// 				Transition t = new Transition(x, y, a);
	// 				dfa.transitions.add(t);
	// 			}// for letra em alfabeto
	// 		}// for State X : E'
	// 	}else{
	// 		System.out.println("Already a DFA");
	// 	}

	// }


}