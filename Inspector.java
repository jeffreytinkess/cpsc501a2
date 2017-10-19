import java.lang.reflect.*;
import java.util.*;



public class Inspector{
	private static ArrayList<String> visitedClasses;
	private static Inspector singleton = null;
	
	public void Inspector(){
		visitedClasses = null;
		if (singleton == null){
			singleton = this;
		}
	}


	public void inspect(Object obj, boolean recursive){
		//oc = object class
		Class oc = obj.getClass();
		if (oc == null){
			//Should never happen, means null was passed in. Print as such and return
			System.out.println("obj is null");
			return;
		}
		//create list of visited objects if it has not been created
		if (visitedClasses == null){
			visitedClasses = new ArrayList<String>();
		}
		String objName = oc.getName();
		//check if this class is in list (if recursive is true), return if so
		if (recursive){
			//inefficient but will find duplicate visits
			for (String storedName:visitedClasses){
				if (objName.equals(storedName)){
					return;
				}
			}		
		}
		//add obj class to list of visited objects
		visitedClasses.add(objName);
		
		//Print name of declaring class
		System.out.println("Objects name is: " + objName);
		//print name of superclass
		System.out.println("Objects superclass name is: " + oc.getSuperclass().getName());
		//print interfaces for this class
		Class<?>[] interfaces = oc.getInterfaces();
		System.out.println("******* Interface list *******");
		for (int i = 0; i < interfaces.length; i++){
			//For each interface, print its name
			System.out.println("Interface #" + i + ": " + interfaces[i].getName());
		}
		//Print all method information
		inspectMethods(oc);

	}
	
	//Helper method, find and prints all info about object methods
	private void inspectMethods(Class c){
		//Get an array of all methods in obj
		ArrayList<Method> methods = new ArrayList<Method>();
		//Get all private methods
		Method[] declaredMethods = c.getDeclaredMethods();
		//Get all other methods including inherited
		Method[] otherMethods = c.getMethods();
		//add all public methods first
		for (int i = 0; i < otherMethods.length; i++){
			methods.add(otherMethods[i]);
		}
		//then add private methods only
		for (int i = 0; i < declaredMethods.length; i++){
			int testForPublic = declaredMethods[i].getModifiers();
			if (!Modifier.isPublic(testForPublic)){
				//Method is not public so would not have been added yet, add it now
				methods.add(declaredMethods[i]);
			}
		}
		//TESTING
		//System.out.println("Number of methods present is " + methods.size());
		//System.out.println("number of declared methods " + declaredMethods.length);
		//System.out.println("number of inherited methods " + otherMethods.length);
		int numMethod = 1;
		for (Method m:methods){
			System.out.println("******* Method #" + numMethod + " *******");
			inspectSingleMethod(m);
			numMethod++;
		}
	}

	private void inspectSingleMethod(Method m){
		//print method name
		System.out.println("Method name: " + m.getName());
		//print return type
		System.out.println("Return type: " + m.getReturnType().getSimpleName());
		//print parameter type
		System.out.print("Parameter types: ");
		Class[] parameters = m.getParameterTypes();
		for (int i = 0; i < parameters.length; i++){
			System.out.print(parameters[i].getSimpleName() + ", ");
		}
		System.out.println("");
		//print exceptions
		Class[] exceptions = m.getExceptionTypes();
		System.out.print("Exception types: ");
		for (int i = 0; i < exceptions.length; i++){
			System.out.print(exceptions[i].getSimpleName() + ", ");
		}
		System.out.println("");
		//print modifiers
		String toPrint = Modifier.toString(m.getModifiers());
		System.out.println("Modifiers: " + toPrint);
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
