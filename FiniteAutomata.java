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
import java.util.Queue;

import javax.lang.model.util.ElementScanner6;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

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
			return "\n\t\t<state id=\"" + this.id + "\"" +  " name=\"" + this.name + "\"" + ">\n\t\t\t<x>" + this.x + "</x>\n\t\t\t<y>" + this.y + "</y>\n\t\t\t<initial/>\n\t\t</state>";
			} else if (this.type == 'F'){
				return "\n\t\t<state id=\"" + this.id + "\"" +  " name=\"" + this.name + "\"" + ">\n\t\t\t<x>" + this.x + "</x>\n\t\t\t<y>" + this.y + "</y>\n\t\t\t<final/>\n\t\t</state>";
			}else{
				return "\n\t\t<state id=\"" + this.id + "\"" +  " name=\"" + this.name + "\"" + ">\n\t\t\t<x>" + this.x + "</x>\n\t\t\t<y>" + this.y + "</y>\n\t\t</state>";
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
		return 	"\n\t\t<transition>\n\t\t\t<from>" + this.from.getId() + "</from>\n\t\t\t<to>" + this.to.getId() + "</to>\n\t\t\t<read>" + this.read + "</read>\n\t\t</transition>";
	}
}


class FiniteAutomata{
	
	List<State> states; /*List of States*/
	List<Transition> transitions; /*List of transitions*/
	String alphabet;
	String[][] transitionTable;
	String type; /* FA = Standard, NFA = Nondeterministic Finite Automata, DFA = Deterministic Finite Automata */
	List<String> statesNames;
	int countStates;

	/* Service functions */

	/**
	 * Standard Constructor
	 */
	public FiniteAutomata(){
		this.states = new ArrayList<>();
		this.transitions = new ArrayList<>();
		this.type = "NFA";
		this.alphabet = "";
		this.countStates = 0;
	}

	/**
	 * Alternative Constructor
	 * @param List<State> states - all automata states
	 * @param List<Transition> transitions - all automata transitions
	 * @param String type  - automata type
	 * @param String alphabet - automata's alphabet
	 */
	public FiniteAutomata(List<State> states, List<Transition> transitions, String type, String alphabet){
		this.states = states;
		this.transitions = transitions;
		this.type = type;
		this.alphabet = alphabet;
		this.countStates = 0;
	}

	/**
	 * Function to return the last id inside the list
	 */
	public int getLastId(){
		return this.states.get(states.size()).id;
	}

	/**
	 * Function to generate the alphabet based on transitions
	 */
	public void generateAlphabet(){
		for(Transition t : transitions){
			if(!(this.alphabet.contains(t.read+"")))
				alphabet += t.read;
		}
	}


	/**
	 * State getter
	 * @param String id
	 */
	public State getState(String name){
		for(State e : states)
			if(e.getName().equals(name))
				return e;
		return null;
	}

	/**
	 * Getter to State
	 * @param char type 
	 */
	public State getState(char type){
		for(State e : states)
			if(e.getType() == type)
				return e;
		return null;
	}

	/**
	 * State getter
	 * @param int id
	 */
	public State getState(int id){
		for(State e : states)
			if(e.getId() == id)
				return e;
		return null;
	}

	/**
	 * Getter of Initial States Only
	 */
	public List<State> getInitialStates(){
		List<State> initials = new ArrayList<State>();
		for(State e : states){
			if(e.getType() == 'I'){
				initials.add(e);
			}
		}
		return initials;
	}

	/**
	 * Function to return all final states
	 */
	public List<State> getFinalStates(){
		List<State> finals = new ArrayList<State>();
		for(State e : states){
			if(e.getType() == 'F'){
				finals.add(e);
			}
		}
		return finals;
	}

	/**
	 * Alphabet Getter
	 */
	public String getAlphabet(){
		if(this.alphabet.equals("")){
			this.generateAlphabet();
			return this.alphabet;
		}else{
			return this.alphabet;
		}
	}

