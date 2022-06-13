package com.example.cscmp.utils.CSCMP;

import java.util.List;
import java.util.Map;

/**
 * @author cscmp
 */
public class GetAllMetaPath {
    
    private List<Map<Integer, Integer>> schema;
    
    public GetAllMetaPath(List<Map<Integer, Integer>> schema){
        this.schema = schema;
    }

    public MetaPath getMetaPath(MetaPath oldPath) {
        int[] oldVertex = oldPath.vertex;
        int oldLen = oldPath.pathLen;
        int[] newVertex ;
        int[] newEdge;

        int i = 0, j = 0;
        boolean flog = false;
        
        int[] temps = getSame(oldVertex, oldLen);
        i = temps[0];
        j = temps[1];
        if(temps[2] == 1) {
            flog = true;
        }
        //APTP
        if (i == oldLen && flog) {
            newVertex = new int[(i - j + 1)];
            for (int k = 0; k < newVertex.length; k++) {
                newVertex[k] = oldVertex[j + k];
            }

            newEdge = new int[(i - j)];
            for (int k = 0; k < newEdge.length; k++) {
                newEdge[k] = schema.get(newVertex[k]).get(newVertex[k + 1]);
            }
            
        }
        //APTP
        else if (flog) {
            int middle = (i + j) / 2;
            int newLen = (oldLen - middle) * 2;

//            System.out.println("newLen:" + newLen);
            newVertex = new int[(newLen + 1)];
            newEdge = new int[newLen];

            int temp = 0;
            for (int k = newLen - oldLen; k <= newLen; k++) {
                newVertex[k] = oldVertex[temp];
                temp++;
            }
            for (int k = 0; k < (oldLen - i); k++) {
                newVertex[k] = oldVertex[oldLen - k];
            }
            for (int k = 0; k < newEdge.length; k++) {
                newEdge[k] = schema.get(newVertex[k]).get(newVertex[k + 1]);
            }


        }else {//APT,AP
            newVertex = new int[(oldLen * 2 + 1)];
            newEdge = new int[oldLen * 2];
//            System.out.println("newVertex:" + newVertex.length);
            
            int temp = 0;
            for(int k = 0; k < oldLen; k++) {
                newVertex[temp] = oldVertex[oldLen - k];
                temp++;
            }
            for (int k = 0; k <= oldLen; k++) {
                newVertex[temp] = oldVertex[k];
                temp++;
            }
            for (int k = 0; k < newEdge.length; k++) {
                newEdge[k] = schema.get(newVertex[k]).get(newVertex[k + 1]);
            }
            
        }

        return new MetaPath(newVertex, newEdge);
    }
    
    private int[] getSame(int[] oldVertex, int oldLen) {
        int[] reslut = new int[3];
        
        for (int i = oldLen; i > 0; i--) {
            for (int j = i - 1; j > -1; j--) {
//                System.out.println("i:" + i + "  j:" + j);
                if (oldVertex[i] == oldVertex[j]) {
                    reslut[0] = i;
                    reslut[1] = j;
                    reslut[2] = 1;
                    return reslut;
                }
            }
        }
        reslut[2] = -1;
        return reslut;
    }
    
}
