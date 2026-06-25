/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FOnto;

import java.io.*;
import java.util.*;
import fuzzydl.*;
import fuzzydl.exception.*;
import fuzzydl.milp.*;
import fuzzydl.parser.*;
import fuzzydl.util.*;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

 /*
 * @author Nguyen Duc Duy - 10520502
 */
public class FuzzyOntologyQuery {
    public static void ActiveParser(String fDL,KnowledgeBase kb )throws FuzzyOntologyException,
    IOException, ParseException 
    {
        String fDLURL = fDL;
    System.out.println("1.1=============== Entered");
    // Load options for the reasoner, using file "CONFIG"
    ConfigReader.loadParameters("C:\\Users\\Mr. Noname\\Documents\\NetBeansProjects\\FuzzyOnto\\src\\FOnto\\CONFIG", new String[0]);
    System.out.println("1.2=============== Loaded");
    // Option 1. Create KnowledgeBase and queries from scratch
    //KnowledgeBase kb = new KnowledgeBase();
    // Add axioms to the KnowledgeBase. Example: kb.addAssertion(...);
    // Define queries: Query q = new MinSatisfiableQuery(Concept c);
    
    // Option 2. Read knowledge base and queries from file "fileName.txt"
   // URL url=getClass().getResource("config.xml");
    
   
    Parser parser = new Parser(new FileInputStream(fDLURL));
    System.out.println("1.3=============== Parsed");
    parser.Start();    
    kb = parser.getKB();
    System.out.println("1.4=============== Got KB");
    // The three latter lines can be replaced by the following one
    //KnowledgeBase kb = Parser.getKB("KBText.kbt");
    // Queries were also part of the file "fileName.txt"

    ArrayList <Query> queries = parser.getQueries();
    // After having created KB and queries, start logical inference
        
     kb.solveKB();
    
     System.out.println("1.5=============== KB Solved! ");
     
             for (Query q :queries )   {
    /////////    

        // Solve a query q
        Solution result = q.solve(kb);
        // Print the result
        if (result.isConsistentKB())
        System.out.println(q.toString() + result.getSolution());
        // In AllInstancesQuery: System.out.println(q.toString());
        else
        System.out.println("KB is inconsistent");
        // Optionally, show the time and language of the KB
        System.out.println("Time (s): " + q.getTotalTime());
        System.out.println("Language: " + kb.getLanguage());
        }
       

 }
    
    }
        
    
           