    /**
	 * Function to return the index of a letter in the alphabet
	 * @param char c - index
	 */
	public int getCharOnAlphabet(char c){
		for(int i = 0; i < alphabet.length(); i++)
			if(alphabet.charAt(i) == c)
				return i;
		return -1;
	}

	/**
	 * Transition Getter
	 * @param State s
	 */
	Transition getTransition(State s){
		for(Transition t : transitions){
			if(t.from.name.equals(s.name))
				return t;
		}
		return null;
	}

	/**
	 * Transition Getter
	 * @param State s
	 * @param char c
	 */
	Transition getTransition(State s, char c){
		for(Transition t :transitions){
			if(t.from.name.equals(s.name) && t.read == c)
				return t;
		}
		return null;
	}

	State getNextState(State s, char c){
		for (Transition t : transitions){
			if(t.from.name.equals(s.name) && t.read == c){
				return t.to;
			}
		}
		return null;
	}

	/**
	 * Function that returns the name of a state by the transition and previous state
	 * @param State s - State that points to returned state
	 * @param char c - what needs to be read
	 */
	String getNameOfTransitionState(State s, char c){
		String name = "";
		for (Transition t : transitions){
			if(t.from.name.equals(s.name) && t.read == c){
				name += "," + t.to.name;
			}
		}
		// correcting the string name
		if(!name.equals("") && name.charAt(0)==',')
			name = name.substring(1, (name.length()));
		return name;
	}

	/**
	 * Method to generate the transition table 
	 */
	void createTransitionTable(){
		//creating table of states/simbols
		this.transitionTable = new String[this.states.size()][this.alphabet.length()];
		for(int i = 0; i < this.states.size(); i++){
			for(int j = 0; j < this.alphabet.length(); j++){
				this.transitionTable[i][j] = getNameOfTransitionState(states.get(i), alphabet.charAt(j));
			}
		}
	}
	
	/**
	 * TransitionTable Getter
	 */
	String[][] getTransitionTable(){
		if(this.transitionTable == null){
			this.createTransitionTable();
			return this.transitionTable;
		}else{
			return this.transitionTable;
		}
	}

	/**
	 * Setter for Type
	 */
	 public void setType(String type){
		 this.type = type;
	 }

	public Boolean isDeterministic(){
		int countOfTransitions = 0;
		for(int i = 0; i < alphabet.length(); i++){
			char c = alphabet.charAt(i);
			for(State s : states){
				for(Transition t : transitions){
					if(t.from.name.equals(s.name) && t.read == c && countOfTransitions < 2){
						countOfTransitions++;
						if(countOfTransitions >= 2){
							this.type = "NFA";
							return false;
						}
					}
				}
				countOfTransitions = 0;
			}
		}
		this.type = "DFA";
		return true;
	}


	public String fixName(String name){
		if(!name.equals("")){
			if(name.charAt(0) == ','){
				name = name.substring(1, name.length());
			}
			if(name.charAt(name.length()-1) == ','){
				name = name.substring(0,name.length()-1);
			}
		}
		return name;
	}

	public boolean hasState(String name){
		for(int i = 0; i < states.size(); i++){
			State tmp = states.get(i);
			if(tmp.name.equals(name)){
				return true;
			}
		}
		return false;
	}



										/* end of service area */


	/**
	 * Function to return the name of the next State Name
	 * @param 
	 */
	public String nextState(State s, char c){
		//state name can be : "0", "0,1", "0,1,2"...
		String[] strSplit = s.name.split(",");
		String name = "";
		for(int i = 0; i < strSplit.length; i++){
			int a = Integer.parseInt(strSplit[i]);
			int b = this.getCharOnAlphabet(c);
			name += "," + this.transitionTable[a][b];
		}
		return this.fixName(name);
	}

	/**
	 * Function to convert a Non deterministic finite automata into a Deterministic one
	 */

