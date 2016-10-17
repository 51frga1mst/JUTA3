
public class Division {

	public static int div(int a, int b){
		if(a < 0 || b <= 0)
			throw new RuntimeException("a muss grРЎвЂ Р Р‡er gleich und b  grРЎвЂ Р Р‡er 0 sein");
		
		if(a < b)
			return 0;
		else
			return 1 + div(a - b, b);
	}

	public static void main(String[] args) {
		System.out.println(div(Input.readInt("a: "), Input.readInt("b: ")));
	}

}
