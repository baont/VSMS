/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsmsdataparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;

/**
 *
 * @author bao
 */
public class VSMSDataParser {
    private static void display() {
        StringItem superParent = new StringItem(-1, "null", null, null);
        superParent.children.addAll(items);
        superParent.id = 0;
        items.stream().forEach((item) -> {
            item.parent = superParent;
        });
        
        display(superParent);
    }

    private static void display(StringItem item) {
        int caseNum = item.id;
        System.out.println("case " + caseNum + ":");
        for (int i = 0; i < item.children.size(); i++) {
            StringItem child = item.children.get(i);
            int childId = caseNum * 100 + (i + 1);
            child.id = childId;
            if (child.to == null) {
                System.out.println("  addNewScreenItem(\"" + child.name + "\", " + childId + ");");
            } else {
                List<String> eles = parseSmsSyntax(child.smssyntax);
                String str1 = "  addAotmicItem(\"" + child.name + "\"," +  2000 + ",\"" ;
                for (int j = 0; j < eles.size(); j++) {
                    str1 += eles.get(j) + "\",\"";
//                    if (j != eles.size()) {
//                        str1 += "\",\"";
//                    }
                }
                str1 += child.to + "\");";
                System.out.println(str1);
            }
        }
        System.out.println("break;");
        
        for (StringItem child: item.children) {
            if (child.to == null) {
                display(child);
            }
        }
    }

    private static List<String> parseSmsSyntax(String smssyntax) {
        List<String> rs = new ArrayList(2);
        StringTokenizer tokenizer = new StringTokenizer(smssyntax);
        String vmsmstoken = tokenizer.nextToken();
        String originalCode = tokenizer.nextToken();
        String code = originalCode.substring(0, originalCode.length() - 3);
        rs.add(code);
        String aftercode = tokenizer.nextToken();
        if (!aftercode.equals("JAVA")) {
            rs.add(aftercode);
        }
        
        return rs;
    }

    /**
     * @param args the command line arguments
     */
    private static class StringItem {

        public int level;
        public String name;
        public String smssyntax;
        public String to;
        public List<StringItem> children;
        public StringItem parent;
        public int id;

        public StringItem(int level, String name, String smssyntax, String to) {
            this.level = level;
            this.name = name;
            this.smssyntax = smssyntax;
            this.to = to;
            children = new LinkedList();
        }

        
        @Override
        public String toString() {
            if (level == 1 || level == 2) {
                if (smssyntax != null && to == null) {
                    return "ERROR " + name;
                } else if (to != null) {
                    try {
                        int i = Integer.parseInt(to);
                    } catch (Exception e) {
                        return "ERROR can not parse to " + name;
                    }
                }
            }
            switch (level) {
                case 0:
                    String rs =  name;
                    for (StringItem item : children) {
                        rs += "\n" + item.toString();
                    }
                    return rs;
                case 1:
                    if (smssyntax != null) {
                        return "       " + name + ", smssyntax=" + smssyntax + ", to=" + to;
                    } else {
                        return "       " + name;
                    }
                case 2:
                    return "                 " + name + ", smssyntax=" + smssyntax + ", to=" + to;
            }
            return null;
        }
    }

    private static int countWhileSpace(String st) {
        char ch = st.charAt(0);
        int count = 0;
        while (Character.isWhitespace(ch) && count < st.length() - 1) {
            count++;
            ch = st.charAt(count);
        }
        return count;
    }

//    String[] items;
    private static final List<StringItem> items = new LinkedList<>();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        readFromFile();
        System.out.println("done");
        display();
//        traverse(item -> verifySMSsyntax(item.smssyntax));
    }
    
    private static void verifySMSsyntax(String syntax) {
        StringTokenizer tokenizer = new StringTokenizer(syntax);
        tokenizer.nextToken();
        String code = tokenizer.nextToken();
        if (code.endsWith("JAV")) {
//            System.out.println("OK");
        } else {
            System.out.println("FALSE");
        }
    }

    private static void readFromFile() throws FileNotFoundException, IOException {
        File file = new File("data.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String st = br.readLine();

        String lastTo = null;
        StringItem currentParent = null;
        while (st != null) {
            try {
                int whiteSpce = countWhileSpace(st);
                StringItem item = new StringItem(whiteSpce, null, null, null);
                switch (whiteSpce) {
                    case 0:
                        item.name = st.trim();
                        items.add(item);
                        currentParent = item;
                        break;
                    case 1:
                        int vmsmTokenPos = st.indexOf("VMSMS");
                        if (vmsmTokenPos == -1) {
                            item.name = st.trim();
                        } else {
                            item.name = st.substring(0, vmsmTokenPos).trim();
                            StringTokenizer tokenizer = new StringTokenizer(st);
                            String token = tokenizer.nextToken();
                            while (!"VMSMS".equals(token)) {
                                token = tokenizer.nextToken();
                            }
                            item.smssyntax = token;
                            while (!token.startsWith("JAV")) {
                                token = tokenizer.nextToken();
                                item.smssyntax += " " + token;
                            }
                            if (tokenizer.hasMoreTokens()) {
                                item.to = tokenizer.nextToken();
                            } else {
                                item.to = lastTo;
                            }
                        }

                        while (currentParent.level != 0) {
                            currentParent = currentParent.parent;
                        }
                        currentParent.children.add(item);
                        item.parent = currentParent;
                        currentParent = item;
                        break;
                    case 2:
                        vmsmTokenPos = st.indexOf("VMSMS");
                        item.name = st.substring(0, vmsmTokenPos).trim();
                        StringTokenizer tokenizer = new StringTokenizer(st);
                        String token = tokenizer.nextToken();
                        while (!"VMSMS".equals(token)) {
                            token = tokenizer.nextToken();
                        }
                        item.smssyntax = token;
                        while (!token.startsWith("JAV")) {
                            token = tokenizer.nextToken();
                            item.smssyntax += " " + token;
                        }
                        if (tokenizer.hasMoreTokens()) {
                            item.to = tokenizer.nextToken();
                        } else {
                            item.to = lastTo;
                        }

                        while (currentParent.level != 1) {
                            currentParent = currentParent.parent;
                        }
                        currentParent.children.add(item);
                        item.parent = currentParent;
                        break;
                }
//                System.out.println(item);
                lastTo = item.to;
                st = br.readLine();
            } catch (Exception ex) {
                System.out.println("ERRROR                " + st);
            }
        }
    }
    
    private static void traverse(Consumer<? super StringItem> csm) {
        for (StringItem item : items) {
            if (item.to != null) {
                csm.accept(item);
            } else {
                traverse(item, csm);
            }
        }
    }

    private static void traverse(StringItem item, Consumer<? super StringItem> csm) {
        for (StringItem child : item.children) {
            if (child.to != null) {
                csm.accept(child); 
            } else {
                traverse(child, csm);
            }
        }
    }
}
