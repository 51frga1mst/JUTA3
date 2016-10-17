
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/** Klassenmethoden zur Umleitung der Ausgabe.
 * @author Marcus Deininger
 * @version 2.1 / 4.12.2015
 */

public abstract class Output {
	
	private interface OutputHolder{
		
		public void enter(String string);
		
		public boolean isDone();
		
		public String[] getStrings();
	}
	
	private static class StaticListHolder implements OutputHolder{
		
		private PrintStream out;
		private String[] strings;
		private int pos;
		private boolean done = false;

		public StaticListHolder(PrintStream out, String[] strings) {
			this.out = out;
			this.strings = strings;
			this.pos = 0;
		}

		@Override
		public void enter(String string) {
			strings[pos] = string;
			pos++;
			if(pos == strings.length){
				done = true;
				System.setOut(this.out);
			}
		}

		@Override
		public boolean isDone() {
			return done;
		}

		@Override
		public String[] getStrings() {
			return strings;
		}
		
	}
	
	private static class DynamicListHolder implements OutputHolder{
		
		private PrintStream out;
		private List<String> strings;
		private boolean done = false;

		public DynamicListHolder(PrintStream out) {
			this.out = out;
			this.strings = new ArrayList<>();
		}

		@Override
		public void enter(String string) {
			strings.add(string);
		}

		@Override
		public boolean isDone() {
			return done;
		}
		
		@Override
		public String[] getStrings() {
			if(!done){
				System.setOut(this.out);
				this.done = true;
			}
			return strings.toArray(new String[strings.size()]);
		}
		
	}
	
	private static class StringOutputStream extends ByteArrayOutputStream{
		
		private static final String charsetName = Charset.defaultCharset().name();

		private PrintStream out, strOut;
		private OutputHolder holder = null;
		private boolean echo;
		
		private StringBuilder sb;
		
		
		public StringOutputStream(boolean echo, String[] strings) {
			this.holder = new StaticListHolder(System.out, strings);
			this.echo = echo;			
			this.initStreams();
		}

		public StringOutputStream(boolean echo) {
			this.holder = new DynamicListHolder(System.out);
			this.echo = echo;			
			this.initStreams();
		}

		private void initStreams() {			
			this.sb = new StringBuilder();
			
			this.out = System.out;
			this.strOut = new PrintStream(this, true);
			System.setOut(strOut);
		}
		
		private String[] getStrings(){
			return holder.getStrings();
		}

		@Override
		public synchronized void write(byte[] b, int off, int len){
			try {
				String str = new String(b, off, len, charsetName);
				
				for(int i = 0; i < str.length(); i++){
					char c = str.charAt(i);
					if(echo & !holder.isDone()){						
						System.setOut(this.out);
						System.out.print(c);
						System.setOut(this.strOut);
					}
					if(holder.isDone())
						System.out.print(c);
					else
						switch(c){
							case '\r':	break; // do nothing
							case '\n':
								holder.enter(sb.toString());
								sb = new StringBuilder();
								break;
							default:
								sb.append(c);
						}					
				}				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.exit(0);
			} 
			super.write(b, off, len);
		}
		
	}
	

	private static StringOutputStream stream = null;
			
	/**
	 * Leitet die Ausgabe fР РЋР Р‰r das Р РЋР Р‰bergebene Feld um. Nachdem 
	 * das Feld gefР РЋР Р‰llt wurde, wird die Standardausgabe
	 * wieder hergestellt.
	 * @param strings Feld, das von der Eingabe gefР РЋР Р‰llt werden soll.
	 */
	public static void redirect(String[] strings){
		redirect(false, strings);
	}
	
	/**
	 * Leitet die Ausgabe fР РЋР Р‰r das Р РЋР Р‰bergebene Feld um. Nachdem 
	 * das Feld gefР РЋР Р‰llt wurde, wird die Standardausgabe
	 * wieder hergestellt.
	 * @param echo true, wenn die Ausgabe am Bildschirm wiederholt werden soll.
	 * @param strings Feld, das von der Eingabe gefР РЋР Р‰llt werden soll.
	 */
	public static void redirect(boolean echo, String[] strings){
		if(strings.length == 0)
			return;
		
		stream = new StringOutputStream(echo, strings);
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Leitet die Ausgabe um. Nachdem das Ergebnis mit getOutput 
	 * abgefragt wurde, wird die Standardausgabe
	 * wieder hergestellt.
	 */
	public static void redirect(){
		stream = new StringOutputStream(false);
	}
	
	/**
	 * Leitet die Ausgabe um. Nachdem das Ergebnis mit getOutput 
	 * abgefragt wurde, wird die Standardausgabe
	 * wieder hergestellt.
	 * @param echo true, wenn die Ausgabe am Bildschirm wiederholt werden soll.
	 */
	public static void redirect(boolean echo){
		stream = new StringOutputStream(echo);
	}
	
	/**
	 * Liefert die ausgegebenen Strings zurР РЋР Р‰ck. Nach der RР РЋР Р‰ckgabe wird
	 * die Standardausgabe wieder hergestellt.
	 * @return
	 */
	public static String[] getOutput(){
		return stream.getStrings();
	}
	
}
