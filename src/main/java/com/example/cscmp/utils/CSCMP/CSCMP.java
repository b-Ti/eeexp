package com.example.cscmp.utils.CSCMP;


import java.util.*;

public class CSCMP {
    //data graph, including vertice IDs, edge IDs, and their link relationships
    private int[][] graph;
    //vertex -> type
    private int[] vertexType;
    //edge -> type
    private int[] edgeType;
    private int queryK = -1;
    private MetaPath queryMPath = null;

    public CSCMP(int[][] graph, int[] vertexType, int[] edgeType) {
        this.graph = graph;
        this.vertexType = vertexType;
        this.edgeType = edgeType;
    }

    public Set<Integer> query(int queryId, MetaPath queryMPath,
                              int queryK, Set<Integer> Sc) {
        this.queryK = queryK;
        Map<Integer, Set<Integer>> Si = getVertexTypeMap(Sc);

        BatchLinker batchLinker = new BatchLinker(graph, vertexType, edgeType);
        Set<Integer> queryIds;

//        System.out.println("vertex:" + queryMPath.vertex[0] + " " + queryMPath.vertex[1] + " : " + queryMPath.vertex.length);
        if(!isSymmetric(queryMPath)) {
            queryIds = batchLinker.link(queryId, queryMPath, Si);
            queryIds.remove(queryId);
            GetSchema gSchema = new GetSchema();
            GetAllMetaPath getAllMetaPath = new GetAllMetaPath(gSchema.getSchema());
            this.queryMPath = getAllMetaPath.getMetaPath(queryMPath);
        }else {
            queryIds = new HashSet<>();
            queryIds.add(queryId);
//            System.out.println("queryId:" + queryId);
            this.queryMPath = queryMPath;
        }



        //step 1: compute the connected subgraph via batch-search with labeling (BSL)
    Set<Integer> keepSet = batchLinker.link(queryIds, this.queryMPath, Si);
//        System.out.println(keepSet);
        //step 2: initialization
        Map<Integer, Set<Integer>> pnbMap = new HashMap<>();//a vertex -> its pnbs
        Map<Integer, List<Set<Integer>>> visitMap = new HashMap<>();//a vertex -> its visited vertices
//        System.out.println(pnbMap);
//        System.out.println(visitMap);
        //step 3: find k-neighbors for each vertex
        for(int startVertex:keepSet) {
            List<Set<Integer>> visitList = new ArrayList<>();
            for(int i = 0;i <= this.queryMPath.pathLen;i ++) {
                visitList.add(new HashSet<>());
            }
            Set<Integer> nbSet = new HashSet<>();
            //find the first k neighbors
            findFirstKNeighbors(startVertex, startVertex, 0, visitList, nbSet, keepSet);
            pnbMap.put(startVertex, nbSet);
            visitMap.put(startVertex, visitList);
        }

        //step 4: compute the k-core
        //do 剃出queryIds不满足要求的queryId
        Set<Integer> tempDelete = new HashSet<>();
//        System.out.println("queryIds.size:" + queryIds.size() + " " + queryIds.toString());
        for(int temp : queryIds) {
            if(pnbMap.get(temp).size() < queryK) {
                tempDelete.add(temp);
            }
        }
        if(tempDelete.size() == queryIds.size()) {
//            System.out.println("FastBCore_AC.query() -> tempDelete.size() == queryIds.size()");
            return (new HashSet<>());
        }else {
            queryIds.removeAll(tempDelete);
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> deleteSet = new HashSet<>();//mark the delete vertices
        for(Map.Entry<Integer, Set<Integer>> entry:pnbMap.entrySet()) {
            if(entry.getValue().size() < queryK) {
                queue.add(entry.getKey());
                deleteSet.add(entry.getKey());
            }
        }
        while(queue.size() > 0) {//iteratively delete vertices whose degrees are less than k
            int curId = queue.poll();
            keepSet.remove(curId);

            Set<Integer> pnbSet = pnbMap.get(curId);
            for(int pnbId:pnbSet) {
                if(!deleteSet.contains(pnbId)) {
                    Set<Integer> tmpSet = pnbMap.get(pnbId);
                    tmpSet.remove(curId);
                    if(tmpSet.size() < queryK) {
                        addMoreNeighbors(pnbId, pnbId, 0, visitMap.get(pnbId), tmpSet, keepSet);
                        if(tmpSet.size() < queryK) {
                            queue.add(pnbId);
                            deleteSet.add(pnbId);
                        }
                    }
                }
            }
        }


        //step 5: find the connected community
        BatchLinkerCSH ccFinder = new BatchLinkerCSH(graph, vertexType, edgeType, queryIds, this.queryMPath, keepSet, pnbMap);
        return ccFinder.computeCC();
    }

    //add keepSet`s constraint
    private void findFirstKNeighbors(int startID, int curId, int index, List<Set<Integer>> visitList, Set<Integer> pnbSet, Set<Integer> qSet) {
        int targetVType = queryMPath.vertex[index + 1], targetEType = queryMPath.edge[index];

        int[] nbArr = graph[curId];
        for(int i = 0;i < nbArr.length;i += 2) {
            int nbVertexID = nbArr[i], nbEdgeID = nbArr[i + 1];
            Set<Integer> visitSet = visitList.get(index + 1);
            if(targetVType == vertexType[nbVertexID] && targetEType == edgeType[nbEdgeID] && !visitSet.contains(nbVertexID)) {
                if(index + 1 < queryMPath.pathLen) {
                    findFirstKNeighbors(startID, nbVertexID, index + 1, visitList, pnbSet, qSet);
                    if(pnbSet.size() >= queryK) {
                        return ;//we have found k meta-paths
                    }
                    visitSet.add(nbVertexID);
                }else {//a meta-path has been found
                    if((nbVertexID != startID) && qSet.contains(nbVertexID)) {
                        pnbSet.add(nbVertexID);
                    }
                    visitSet.add(nbVertexID);
                    if(pnbSet.size() >= queryK) {
                        return ;//we have found k meta-paths
                    }
                }
            }
        }
    }

    private void addMoreNeighbors(int startID, int curId, int index, List<Set<Integer>> visitList, Set<Integer> pnbSet, Set<Integer> keepSet) {
        int targetVType = queryMPath.vertex[index + 1], targetEType = queryMPath.edge[index];

        int[] nbArr = graph[curId];
        for(int i = 0;i < nbArr.length;i += 2) {
            int nbVertexID = nbArr[i], nbEdgeID = nbArr[i + 1];
            Set<Integer> visitSet = visitList.get(index + 1);
            if(!visitSet.contains(nbVertexID) && targetVType == vertexType[nbVertexID] && targetEType == edgeType[nbEdgeID]) {
                if(index + 1 < queryMPath.pathLen) {
                    addMoreNeighbors(startID, nbVertexID, index + 1, visitList, pnbSet, keepSet);
                    if(pnbSet.size() >= queryK) {
                        return ;//we have found k meta-paths
                    }
                    visitSet.add(nbVertexID);
                }else {//a meta-path has been found
                    if(keepSet.contains(nbVertexID)) {//restrict it to be in keepSet
                        if(nbVertexID != startID) {
                            pnbSet.add(nbVertexID);
                            visitSet.add(nbVertexID);
                            if(pnbSet.size() >= queryK) {
                                return ;//we have found k meta-paths
                            }
                        }
                    }
                }
                visitSet.add(nbVertexID);//mark this vertex (and its branches) as visited
            }
        }
    }

    private boolean isSymmetric(MetaPath queryMPath) {
        int[] vertex = queryMPath.vertex;
        int length= queryMPath.pathLen;
        if((length%2) != 0) {
            return false;
        }

        for(int i = 0; i < length/2; i++) {
            if (vertex[i] != vertex[length - i]) {
                return false;
            }
        }

        return true;
    }

    //得到queryId的一跳邻居
    public Set<Integer> getOneWalkPNB(int queryId, MetaPath metaPath) {
        int pathLen = metaPath.pathLen;

        //label the rest layers
        Set<Integer> batchSet = new HashSet<>();
        batchSet.add(queryId);
        for(int index = 0;index < pathLen;index ++) {
            int targetVType = metaPath.vertex[index + 1], targetEType = metaPath.edge[index];
//            int targetVType = vertex[index + 1], targetEType = edge[index];
            Set<Integer> nextBatchSet = new HashSet<>();
            for(int anchorId:batchSet) {
                int[] nbArr = graph[anchorId];
                for(int i = 0;i < nbArr.length;i += 2) {
                    int nbVertexID = nbArr[i], nbEdgeID = nbArr[i + 1];
                    if(targetVType == vertexType[nbVertexID] && targetEType == edgeType[nbEdgeID]) {
                        nextBatchSet.add(nbVertexID);
                    }
                }
            }
            batchSet = nextBatchSet;
        }
        batchSet.remove(queryId);
        return batchSet;
    }

    public Map<Integer, Set<Integer>> getVertexTypeMap(Set<Integer> Sc){
        Map<Integer, Set<Integer>> Si = new HashMap<>();
        // 约束节点集类型映射转换vertexType->Sc
        for(int temp : Sc) {
            int index = vertexType[temp];
            if (Si.containsKey(index)) {
                Set<Integer> tempSet = Si.get(index);
                tempSet.add(temp);
            }else {
                Set<Integer> tempSet = new HashSet<>();
                tempSet.add(temp);
                Si.put(index, tempSet);
            }
        }
        return Si;
    }
}

