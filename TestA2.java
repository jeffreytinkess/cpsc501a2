import java.io.*;
public class TestA2 implements Serializable{

	public Inspector iTest;
	private static TestA2 singleton;
	public boolean testBool = false;
	public int testInt = 42;
	public double testFloat = 0.005;
	public int[] testIntArray =  {1,2,3,4,5};
	public Object[] testObjArray;


	public TestA2(){
		iTest = new Inspector();
		singleton = this;
		testObjArray = new Object[3];
		for (int i = 0; i < 3; i++){
			testObjArray[i] = new Object();
		}
	}

	public static void main(String[] args){
		//Create an inspector
		Inspector i = new Inspector();
		TestA2 test = new TestA2();
		//pass it an object
		Object s = new String("Hello world");
		i.inspect(test, true);

	}
}
