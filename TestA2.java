
public class TestA2{

	public TestA2(){

	}

	public static void main(String[] args){
		//Create an inspector
		Inspector i = new Inspector();
		TestA2 test = new TestA2();
		//pass it an object
		Object s = new String("Hello world");
		i.inspect(test, false);

	}
}
