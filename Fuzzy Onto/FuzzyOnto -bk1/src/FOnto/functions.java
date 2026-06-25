/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FOnto;

import java.io.*;
import java.util.*;
import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author Mr. Noname
 */
public class functions {
    public  String getRootURL ()
    {
        URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
        String str = location.toString();
   //     File file = new File(location.getPath());
        System.out.println("URL=============== "+ location.toString());
        return str;

    }
    public static void OWL2toFuzzyDLandParse(String AppRoot, String PathIn, tmpFilePath PathfDL, FuzzyOwl2toFuzzyDL.InputDataBlock[] argIn)
            //Output will be a translated fDLfile.txt
    {
        PathfDL.tmpPath="";
        try{
        // Create temp file.
        Path tempfile = Files.createTempFile("fDLfile", ".txt");
        PathfDL.tmpPath= tempfile.toString();

        // Delete temp file when program exits.
     //   temp.deleteOnExit();

        // Write to temp file
//        BufferedWriter out = new BufferedWriter(new FileWriter(tempfile));
//        String strOut= new String();
//        String[] args = {PathIn,URLOut};
//        System.out.println("ARG===============" + args[0]);
//        System.out.println("ARG===============" + args[1]+ "/////////");
  //      FuzzyOwl2toFuzzyDL.startTranslation(args);
 //       System.out.println("ARG===============Translated"+ "/////////");
 //       out.write(strOut);
 //       out.write(" sdasdsad");
        
        
        //////
  //      Charset charset = Charset.forName("UTF-8");
  //      String s ="I&T*IUG*OI(GI(&";
  //      try (BufferedWriter writer = Files.newBufferedWriter(tempfile, charset)) {
  //          writer.write(s, 0, s.length());
  //      } catch (IOException x) {
  //          System.err.format("IOException: %s%n", x);
  //      }

       // out.close();
        
        String[] args = {PathIn,tempfile.toString()};
       
        FuzzyOwl2toFuzzyDL.startTranslation(args, argIn,tempfile);
        System.out.println("PRINT===============" + readFile(tempfile.toString(),Charset.forName("UTF-8"))) ;
        
        
        } catch (IOException ioe) {}
        
    }
    
    static String readFile(String path, Charset encoding) 
          throws IOException 
        {
          byte[] encoded = Files.readAllBytes(Paths.get(path));
          return encoding.decode(ByteBuffer.wrap(encoded)).toString();
        }
    public static class tmpFilePath
    {
        public String tmpPath="";       
        tmpFilePath(String p){
            tmpPath= p;
        }
    }
    
}
