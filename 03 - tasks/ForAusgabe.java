
public class ForAusgabe {

	public static void main(String[] args) {
		
		int n = Input.readInt("Eingabe: ");
		char b;
		if(n > 0)
			b = '#';
		else{
			b = '*';
			n = -n;
		}
		for(int i = 1; i<= n; i++)
			System.out.println(b);
		
	}
}
