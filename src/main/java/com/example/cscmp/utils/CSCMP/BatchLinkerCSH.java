package com.example.cscmp.utils.CSCMP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BatchLinkerCSH {
	//data graph, including vertex IDs, edge IDs, and their link relationships
	private int[][] graph;
	//vertex -> type
	private int[] vertexType;
	//edge -> type
	private int[] edgeType;
	private Set<Integer> queryId = new HashSet<>();
	private MetaPath metaPath;
	private Set<Integer> keepSet;
	private Map<Integer, Set<Integer>> pnbMap;
	//label a vertex if it participates in a meta-path
	private List<Set<Integer>> labelList;
	
	public BatchLinkerCSH(int[][] graph, int[] vertexType, int[] edgeType,
						  Set<Integer> queryId, MetaPath metaPath, Set<Integer> keepSet, Map<Integer, Set<Integer>> pnbMap) {
		this.graph = graph;
		this.vertexType = vertexType;
		this.edgeType = edgeType;
		this.queryId = queryId;
		this.metaPath = metaPath;
		this.keepSet = keepSet;
		this.pnbMap = pnbMap;
		//Double.compare(1,0.0f);
		this.labelList = new ArrayList<Set<Integer>>();
		for(int i = 0;i < metaPath.pathLen + 1;i ++) {
			labelList.add(new HashSet<Integer>());
		}
	}
	
	public Set<Integer> computeCC(){
        //do 剃出queryIds不满足要求的queryId
        Set<Integer> tempDelete = new HashSet<>();
        for(int temp : queryId) {
            if(!keepSet.contains(temp)) {
				tempDelete.add(temp);
			}
        }
        if(tempDelete.size() == queryId.size()) {
            return (new HashSet<>());
        }else {
            queryId.removeAll(tempDelete);
        }
	    
		//step 1: obtain an undirected graph
		for(int id:keepSet) {
			Set<Integer> nbSet = pnbMap.get(id);
			for(int nbId:nbSet) {
				Set<Integer> tmpSet = pnbMap.get(nbId);
				if(tmpSet != null) {
					tmpSet.add(id);
				}
			}
		}
		
		//step 2: find the first set
		Set<Integer> rsSet = obtainACluster(pnbMap, queryId, keepSet);

		//step 3: return or find more linked vertices
		if(rsSet.size() == keepSet.size()) {
		    return rsSet;
		}else {
			Set<Integer> startSet = new HashSet<Integer>();
			for(int id:rsSet) {
				startSet.add(id);
			}
			
			while(startSet.size() > 0) {
				Set<Integer> nextStartSet = label(startSet);
				
				startSet = new HashSet<Integer>();
				for(int id:nextStartSet) {
					if(!rsSet.contains(id)) {
						startSet.add(id);
					}
					rsSet.add(id);
				}
			}
			
			return rsSet;
		}
	}
	
	private Set<Integer> label(Set<Integer> startSet) {
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
								if(keepSet.contains(nbVertexID)) {
									nextBatchSet.add(nbVertexID);
								}
							}else {
								nextBatchSet.add(nbVertexID);
							}
						}
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
	
	private Set<Integer> obtainACluster(Map<Integer, Set<Integer>> pnbMap, Set<Integer> ids, Set<Integer> keepSet){
		Set<Integer> visitSet = new HashSet<Integer>();
		
		Set<Integer> set = new HashSet<Integer>();
		Queue<Integer> queue = new LinkedList<Integer>();//a queue
		queue.addAll(ids);
		visitSet.addAll(ids);
		while(queue.size() > 0) {
			int curId = queue.poll();
			set.add(curId);
			
			Set<Integer> pnbSet = pnbMap.get(curId);
			for(int pnbId:pnbSet) {
				if(keepSet.contains(pnbId) && !visitSet.contains(pnbId)) {
					queue.add(pnbId);
					visitSet.add(pnbId);
				}
			}
		}
		
		return set;
	}
}
