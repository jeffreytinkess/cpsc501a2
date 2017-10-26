import org.junit.Before;
import org.junit.Test;
public class TestA2{

Inspector inspector;
	@Before
	public void setup(){

		inspector = new Inspector();

	}

	@Test
	public void testNullInput(){
		Object test = null;
		inspector.inspect(test, false);
	}

	@Test
	public void testArrayInput(){
		int[] test = new int[10];
		for (int i = 0; i < 10; i++){
			test[i] = i;
		}
		inspector.inspect(test, false);

	}

	@Test
	public void testObjectArrayInput(){
		Object[] test = new Object[10];
		for (int i = 0; i < 10; i++){
			test[i] = new Object();
		}
		inspector.inspect(test, false);
	}
	@Test
	public void testObjectNullFields(){
		TestObject test = new TestObject();
		inspector.inspect(test, false);
	}
	@Test
	public void testObjectWithFields(){
		int[] array = new int[5];
		for (int i = 0; i < 5; i++){
			array[i] = i;
		}
		TestObject test = new TestObject(5, true, new Object(), array);
		inspector.inspect(test, false);
	}
	@Test
	public void testObjectRecursive(){
		TestA2 test = new TestA2();
		inspector.inspect(test, true);
	}
	private class TestObject{
		Integer testInt;
		Boolean testBool;
		Object testing;
		int[] testIntArray;

		public TestObject(){
			testInt = null;
			testBool = null;
			testing = null;
			testIntArray = null;
		}

		public TestObject(int i, boolean b, Object o, int[] array){
			testInt = i;
			testBool = b;
			testing = o;
			testIntArray = array;
			}
		};

}
