/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package titanium.scp.config.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author dfontana
 */
public class Main {
    
    private static final boolean PREPREND_CREATE = true;
    private static final boolean INCLUDE_HTTP_FACILITY = false;
    private static final String FILE_LOCATION = "C:\\Users\\dfontana\\Downloads\\mboutin-8.0.0-190-20200312-200045.txt";
    
    //Order 1
    private static final String ROUTE_LIST = "scp-5g-route-list";
    private static final String ROUTE_INSTANCE = "scp-5g-route-instance";
    //Order 2
    private static final String ACTION_INSTANCE = "scp-5g-action-instance";
    //Order 3
    private static final String RULE_LIST = "scp-5g-rule-list";
    private static final String RULE_INSTANCE = "scp-5g-rule-instance";
    //Order 4
    private static final String HTTP_LOCAL = "http-local";
    private static final String HTTP_PEER = "http-peer";
    //Order 5
    private static final String NETWORK_INSTANCE = "scp-5g-network-instance";
    //Order 6
    private static final String TEST_INSTANCE = "scp-5g-test-instance";
    
    public static void main(String[] args) throws FileNotFoundException {
        
        File file = new File(FILE_LOCATION);
        
        if (!file.exists()) {
            System.out.println("Error; File does not exist");
        } else if (file.isDirectory()) {
            System.out.println("Error; File is Directory");
        } else if (!file.canRead()) {
            System.out.println("Error; Cannot read file");
        } else {
            Scanner scanner = new Scanner(file);
            ArrayList<String> lines = new ArrayList<>();
            
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            
            //Parse Route Lists then Route Instances
            parseToken(ROUTE_LIST, lines);
            parseToken(ROUTE_INSTANCE, lines);
            
            //Parse Actions
            parseToken(ACTION_INSTANCE, lines);
            
            //Parse Rule Lists then Rule Instances
            parseToken(RULE_LIST, lines);
            parseToken(RULE_INSTANCE, lines);
            
            //Parse HTTP Locals then HTTP Peers
            if (INCLUDE_HTTP_FACILITY) {
                parseToken(HTTP_LOCAL, lines);
                parseToken(HTTP_PEER, lines);
            }
            
            //Parse Network Instances
            parseToken(NETWORK_INSTANCE, lines);
            
            //Parse Test Instances
            parseToken(TEST_INSTANCE, lines);
        }
    }
    
    private static ArrayList<String> parseToken(String token, ArrayList<String> lines) {
        
        ArrayList<String> configList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        
        for (int index = 0; index < lines.size(); index++) {
            
            String line = lines.get(index).trim();
            
            if (line.contains(token)) {
                sb.setLength(0);
                sb.append(line);
                
                while (++index < lines.size()) {
                    
                    line = lines.get(index).trim();
                    
                    if (line.length() == 0) {
                        break;
                    } else {
                        sb.append(line);
                        sb.append(" ");
                    }
                }
                configList.add(sb.toString());
            }
        }
        
        if (!PREPREND_CREATE) {
            System.out.println(String.format("TOKEN [%s]; [%s] configs parsed", 
                    token, Integer.toString(configList.size())));
        }
        
        for (String config : configList) {
            if (PREPREND_CREATE) {
                System.out.println("create ".concat(config));
            } else {
                System.out.println(config);
            }
        }
        System.out.println();
        
        return configList;
    }
    
}