	public FiniteAutomata convert( ){
		//Tests if its aready a Deterministic Finite Automata
		if(this.type.equals("NFA")){
			
			List<State> new_f = this.getFinalStates(); // List of final States of the  NFA
			List<State> new_i = this.getInitialStates(); // List of Initial States of the NFA
			List<Transition> new_t = new ArrayList<Transition>(); // List of new Transitions
			FiniteAutomata dfa = new FiniteAutomata(new_i, new_t, "DFA", this.getAlphabet());
			int size = dfa.states.size(); // control variable for looping trought convertion
			// for each state 
			for(int k = 0; k < size; k++ ){
				State x = dfa.states.get(k);
				//for each char on alphabet
					for(int i = 0; i < dfa.alphabet.length(); i++){
					char a = dfa.alphabet.charAt(i);
					// passing actual state to recieve the new state transiction
					String name = nextState(x, a);
					State y;
					if(dfa.hasState(name) == false){
						// avoiding duplicated states for not having a error state
						if(name.charAt(name.length()-1) != ','){
							char type = 'c';
							//Testing if a final state
							for(State e : new_f){
								String[] tmp = name.split(",");
								for(int p = 0; p < tmp.length; p++){
									if(e.name.contains(tmp[p]))
										type = 'F';
								}
							}
	
							y = new State(name, dfa.states.size(), 0.0, 0.0, type);
							dfa.states.add(y);
							Transition t = new Transition(x, y, a);
							dfa.transitions.add(t);
						}
					}else{
						y = dfa.getState(name);
						Transition t = new Transition(x, y, a);
						dfa.transitions.add(t);
					}
				}// for letra em alfabeto
				size = dfa.states.size();
			}// for State X : E'
			return dfa;
		}else{
			System.out.println("Already a DFA");
			return this;
		}
	}

	/**
	 * Function to test if a given word belongs to this automata
	 * @param String word - input word
	 * @return Boolean 
	 */
	public Boolean simulate(String word){
		Boolean belongs;
		Queue<Character> input = new LinkedList<Character>();
		State s = this.states.get(0);
		
		if(s.getType()!='I'){
			s = this.getState('I');
		}

		char[] carr = word.toCharArray();
		for(char c : carr)
			input.add(c);

		while(input.isEmpty() == false && s != null){
			s = this.getNextState(s, input.poll());
		}

		if(input.isEmpty() && s != null){
			if(s.getType() == 'F'){
			System.out.println("Palavra pertence!");
			belongs = true;				
			}else{
			System.out.println("Palavra não pertence!");
			belongs = false;				
			}
		}else if(input.isEmpty() == false && s == null){
			System.out.println("Palavra não pertence!");
			belongs = false;
		}

		return false;
	}


										/* Print Area */

	/**
	 * Method to print the Automata Settings
	 */
	public void printAutomata(){
		System.out.println("Alphabet: " + this.alphabet );
		System.out.println("States: ");
		for(State s : states){
			System.out.println(s);
		}
		System.out.println("Transitions: ");
		for(Transition t : transitions){
			System.out.println(t);
		}
	}

	/**
	 * Method to print the Transition Table
	 */
	public void printTransTable(){
		if(transitionTable == null){
			this.getTransitionTable();
		}
		System.out.println("Tabela de transicao: ");

		for( int i = 0; i < states.size(); i++){
			for(int j = 0; j < alphabet.length(); j++){
				System.out.print(transitionTable[i][j]+ " ");
			}
			System.out.println();
		}

	}


	public String toXML(){
		String answer = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Created with JFLAP 6.4.--><structure>\n\t<type>fa</type>\n\t<automaton>\n\t\t";
		for(State s : states){
			answer += s.toXML();
		}
		for(Transition t : transitions){
			answer += t.toXML();
		}
		answer += "\n\t</automaton>\n</structure>";
		return answer;
	}

}