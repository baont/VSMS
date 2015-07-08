/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsmsdataparser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bao
 */
public class NewClass {
    private static class S {
        public String name;
        public int id;

        public S(String name, int id) {
            this.name = name;
            this.id = id;
        }
        
    }
   public static void main(String[] args) {
       List<S> aList = new ArrayList<>();
       aList.add(new S("s", 1));
       S s2 = new S("s2", 2);
       aList.add(s2);
       aList.add(new S("s3", 3));
       
       int i = aList.indexOf(s2);
       System.out.println(i);
   } 
}
