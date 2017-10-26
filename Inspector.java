import java.lang.reflect.*;
import java.util.*;



public class Inspector{
	private ArrayList<String> visitedClasses;
	//List of objects that still must be recursively checked
	private ArrayList<Object> toBeVisited;


	public void Inspector(){

		toBeVisited = new ArrayList<Object>();

	}


	public void inspect(Object obj, boolean recursive){
		if (obj == null){
			System.err.println("null object passed into inspect");
			return;
		}
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
		if (toBeVisited == null){
			toBeVisited = new ArrayList<Object>();
		}
		//Get object identifier
		String objName = obj.toString();
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
		System.out.println();
		System.out.println();
		System.out.println("************** Object Inspection Start **************");
		//Print name of declaring class
		System.out.println("Objects name is: " + oc.getName());
		//print name of superclass
		try{
		System.out.println("Objects superclass name is: " + oc.getSuperclass().getName());
		} catch (NullPointerException npe) {System.out.println("Object has no superclass (instance of Object)");};
		System.out.println();
		System.out.println();

		//Get a list of intheritance chain
		Set<String> inheritanceNames = new HashSet<String>();
		getInheritenceChain(oc, inheritanceNames);
		//Convert names into list of classes
		Class[] inheritanceClasses = new Class[inheritanceNames.size()];
		int currName = 0;
		for (String name: inheritanceNames){
			try{
				Class c = Class.forName(name);
				inheritanceClasses[currName] = c;
				currName++;
			} catch (ClassNotFoundException cnfe) {System.out.println("Cant find class for given name " + name);};
		}


		//print interfaces for this class
		Class<?>[] interfaces = oc.getInterfaces();
		System.out.println("************** Interfaces **************");
		for (int i = 0; i < interfaces.length; i++){
			//For each interface, print its name
			System.out.println("Interface #" + i + ": " + interfaces[i].getName());
		}

		//Print all method information
		System.out.println();
		System.out.println();
		inspectMethods(oc);
		System.out.printf("%n ***** Inherited Methods ***** %n");
		//Call inherited method helper
		inspectInheritedMethods(inheritanceClasses);
		//Print all constructor info
		System.out.println();
		System.out.println();
		inspectConstructors(oc);
		System.out.printf("%n ***** Inherited Constructors ***** %n");
		//Call inherited constructor helper
		inspectInheritedConstructors(inheritanceClasses);
		//print all field info
		System.out.println();
		System.out.println();
		inspectFields(obj, recursive);
		System.out.printf("%n ***** Inherited Fields ***** %n");
		//Call inherited field helper
		inspectInheritedFields(inheritanceClasses, obj, recursive);
		System.out.println("************** Object Inspection End **************");
		//If recursive is on, call inspect method on each object returned from field inspection
		if (recursive){
			for (Object o:toBeVisited){
				inspect(o, recursive);
			}
		}

	}

	//Helper method, find and prints all info about object methods
	private void inspectMethods(Class c){
		//Get an array of all methods in obj
		Method[] methods = c.getDeclaredMethods();
		if (methods.length == 0){
			System.out.println("This class has no declared methods");
			return;
		}
		System.out.println("************** Methods **************");
		int numMethod = 1;
		for (int i = 0; i < methods.length; i++){
			System.out.println("******* Method #" + numMethod + " *******");
			inspectSingleMethod(methods[i]);
			numMethod++;
		}
	}

