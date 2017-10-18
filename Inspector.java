import java.lang.reflect.*;
import java.util.*;



public class Inspector{
	private static ArrayList<Class> visitedClasses;
	private static Inspector singleton = null;
	
	public void Inspector(){
		visitedClasses = null;
		if (singleton == null){
			singleton = this;
		}
	}


	public void inspect(Object obj, boolean recursive){
		//create list of visited objects if it has not been created
		//check if this class is in list (if recursive is true), return if so
		//add obj class to list of visited objects
		
		//Print name of declaring class
		//print name of superclass
		//print interfaces for this class
		//print method info

	}
	
	//Helper method, find and prints all info about object methods
	private void inspectMethods(Object obj){
		//Get an array of all methods in obj
		//call method on each individual method
	}

	private void inspectSingleMethod(Method m){
		//print method name
		//print return type
		//print parameter type
		//print exceptions
		//print modifiers
	}
	private void inspectConstructors(Object obj){
		//Get array of all constructors from obj
		//call helper method on each
	}

	private void inspectSingleConstructor(Constructor cons){
		//print constructor name
		//print parameter types
		//print modifiers
	}

	private Object[] inspectFields(Object obj, boolean recursive){
		//get all fields for obj
		//check if each is an object: if it is, add it to array to return
		//if recursive is on and field is an obj, dont call helper: otherwise call helper
		//always call helper if primitive
		return null;
	}

	private void inspectSingleField(Field f){
		//Print field name
		//print type
		//print modifiers
		//print current value
	}


}
