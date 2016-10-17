
public class WhileAusgabe {

	public static void main(String[] args) {
		
		int n = Input.readInt("Eingabe: ");
		int i = 1;
		char b;
		if(n > 0)
			b = '*';
		else{
			b = '#';
			n = -n;
		}
		while(i <= n){
			System.out.println(b);
			i++;
		}

	}

}
