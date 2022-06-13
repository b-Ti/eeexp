package com.example.cscmp.utils.CSCMP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BatchLinker {
    //data graph, including vertex IDs, edge IDs, and their link relationships
	private int[][] graph;
    //vertex -> type
	private int[] vertexType;
    //edge -> type
	private int[] edgeType ;
    //label a vertex if it participates in a meta-path
	private List<Set<Integer>> labelList;
	
	public BatchLinker(int[][] graph, int[] vertexType, int[] edgeType) {
		this.graph = graph;
		this.vertexType = vertexType;
		this.edgeType = edgeType;
	}
	
	public Set<Integer> link(int queryId, MetaPath metaPath){
		this.labelList = new ArrayList<Set<Integer>>();
		
		for(int i = 0;i < metaPath.pathLen + 1;i ++) {
			labelList.add(new HashSet<Integer>());
		}
		
		Set<Integer> rsSet = new HashSet<Integer>();
		Set<Integer> startSet = new HashSet<Integer>();
		rsSet.add(queryId);
		startSet.add(queryId);
        //System.out.println("BatchLinker: " + startSet);
		while(startSet.size() > 0) {
			Set<Integer> nextStartSet = label(startSet, metaPath);
			
			startSet = new HashSet<Integer>();
			for(int id:nextStartSet) {
				if(!rsSet.contains(id)) {
                    startSet.add(id);
                }
				rsSet.add(id);
			}
//			System.out.println("startSet.size:" + startSet.size());
		}
		
		return rsSet;
	}
	
    //for CSCMP
    public Set<Integer> link(Integer queryId, MetaPath metaPath, Map<Integer, Set<Integer>> Si) {
        this.labelList = new ArrayList<Set<Integer>>();

        for (int i = 0; i < metaPath.pathLen + 1; i++) {
            labelList.add(new HashSet<Integer>());
        }

        Set<Integer> rsSet = new HashSet<Integer>();
        Set<Integer> startSet = new HashSet<Integer>();

        rsSet.add(queryId);
        startSet.add(queryId);

        while (startSet.size() > 0) {
            System.out.println("BatchLinker: " + startSet);
            Set<Integer> nextStartSet = label_AC(startSet, metaPath, Si);
            startSet = new HashSet<Integer>();
            for (int id : nextStartSet) {
                if (!rsSet.contains(id)) {
                    startSet.add(id);
                }
                rsSet.add(id);
            }
        }

        return rsSet;
    }
    
    //for CSCMP
    public Set<Integer> link(Set<Integer> queryId, MetaPath metaPath, Map<Integer, Set<Integer>> Si) {
        this.labelList = new ArrayList<Set<Integer>>();

        for (int i = 0; i < metaPath.pathLen + 1; i++) {
            labelList.add(new HashSet<Integer>());
        }

        Set<Integer> rsSet = new HashSet<Integer>();
        Set<Integer> startSet = new HashSet<Integer>();

        rsSet.addAll(queryId);
        startSet.addAll(queryId);

        while (startSet.size() > 0) {// System.out.println("BatchLinker: " +
                                     // startSet);
            Set<Integer> nextStartSet = label_AC(startSet, metaPath, Si);

            startSet = new HashSet<Integer>();
            for (int id : nextStartSet) {
                if (!rsSet.contains(id)) {
                    startSet.add(id);
                }
                rsSet.add(id);
            }
        }

        return rsSet;
    }
	
	private Set<Integer> label(Set<Integer> startSet, MetaPath metaPath) {
		int pathLen = metaPath.pathLen;
		
		//label the first layer
		Set<Integer> set0 = labelList.get(0);
		for(int id:startSet) {
            set0.add(id);
        }
		
		//label the rest layers
		Set<Integer> batchSet = startSet;
		for(int index = 0;index < pathLen;index ++) {
			Set<Integer> nextLabelSet = labelList.get(index + 1);
			
			int targetVType = metaPath.vertex[index + 1], targetEType = metaPath.edge[index];
			Set<Integer> nextBatchSet = new HashSet<Integer>();
			for(int anchorId:batchSet) {
				int nbArr[] = graph[anchorId];
				for(int i = 0;i < nbArr.length;i += 2) {
					int nbVertexID = nbArr[i], nbEdgeID = nbArr[i + 1];
					if(targetVType == vertexType[nbVertexID] && targetEType == edgeType[nbEdgeID]) {
						if(nextLabelSet.contains(nbVertexID)) {
							//do nothing
						}else {
							if(index == metaPath.pathLen - 1) {//impose restriction
								nextBatchSet.add(nbVertexID);
							}else {
								nextBatchSet.add(nbVertexID);
							}
						}
						
//						System.out.println("BatchLinker: " + nbVertexID + " its type: " + vertexType[nbVertexID]);
					}
				}
			}
			
			for(int id:nextBatchSet) {
                nextLabelSet.add(id);
            }
			
			batchSet = nextBatchSet;
		}
		
		return batchSet;
	}

	private Set<Integer> label_AC(Set<Integer> startSet, MetaPath metaPath, Map<Integer, Set<Integer>> Si) {
        int pathLen = metaPath.pathLen;
        
        //label the first layer
        Set<Integer> set0 = labelList.get(0);
        for(int id:startSet) {
            set0.add(id);
        }
        
        //label the rest layers
        Set<Integer> batchSet = startSet;
        for(int index = 0;index < pathLen;index ++) {
            Set<Integer> nextLabelSet = labelList.get(index + 1);

            int targetVType = metaPath.vertex[index + 1], targetEType = metaPath.edge[index];
            if (Si.containsKey(targetVType)) {
                Set<Integer> constraintSet = Si.get(targetVType);
                Set<Integer> nextBatchSet = new HashSet<Integer>();
                for(int anchorId:batchSet) {
                    int nbArr[] = graph[anchorId];
                    for(int i = 0;i < nbArr.length;i += 2) {
                        int nbVertexID = nbArr[i], nbEdgeID = nbArr[i + 1];
                        if(constraintSet.contains(nbVertexID) && targetEType == edgeType[nbEdgeID]) {
                            if(nextLabelSet.contains(nbVertexID)) {
                                //do nothing
                            }else {
                                if(index == metaPath.pathLen - 1) {//impose restriction
                                    nextBatchSet.add(nbVertexID);
                                }else {
                                    nextBatchSet.add(nbVertexID);
                                }
                            }
                            
//                          System.out.println("BatchLinker: " + nbVertexID + " its type: " + vertexType[nbVertexID]);
                        }
                    }
                }
                
                for(int id:nextBatchSet) {
                    nextLabelSet.add(id);
                }
                
                batchSet = nextBatchSet;
           }else {
               Set<Integer> nextBatchSet = new HashSet<Integer>();
               for(int anchorId:batchSet) {
                   int nbArr[] = graph[anchorId];
                   for(int i = 0;i < nbArr.length;i += 2) {
                       int nbVertexID = nbArr[i], nbEdgeID = nbArr[i + 1];
                       if(targetVType == vertexType[nbVertexID] && targetEType == edgeType[nbEdgeID]) {
                           if(nextLabelSet.contains(nbVertexID)) {
                               //do nothing
                           }else {
                               if(index == metaPath.pathLen - 1) {//impose restriction
                                   nextBatchSet.add(nbVertexID);
                               }else {
                                   nextBatchSet.add(nbVertexID);
                               }
                           }
                           
//                         System.out.println("BatchLinker: " + nbVertexID + " its type: " + vertexType[nbVertexID]);
                       }
                   }
               }
               
               for(int id:nextBatchSet) {
                   nextLabelSet.add(id);
               }
               
               batchSet = nextBatchSet;
           }

        }
        
        return batchSet;
    }
}
