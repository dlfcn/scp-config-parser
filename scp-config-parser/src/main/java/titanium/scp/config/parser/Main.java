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
    
    private static final boolean TRACE = false;
    private static final String PREPREND = "create";
    private static final String FILE_LOCATION = "C:\\Users\\dfontana\\Downloads\\mboutin-8.0.0-190-20200603-031212.txt";
    
    //HTTP Peers and Locals
    private static final String HTTP_LOCAL = "http-local";
    private static final String HTTP_PEER = "http-peer";
    
    //NRF-5G Application Schemas
    private static final String NRF_5G_CONFIGURATION = "nrf-5g-configuration";
    private static final String NRF_5G_SERVICE_INSTANCE = "nrf-5g-service-instance";
    
    //BSF-5G Application Schemas
    private static final String BSF_5G_CONFIGURATION = "bsf-5g-configuration";
    private static final String BSF_5G_CACHE_INSTANCE = "bsf-5g-cache-instance";
    private static final String BSF_5G_SCENARIO_GROUP = "bsf-5g-scenario-group";
    private static final String BSF_5G_SCENARIO_INSTANCE = "bsf-5g-scenario-instance";
    private static final String BSF_5G_SERVICE_INSTANCE = "bsf-5g-service-instance";
    
    //SCP-5G Application Schemas
    private static final String ROUTE_LIST = "scp-5g-route-list";
    private static final String ROUTE_INSTANCE = "scp-5g-route-instance";
    private static final String ACTION_INSTANCE = "scp-5g-action-instance";
    private static final String RULE_LIST = "scp-5g-rule-list";
    private static final String RULE_INSTANCE = "scp-5g-rule-instance";
    private static final String NETWORK_INSTANCE = "scp-5g-service-instance";
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
            
            System.out.println("\n\n\n");
            parseToken(HTTP_LOCAL, lines);
            parseToken(HTTP_PEER, lines);
            
            System.out.println("\n\n\n");
            parseToken(NRF_5G_CONFIGURATION, lines);
            parseToken(NRF_5G_SERVICE_INSTANCE, lines);
            
            System.out.println("\n\n\n");
            parseToken(BSF_5G_CONFIGURATION, lines);
            parseToken(BSF_5G_CACHE_INSTANCE, lines);
            parseToken(BSF_5G_SCENARIO_GROUP, lines);
            parseToken(BSF_5G_SCENARIO_INSTANCE, lines);
            parseToken(BSF_5G_SERVICE_INSTANCE, lines);
            
            System.out.println("\n\n\n");
            parseToken(ROUTE_LIST, lines);
            parseToken(ROUTE_INSTANCE, lines);
            parseToken(ACTION_INSTANCE, lines);
            parseToken(RULE_LIST, lines);
            parseToken(RULE_INSTANCE, lines);
            parseToken(NETWORK_INSTANCE, lines);
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
                
                boolean containsClob = false;
                boolean clobEndFound = true;
                
                while (++index < lines.size()) {
                    
                    line = lines.get(index).trim();
                    
                    if (line.contains("body=<CLOB>") && !line.contains("</CLOB>")) {
                        containsClob = true;
                        clobEndFound = false;
                    }
                    
                    if (line.length() == 0 && (!containsClob && clobEndFound)) {
                        break;
                    } else {
                        if (line.contains("</CLOB>")) {
                            containsClob = false;
                            clobEndFound = true;
                        }
                        sb.append(line);
                        sb.append(" ");
                    }
                }
                configList.add(sb.toString());
            }
        }
        
        if (TRACE) {
            System.out.println(String.format("TOKEN [%s]; [%s] configs parsed", 
                    token, Integer.toString(configList.size())));
        }
        
        for (String config : configList) {
            if (PREPREND != null) {
                System.out.println(PREPREND + " " + config);
            } else {
                System.out.println(config);
            }
        }
        System.out.println();
        
        return configList;
    }
    
}
