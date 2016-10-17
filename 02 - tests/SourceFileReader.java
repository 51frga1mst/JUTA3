import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class SourceFileReader {

	private static final String DEFAULT_LOCATION = "src";
	private static final String JAVA_EXTENSION = ".java";
	
	public String filename(String path, Class<?> cls){
		if(path == null)
			path = DEFAULT_LOCATION;
		String classname = cls.getName();
		String filename = path + File.separator + classname.replace('.', File.separatorChar) + JAVA_EXTENSION;
		return filename;
	}

	public boolean exists(String path, Class<?> cls){
		String filename = this.filename(path, cls);
		return this.exists(filename);
	}

	public boolean exists(String filename){
		if(filename == null)
			return false;
		File f = new File(filename);
		return f.exists();
	}

	public int[] contains(String filename, String ... patterns){
		int[] occurences = new int[patterns.length];
		
		if(filename == null || !exists(filename))
			return occurences;
		
		Reader reader = null;
		BufferedReader buffer = null;

		try {
			reader = new FileReader(filename);
			buffer = new BufferedReader(reader);

			String line = buffer.readLine(); // Text verarbeiten
			while(line != null){
				String[] segments = line.split("//");
				if(segments.length > 0){
					String code = segments[0];
					for (int i = 0; i < patterns.length; i++)
						if(code.contains(patterns[i]))
							occurences[i]++;
				}					
				line = buffer.readLine();
			}
		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			try {
				buffer.close(); // schlieР В Р вЂЎt auch "reader"
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
		return occurences;		
	}
}
