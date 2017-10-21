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
		System.out.println();
		System.out.println();
		System.out.println("************** Object Inspection Start **************");
		//Print name of declaring class
		System.out.println("Objects name is: " + objName);
		//print name of superclass
		System.out.println("Objects superclass name is: " + oc.getSuperclass().getName());
		System.out.println();
		System.out.println();
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
		//Print all constructor info
		System.out.println();
		System.out.println();
		inspectConstructors(oc);
		//print all field info
		System.out.println();
		System.out.println();
		Object[] fieldObjRef = inspectFields(obj, recursive);
		System.out.println("************** Object Inspection End **************");
		//If recursive is on, call inspect method on each object returned from field inspection
		if (recursive){
			for (int i = 0; i < fieldObjRef.length; i++){
				inspect(fieldObjRef, recursive);
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

	private Object[] inspectFields(Object obj, boolean recursive){
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
					objList.add(fields[i].get(obj));
				} catch (IllegalAccessException iae){}
				  catch (IllegalArgumentException iae){}
				  catch (NullPointerException npe) {}
				  catch (ExceptionInInitializerError eiie) {}
			}
			fieldNum++;
		}
		
		return objList.toArray();
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


}
