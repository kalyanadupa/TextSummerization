/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosinesimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kalyan
 */
public class CosineSimilarity {

    static HashMap<Integer,String> map = new HashMap<Integer,String>();
    static List<Integer> nodeWord = new ArrayList<Integer>();
    public class values {

        int val1;
        int val2;

        values(int v1, int v2) {
            this.val1 = v1;
            this.val2 = v2;
        }

        public void Update_VAl(int v1, int v2) {
            this.val1 = v1;
            this.val2 = v2;
        }
    }//end of class values

    public double CosineSimilarity_Score(String Text1, String Text2) {
        double sim_score = 0.0000000;
        //1. Identify distinct words from both documents
        String[] word_seq_text1 = Text1.split(" ");
        String[] word_seq_text2 = Text2.split(" ");
        Hashtable<String, values> word_freq_vector = new Hashtable<String, CosineSimilarity.values>();
        LinkedList<String> Distinct_words_text_1_2 = new LinkedList<String>();

        //prepare word frequency vector by using Text1
        for (int i = 0; i < word_seq_text1.length; i++) {
            String tmp_wd = word_seq_text1[i].trim();
            if (tmp_wd.length() > 0) {
                if (word_freq_vector.containsKey(tmp_wd)) {
                    values vals1 = word_freq_vector.get(tmp_wd);
                    int freq1 = vals1.val1 + 1;
                    int freq2 = vals1.val2;
                    vals1.Update_VAl(freq1, freq2);
                    word_freq_vector.put(tmp_wd, vals1);
                } else {
                    values vals1 = new values(1, 0);
                    word_freq_vector.put(tmp_wd, vals1);
                    Distinct_words_text_1_2.add(tmp_wd);
                }
            }
        }

        //prepare word frequency vector by using Text2
        for (int i = 0; i < word_seq_text2.length; i++) {
            String tmp_wd = word_seq_text2[i].trim();
            if (tmp_wd.length() > 0) {
                if (word_freq_vector.containsKey(tmp_wd)) {
                    values vals1 = word_freq_vector.get(tmp_wd);
                    int freq1 = vals1.val1;
                    int freq2 = vals1.val2 + 1;
                    vals1.Update_VAl(freq1, freq2);
                    word_freq_vector.put(tmp_wd, vals1);
                } else {
                    values vals1 = new values(0, 1);
                    word_freq_vector.put(tmp_wd, vals1);
                    Distinct_words_text_1_2.add(tmp_wd);
                }
            }
        }

        //calculate the cosine similarity score.
        double VectAB = 0.0000000;
        double VectA_Sq = 0.0000000;
        double VectB_Sq = 0.0000000;

        for (int i = 0; i < Distinct_words_text_1_2.size(); i++) {
            values vals12 = word_freq_vector.get(Distinct_words_text_1_2.get(i));

            double freq1 = (double) vals12.val1;
            double freq2 = (double) vals12.val2;
            //System.out.println(Distinct_words_text_1_2.get(i) + "#" + freq1 + "#" + freq2);

            VectAB = VectAB + (freq1 * freq2);

            VectA_Sq = VectA_Sq + freq1 * freq1;
            VectB_Sq = VectB_Sq + freq2 * freq2;
        }
        //System.out.println("VectAB " + VectAB + " VectA_Sq " + VectA_Sq + " VectB_Sq " + VectB_Sq);
        sim_score = ((VectAB) / (Math.sqrt(VectA_Sq) * Math.sqrt(VectB_Sq)));

        return (sim_score);
    }
    
    public static LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String) key, (Double) val);
                    break;
                }

            }

        }
        return sortedMap;
    }    

    public static void main(String[] args) throws FileNotFoundException, IOException {
        CosineSimilarity cs1 = new CosineSimilarity();
        List<String> allStrings = new ArrayList<String>();
        //BufferedReader in = new BufferedReader(new FileReader("N05-1042.list"));
        PrintWriter out = new PrintWriter("String.nodes", "UTF-8");
        File[] allfiles = new File("new").listFiles();
        BufferedReader in = null;
        int idx = 0;
        String s = null;
        
        for (File f : allfiles) {
                in = new BufferedReader(new FileReader(f));
                while ((s = in.readLine()) != null) {
                    if(s.compareTo("") != 0){
                        allStrings.add(s);
                        String[] count = s.split(" ");
                        System.out.println("idx => "+ count.length);
                        map.put(idx, s);
                        nodeWord.add(count.length);
                        out.println(idx + " " + s);
                        idx++;
                    }
                }            
        }
        //map = sortHashMapByValuesD(map);
        
//        PrintWriter writer = new PrintWriter("StringNearNum.edges", "UTF-8");
//        for(int i =0;i < (allStrings.size());i++){
//            for(int j = i; j<(allStrings.size());j++){
//                double sim = cs1.CosineSimilarity_Score(allStrings.get(i), allStrings.get(j));
//                sim = 1 - sim;
//                if(sim == 1)
//                    sim = 0;
//                else if(sim == 0)
//                    sim = 0.01;
//                sim = sim*100;
//                int num = (int) sim;
//                if((i != j)&&(sim != 0)&&(!Double.isNaN(sim))&&(num != 0))
//                    writer.println(i+" "+j+" "+ num);
//                    //writer.println(i+" "+j+" "+ (float)sim);
//            }
//        }
        PrintWriter writer = new PrintWriter("StringPB20.edges", "UTF-8");        
        //PrintWriter writer2 = new PrintWriter("StringNum60.edges", "UTF-8");
        for(int i =0;i < (allStrings.size());i++){
            for(int j = i; j<(allStrings.size());j++){
                double sim = cs1.CosineSimilarity_Score(allStrings.get(i), allStrings.get(j));
                //sim = sim*100;
                //int num = (int) sim;
                if((i != j)&&(sim != 0)&&(!Double.isNaN(sim))&&(sim > 0.2)){
                    writer.println(i+" "+j+" "+sim);
                    //writer2.println(i+" "+j+" "+ num);
                    //writer.println(i+" "+j+" "+ (float)sim);
                }
                else
                    writer.println(i+" "+j+" "+sim);
                      
            }
        }        
        writer.close();
    }
}
