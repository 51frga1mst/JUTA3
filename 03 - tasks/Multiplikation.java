
public class Multiplikation {

	public static int mult(int a, int b){
		if(b < 0)
			throw new RuntimeException("b muss grРЎвЂ Р Р‡er 0 sein");
		
		if(b == 0)
			return 0;
		else
			return a + mult(a, b - 1);
	}

	public static void main(String[] args) {
		System.out.println(mult(Input.readInt("a: "), Input.readInt("b: ")));
	}

}
