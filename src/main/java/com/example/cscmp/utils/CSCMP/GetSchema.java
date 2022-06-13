package com.example.cscmp.utils.CSCMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSchema {
    
    public List<Map<Integer, Integer>> getSchema() {
    	List<Map<Integer, Integer>> result = null;
    	switch (Config.dataName) {
		case "IMDB":
			result = getIMDBSchema();
			break;

		case "newDBLP":
			result = getDBLPSchema();
			break;
			
		case "FourSquare":
			result = getFSSchema();
			break;
			
		case "Freebase":
			result = getFBSchema();
			break;
			
		case "DBPedia":
			result = getDBPSchema();
			break;

		case "S-DBLP":
			result = getDBLPSchema();
			break;
		}
        
        return result;
    }
    
    private List<Map<Integer, Integer>> getDBLPSchema() {
        List<Map<Integer, Integer>> result = new ArrayList<Map<Integer,Integer>>();
        Map<Integer, Integer> tempMap = new HashMap<>();
        tempMap.put(1, 0);tempMap.put(2, 1);tempMap.put(3, 2);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 3);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 4);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 5);
        result.add(tempMap);
        return result;
    }
    
    private List<Map<Integer, Integer>> getIMDBSchema() {
        List<Map<Integer, Integer>> result = new ArrayList<Map<Integer,Integer>>(); 
        Map<Integer, Integer> tempMap = new HashMap<>();
        tempMap.put(1, 0);tempMap.put(2, 2);tempMap.put(3, 4);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 1);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 3);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 5);
        result.add(tempMap);
        return result;
    }
    
    private List<Map<Integer, Integer>> getFSSchema() {
        List<Map<Integer, Integer>> result = new ArrayList<Map<Integer,Integer>>(); 
        Map<Integer, Integer> tempMap = new HashMap<>();
        tempMap.put(1, 0);tempMap.put(2, 2);tempMap.put(3, 4);tempMap.put(4, 6);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 1);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 3);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 5);
        result.add(tempMap);
        tempMap = new HashMap<>();
        tempMap.put(0, 7);
        result.add(tempMap);
        return result;
    }
    
    private List<Map<Integer, Integer>> getFBSchema() { 
        List<Map<Integer, Integer>> result = new ArrayList<Map<Integer,Integer>>(); 
        Map<Integer, Integer> tempMap = new HashMap<>();
        
        for (int i = 0; i < 75; i++) {
        	tempMap = new HashMap<>();
            result.add(tempMap);
		}

        System.out.println("reslut.size:" + result.size());
        tempMap = result.get(4);
        tempMap.put(8, 2);tempMap.put(48, 2);
//        reslut.add(tempMap);
        
        tempMap = result.get(8);
        tempMap.put(59, 1);tempMap.put(4, 9);
//        reslut.add(tempMap);
        
        tempMap = result.get(10);
        tempMap.put(11, 0);tempMap.put(59, 2);
//        reslut.add(tempMap);
        
        tempMap = result.get(11);
        tempMap.put(10, 4);tempMap.put(59, 6);
//        reslut.add(tempMap);
        
        tempMap = result.get(59);
        tempMap.put(8, 17);tempMap.put(10, 17);tempMap.put(11, 21);tempMap.put(48, 17);
//        reslut.add(tempMap);
        
        tempMap = result.get(48);
        tempMap.put(59, 1);tempMap.put(4, 9);
//        reslut.add(tempMap);
        
        return result;
    }
    
    private List<Map<Integer, Integer>> getDBPSchema() { 
        List<Map<Integer, Integer>> result = new ArrayList<Map<Integer,Integer>>(); 
        Map<Integer, Integer> tempMap = new HashMap<>();
        
        for (int i = 0; i < 423; i++) {
        	tempMap = new HashMap<>();
        	result.add(tempMap);
		}

        System.out.println("reslut.size:" + result.size());
        tempMap = result.get(0);
        tempMap.put(422, 1);tempMap.put(13, 1);tempMap.put(8, 699);
//        reslut.add(tempMap);
        
        tempMap = result.get(2);
        tempMap.put(422, 3);tempMap.put(8, 717);tempMap.put(38, 744);
//        reslut.add(tempMap);
        
        tempMap = result.get(8);
        tempMap.put(0, 36);tempMap.put(2, 8);tempMap.put(13, 115);tempMap.put(38, 7);tempMap.put(422, 7);
//        reslut.add(tempMap);
        
        tempMap = result.get(13);
        tempMap.put(8, 778);tempMap.put(38, 665);
//        reslut.add(tempMap);
        
        tempMap = result.get(38);
        tempMap.put(2, 668);tempMap.put(8, 670);tempMap.put(13, 667);tempMap.put(422, 1);
//        reslut.add(tempMap);
        
        tempMap = result.get(422);
        tempMap.put(0, 664);tempMap.put(2, 666);tempMap.put(8, 707);tempMap.put(38, 716);
//        reslut.add(tempMap);
        
        return result;
    }
}
