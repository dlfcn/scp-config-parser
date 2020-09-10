/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package titanium.scp.config.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author dfontana
 */
public class Main {
    
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final String FILE_LOCATION = "C:\\Users\\dfontana\\Downloads\\mboutin-8.0.0-190-20200817-202836.txt";
    
    //User Defined Dissectors
    private static final String DISSECTOR = "dissector ";
    
    //HTTP Peers and Locals
    private static final String HTTP_LOCAL = "http-local";
    private static final String HTTP_PEER = "http-peer";
    
    //NRF-5G Application Schemas
    private static final String NRF_5G_CONFIGURATION = "nrf-5g-configuration";
    private static final String NRF_5G_LOCAL_INSTANCES = "nrf-5g-local-instance";
    private static final String NRF_5G_SERVICE_INSTANCE = "nrf-5g-service-instance";
    
    //BSF-5G Application Schemas
    private static final String BSF_5G_CONFIGURATION = "bsf-5g-configuration";
    private static final String BSF_5G_LOCAL_INSTANCES = "bsf-5g-local-instance";
    private static final String BSF_5G_CACHE_INSTANCE = "bsf-5g-cache-instance";
    private static final String BSF_5G_SCENARIO_GROUP = "bsf-5g-scenario-group";
    private static final String BSF_5G_SCENARIO_INSTANCE = "bsf-5g-scenario-instance";
    private static final String BSF_5G_SERVICE_INSTANCE = "bsf-5g-service-instance";

    //BSF-Diameter Application Schema
    private static final String BSF_DIAM_CONFIGURATION = "bsf-diameter-configuration";
    private static final String BSF_DIAM_CACHE_INSTANCE = "bsf-diameter-cache-instance";
    private static final String BSF_DIAM_SCENARIO_GROUP = "bsf-diameter-scenario-group";
    private static final String BSF_DIAM_SCENARIO_INSTANCE = "bsf-diameter-scenario-instance";
    private static final String BSF_DIAM_NO_SESSION_BINDING = "bsf-diameter-no-session-binding";
    private static final String BSF_DIAM_ROUTE = "bsf-diameter-route";
    private static final String BSF_DIAM_SERVICE_INSTANCE = "bsf-diameter-service-instance";
    
    //SCP-5G Application Schemas
    private static final String SCP_5G_CONFIGURATION = "scp-5g-configuration";
    private static final String SCP_5G_LIST = "scp-5g-configuration";
    private static final String SCP_5G_LOCAL = "scp-5g-local-instance";
    private static final String SCP_5G_ROUTE_LIST = "scp-5g-route-list";
    private static final String SCP_5G_ROUTE_INSTANCE = "scp-5g-route-instance";
    private static final String SCP_5G_ACTION_INSTANCE = "scp-5g-action-instance";
    private static final String SCP_5G_RULE_LIST = "scp-5g-rule-list";
    private static final String SCP_5G_RULE_INSTANCE = "scp-5g-rule-instance";
    private static final String SCP_5G_SERVICE_INSTANCE = "scp-5g-service-instance";
    private static final String SCP_5G_TRANS_LIST = "scp-5g-transformation-list";
    private static final String SCP_5G_TRANS_INST = "scp-5g-transformation-instance";
    private static final String SCP_5G_TEST_INSTANCE = "scp-5g-test-instance"; //deprecated
    
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
            List<String> lines = new ArrayList<>();
            
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            
            // Dissectors
            parseToken(DISSECTOR, lines, CREATE);
            
            // TITAN 8 HTTP Facility
            parseToken(HTTP_LOCAL, lines, CREATE);
            parseToken(HTTP_PEER, lines, CREATE);
            
            // NRF
            parseToken(NRF_5G_CONFIGURATION, lines, UPDATE);
            parseToken(NRF_5G_LOCAL_INSTANCES, lines, CREATE);
            parseToken(NRF_5G_SERVICE_INSTANCE, lines, CREATE);
            
            // BSF
            parseToken(BSF_5G_CONFIGURATION, lines, UPDATE);
            parseToken(BSF_5G_LOCAL_INSTANCES, lines, CREATE);
            parseToken(BSF_5G_CACHE_INSTANCE, lines, CREATE);
            parseToken(BSF_5G_SCENARIO_GROUP, lines, CREATE);
            parseToken(BSF_5G_SCENARIO_INSTANCE, lines, CREATE);
            parseToken(BSF_5G_SERVICE_INSTANCE, lines, CREATE);
            
            // BSF (Diameter)
            parseToken(BSF_DIAM_CONFIGURATION, lines, UPDATE);
            parseToken(BSF_DIAM_CACHE_INSTANCE, lines, CREATE);
            parseToken(BSF_DIAM_SCENARIO_GROUP, lines, CREATE);
            parseToken(BSF_DIAM_SCENARIO_INSTANCE, lines, CREATE);
            parseToken(BSF_DIAM_NO_SESSION_BINDING, lines, UPDATE);
            parseToken(BSF_DIAM_ROUTE, lines, UPDATE);
            parseToken(BSF_DIAM_SERVICE_INSTANCE, lines, CREATE);
            
            // SCP
            parseToken(SCP_5G_CONFIGURATION, lines, UPDATE);
            parseToken(SCP_5G_LIST, lines, CREATE);
            parseToken(SCP_5G_LOCAL, lines, CREATE);
            parseToken(SCP_5G_ROUTE_LIST, lines, CREATE);
            parseToken(SCP_5G_ROUTE_INSTANCE, lines, CREATE);
            parseToken(SCP_5G_ACTION_INSTANCE, lines, CREATE);
            parseToken(SCP_5G_RULE_LIST, lines, CREATE);
            parseToken(SCP_5G_RULE_INSTANCE, lines, CREATE);
            parseToken(SCP_5G_SERVICE_INSTANCE, lines, CREATE);
            parseToken(SCP_5G_TRANS_LIST, lines, CREATE);
            parseToken(SCP_5G_TRANS_INST, lines, CREATE);
            parseToken(SCP_5G_TEST_INSTANCE, lines, CREATE);
        }
    }
    
    private static List<String> parseToken(String token, List<String> lines, String prefix) {
        
        List<String> configList = new ArrayList<>();
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
        
        for (String config : configList) {
            System.out.println(prefix + " " + config);
        }
        
        if (!configList.isEmpty()) {
            System.out.println();
        }
        return configList;
    }
    
}
