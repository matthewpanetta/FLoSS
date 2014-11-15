package fileManagement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class GetFileHandler {
	public static void main(String[] args) throws IOException {
         URLConnection http = new URL("http://65.185.85.1/PanettaERD.docx")
                   .openConnection();

         // Start copying!
         InputStream in = http.getInputStream();
         try {
              OutputStream out = new FileOutputStream("C:\\Users\\mp755\\Desktop\\test.docx");
              try {
                   byte[] buf = new byte[512];
                   int read;

                   while ((read = in.read(buf)) > 0)
                        out.write(buf, 0, read);
              } finally {
                   out.close();
              }
         } finally {
              in.close();
         }
    }
}
