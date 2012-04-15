import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class FileWrite {
	
	protected boolean closed = false;
	protected BufferedWriter out;
	protected String fileName;
	
	public FileWrite(String filePath) throws IOException {
		this(filePath, false);
	}
	
	public FileWrite(String filePath, boolean append) throws IOException {
		 FileWriter fstream = new FileWriter(filePath, append);
		 out = new BufferedWriter(fstream);
		 fileName = filePath;
	}
	
	public void write(String s) throws IOException {
		out.write(s);
	}
	
	public void writeLine(String s) throws IOException {
		out.write(s + "\r\n");
	}
	
	public void close() throws IOException {
		closed = true;
		out.close();
	}
	protected void finalize() {
		if (! closed ) {
			try {
				this.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("File "+fileName+" couldn't be saved.");
				e.printStackTrace();
			}
		}
	}
}