	private void inspectSingleMethod(Method m){
		//print method name
		System.out.printf("%nMethod name: " + m.getName() + "%n");
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

	private void inspectInheritedMethods(Class[] classes){
		ArrayList<Method> allMethods = new ArrayList<Method>();
		for (int i = 0; i < classes.length; i++){
			Method[] methods = classes[i].getDeclaredMethods();
			for (int j = 0; j < methods.length; j++){
				Method m = methods[j];
				int modifier = m.getModifiers();
				if (!Modifier.isPrivate(modifier) && !Modifier.isStatic(modifier)){
					allMethods.add(m);
				}
			}
		}
		for (Method m:allMethods){
			inspectSingleMethod(m);
		}
	}

	private void inspectInheritedConstructors(Class[] classes){
		ArrayList<Constructor> allConstructors = new ArrayList<Constructor>();
		for (int i = 0; i < classes.length; i++){
			Constructor[] cons = classes[i].getDeclaredConstructors();
			for (int j = 0; j < cons.length; j++){
				Constructor c = cons[j];
				int modifier = c.getModifiers();
				if (!Modifier.isPrivate(modifier) && !Modifier.isStatic(modifier)){
					allConstructors.add(c);
				}
			}
		}
		for (Constructor c:allConstructors){

			inspectSingleConstructor(c);
		}

	}

	private void inspectInheritedFields(Class[] classes, Object obj, boolean recursive){
	ArrayList<Field> allFields = new ArrayList<Field>();
	for (int i = 0; i < classes.length; i++){
		Field[] fields = classes[i].getDeclaredFields();
		for (int j = 0; j < fields.length; j++){
			Field f = fields[j];
			int modifier = f.getModifiers();
			if (!Modifier.isPrivate(modifier) && !Modifier.isStatic(modifier)){
				allFields.add(f);
			}
		}
	}
	for (Field f : allFields){
		inspectSingleField(f, recursive, obj);
	}

	}


	private void inspectConstructors(Class c){
		//Get array of all constructors from obj
		Constructor[] cons = c.getDeclaredConstructors();
		//call helper method on each
		System.out.println("************** Constructors **************");
		int numCons = 1;
		for (int i = 0; i < cons.length; i++){
			System.out.println("******* Constructor #" + numCons + " *******");
			inspectSingleConstructor(cons[i]);
			numCons++;
		}
	}

	private void inspectSingleConstructor(Constructor cons){
		//print constructor name
		System.out.println("Constructor name: " + cons.getName());
		//print parameter types
		Class[] params = cons.getParameterTypes();
		System.out.print("Parameters: ");
		for (int i = 0; i < params.length; i++){
			System.out.print(params[i].getSimpleName() + ", ");
		}
		System.out.println("");
		//print modifiers
		String toPrint = Modifier.toString(cons.getModifiers());
		System.out.println("Modifiers: " + toPrint);
	}

	private void inspectFields(Object obj, boolean recursive){
		System.out.println("************** Fields **************");
		ArrayList<Object> objList = new ArrayList<Object>();
		Class c = obj.getClass();
		//get all fields for obj
		Field[] fields = c.getFields();
		int fieldNum = 1;
		for (int i = 0; i < fields.length; i++){
			System.out.println("******* Field #" + fieldNum + " *******");
			boolean isObject = inspectSingleField(fields[i], recursive, obj);
			if (isObject && recursive){
				try{
					toBeVisited.add(fields[i].get(obj));
					//System.out.println("DEBUG: added an object for recirsive testing");
				} catch (IllegalAccessException iae){}
				  catch (IllegalArgumentException iae){}
				  catch (NullPointerException npe) {}
				  catch (ExceptionInInitializerError eiie) {}
			}
			fieldNum++;
		}


	}

	private boolean inspectSingleField(Field f, boolean recursive, Object object){
		//Print field name
		System.out.println("Field name: " + f.getName());
		//print type
		System.out.println("Field type: " + f.getType().getSimpleName());
		//print modifiers
		String toPrint = Modifier.toString(f.getModifiers());
		System.out.println("Modifiers: " + toPrint);
		//print current value
		Class c = f.getType();
		//handle specific cases
		if (c.isPrimitive()){
			try{
				Object fieldValue = f.get(object);
				System.out.println("***Primitive Field*** Value: " + fieldValue.toString());
			} catch (Exception e) {}
		} else if (c.isArray()){
			System.out.println("***Array Field***");
			Class elementType = c.getComponentType();

			try{


			//If it is an object and recursion is on, add it to "to be visited" list
			if (!elementType.isPrimitive() && recursive){
				Object[] targetArray = (Object[]) f.get(object);
				for (int i = 0; i < targetArray.length; i++){
					//add all elements of array to list to be inspected individually
					toBeVisited.add(targetArray[i]);
				}
			} else {
				//Print all info of each element
				Object targetPrimArray = f.get(object);
				//Use static methods in Array class to find length and elements indirectly
				int primArrayLength = Array.getLength(targetPrimArray);
				for (int i = 0; i < primArrayLength; i++){
					Object element = Array.get(targetPrimArray, i);
					System.out.println("Element #" + i + " value: " + element.toString());
				}
			}
			} catch (Exception e){e.printStackTrace(); };
		} else { //If it reaches this point it is an object reference, check recursive and handle
			if (!recursive){
				try{
					Object fieldValue = f.get(object);
					System.out.println("***Object Reference Field*** Value: " + fieldValue.toString());
				} catch (Exception e) {}
			}else {
				return true;
			}
		}
		return false;
	}

	private void getInheritenceChain(Class c, Set<String> names){
		Class mySuper = c.getSuperclass();
		Class[] interfaces = c.getInterfaces();

		if (mySuper == null){
			return;
		} else {
			names.add(mySuper.getName());
			for (int i = 0; i < interfaces.length; i++){
				names.add(interfaces[i].getName());
				getInheritenceChain(interfaces[i], names);
			}
			getInheritenceChain(mySuper, names);
		}

	}


}
