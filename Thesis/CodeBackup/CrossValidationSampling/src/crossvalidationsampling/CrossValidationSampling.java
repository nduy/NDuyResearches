/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossvalidationsampling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author Administrator
 */
// PLEASE NOTE!! the order of vetors in SVMtrain is exactly in FullTextTrain_Output_Raw
// but, it is completlely different from sed2013_task2_dataset_train_gs
//therefore, what we are about to do is:
// Get picID form FullTextTrain_Output_Raw, add training vector that have the same index, then use the picID to get ground truth(gs)

public class CrossValidationSampling {

    /**
     * @param args the command line arguments
     */
    // args[0] -> fullTestTrain_output_raw
    // args[1] -> seed2013_task_dataset_train
    public static String fullTrainPath = "3. SVMTrainFile.txt";
    public static String fullTextPath = "FullTextTrain_Output_Raw.txt";
    public static String gsPath= "sed2013_task2_dataset_train_gs.csv";

    public static Map<String,String> gsMap = new HashMap<>();   // picID, Event_type
    public static List<PicData> fullData = new ArrayList<>();
    public static List<String> fullIDList = new ArrayList<>();

    public static List<PicData> positiveList = new ArrayList<>();
    public static List<PicData> negativeList = new ArrayList<>();
    public static List<List<PicData>> folds = new ArrayList<>();
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        // Load ground truth
        int DATA_SIZE = 0;      
        try (BufferedReader br = new BufferedReader(new FileReader(gsPath))){
            String sCurrentLine = br.readLine(); // Skip first line
            while ((sCurrentLine = br.readLine()) != null) {
                if (!sCurrentLine.isEmpty()) {
                    String [] item = sCurrentLine.split("	"); // Control Character, not space!!!
                    if (item.length==2) {
                        gsMap.put(item[0],item[1]);
                    }
                            
                }
            }
            DATA_SIZE = gsMap.size();
        } catch (IOException e){e.printStackTrace();}  
        
        // Load original FullTextTrain_Output_Raw
        try (BufferedReader br = new BufferedReader(new FileReader(fullTextPath))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if (!sCurrentLine.isEmpty()) {
                    String [] item = sCurrentLine.split("	"); // Control Character, not space!!!
                    if (item.length==2 && gsMap.containsKey(item[0])) {
                        fullIDList.add(item[0]);
                    } else System.out.println("Error - Abnormal record! ");
                            
                }
            }
        } catch (IOException e){e.printStackTrace();}
        
        // Load train vectors
         try (BufferedReader br = new BufferedReader(new FileReader(fullTrainPath))){
            String sCurrentLine;
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                if (!sCurrentLine.isEmpty()) {
                   if (count <DATA_SIZE) {
                       String picID =fullIDList.get(count);
                       PicData item = new PicData(picID,sCurrentLine, gsMap.get(picID));   
                       fullData.add(item);
                       count++;
                   }
                   else if (count >DATA_SIZE) {
                       System.out.println("Error - Miss match datasize");
                   }
                            
                }
            }
        } catch (IOException e){e.printStackTrace();}
         
        // Now split negative and positive to two list
         for (PicData item: fullData){
             if (item.label.equals("non_event")){
                 negativeList.add(item);
             } else positiveList.add(item);
         }
         
         // Now shuffle the lists
         long seed = System.nanoTime();
        Collections.shuffle(negativeList, new Random(seed));
        Collections.shuffle(positiveList, new Random(seed));
         
        // Now save into 10 folds
            // Init 10 folds
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());
         folds.add(new LinkedList());        
        
             // Add positive items to folds
        int count = 0;
        for (PicData item: positiveList){
            folds.get(count).add(item);
            count = (++count) % 10;
          }
        
            // Add negative items to folds
        count = 0;
        for (PicData item: negativeList){
            folds.get(9-count).add(item);
            count = (++count) % 10;
          }
      
        // Now print some data to file 
        // 1. Full data
        try {
            File file1 = new File("1.Fulldata.txt");
            if (!file1.exists()) { 
                file1.createNewFile();
                }
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
            for (PicData item: fullData){
                bw1.write(item.picID +" " +item.label +" "+ item.vector + "\n");
            }
            bw1.close();
        } catch (IOException e) {e.printStackTrace();}
        
        // 2. Negative
            try {
                File file1 = new File("2.Negative.txt");
                if (!file1.exists()) { 
                    file1.createNewFile();
                    }
                BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
                for (PicData item: negativeList){
                    bw1.write(item.picID +" " +item.label +" "+ item.vector + "\n");
                }
                bw1.close();
            } catch (IOException e) {e.printStackTrace();}
        // 3. Positives
            try {
                File file1 = new File("3.Positive.txt");
                if (!file1.exists()) { 
                    file1.createNewFile();
                    }
                BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
                for (PicData item: positiveList){
                    bw1.write(item.picID +" " +item.label +" "+ item.vector + "\n");
                }
                bw1.close();
            } catch (IOException e) {e.printStackTrace();}
        // 4. Folds
            for (int i=0; i<10; i++){
                try {
                    File file1 = new File("4.Folds"+ i+".txt");
                    if (!file1.exists()) { 
                        file1.createNewFile();
                        }
                    BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
                    for (PicData item: folds.get(i)){
                        bw1.write(item.picID +" " +item.label +" "+ item.vector + "\n");
                    }
                    bw1.close();
                } catch (IOException e) {e.printStackTrace();}
            }
        
        // Now write to files
        for (int i = 0; i<10; i++){
            // Write the test set: the fold itself and gound-truth
            try {
                File foldFolder = new File("fold"+i);
                if (!foldFolder.exists()) {
                   if (foldFolder.mkdir()) { System.out.println("Directory is created!"); } 
                   else { System.out.println("Failed to create directory!");}
                } // end if
                
                File file1 = new File("fold"+ i + "\\3. SVMTestFile.txt");
                File file2 = new File("fold"+ i + "\\sed_task2_gs.txt");
                File file3 = new File("fold"+ i + "\\3. PicIDList.txt");
                
                if (!file1.exists()) { 
                    file1.createNewFile();
                }
                
                if (!file2.exists()) { 
                    file2.createNewFile();
                }
                   
                if (!file3.exists()) { 
                    file3.createNewFile();
                }
                
                BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2));
                BufferedWriter bw3 = new BufferedWriter(new FileWriter(file3));
                
                for (PicData item: folds.get(i)){
                    bw1.write(item.vector + "\n");
                    bw2.write(item.picID + " " + item.label+ "\n");
                    bw3.write(item.picID + "\n");
                }
                
                bw1.close();
                System.out.println("Test fold " + i  + " writing done!");
                bw2.close();
                System.out.println("Test fold gs " + i  + " writing done!");
                bw3.close();
                System.out.println("PicID list " + i  + " writing done!");
            } catch (IOException e) {e.printStackTrace();}
            

            // Merge the remaining and write them
             try {
                
                File file1 = new File("fold"+ i + "\\3. SVMTrainFile.txt");
                
                if (!file1.exists()) { 
                    file1.createNewFile();
                }
                
                               
                BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
                

                for (int j = 0; j<10; j++){
                    if (j==i) continue;
                    for (PicData item: folds.get(j)){
                        bw1.write(item.vector + "\n");
                        
                    }
                }
                
                bw1.close();
                System.out.println("Train fold " + i  + " writing done!");
            } catch (IOException e) {e.printStackTrace();}
            
            

        }
    }
    
    
    
}
