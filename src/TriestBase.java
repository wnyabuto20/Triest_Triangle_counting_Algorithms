import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;
import java.io.*;
public class TriestBase implements DataStreamAlgo {
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */
    int samSize;
    int triangleCount = 0;
    public int time = 0;
    public int estimate = 0;
    HashMap<Integer, HashSet<Integer>> graph; //uses a vertex as key and a set of its neighbors as the value
    HashMap <Edge, Integer> edgeWeight; //stores each edge and its weight
    ArrayList<Edge> edges;
       
	public TriestBase(int samsize) {
        samSize = samsize;
        graph = new HashMap<Integer, HashSet<Integer>>();
        edgeWeight = new HashMap <Edge, Integer>();
        edges = new ArrayList<Edge>();
    }
    public int getest(){
        return estimate;
    }
    public int gettime(){
        return time;
    }
	public void handleEdge(Edge edge) {
        Integer key1 = new Integer(edge.u);
        Integer key2 = new Integer(edge.v);
        edges.add(edge);
        if(time <= samSize){ //add the endpoints of this edge to the graph
            if(graph.containsKey(key1)){ //if we already have this endpoint
                graph.get(key1).add(key2);
            }
            if(!graph.containsKey(key1)){
            HashSet<Integer> eArr = new HashSet<Integer>();
            eArr.add(key2);
            graph.put(key1, eArr);
            }
            if(graph.containsKey(key2)){
                graph.get(key2).add(key1);
            }
            if(!graph.containsKey(key2)){
                HashSet<Integer> eArr1 = new HashSet<Integer>();
                eArr1.add(key1);
                graph.put(key2, eArr1);
                }
            counter1(edge);  
        }
        else{
            counter2(edge);
        }

    }
    //keeps track of the triangle count resulting from the edge and adds it to the total count of triangles .
    public void counter1(Edge e){
        int u = e.u;
        int v = e.v;
        Integer weight = 0;
        for (Integer var : graph.keySet()) {
            if(graph.get(var).contains(u) && graph.get(var).contains(v)){
                triangleCount++;
                weight++;
            }
        }
        if(edgeWeight.containsKey(e)){
            Integer inc = edgeWeight.get(e) + weight;
            edgeWeight.replace(e, inc);
        }
        if(!edgeWeight.containsKey(e)){
            edgeWeight.put(e, weight);
        }

       /* for(int i = 0;i<graph.get(e.u).size();i++){
            int v1 = graph.get(e.u).get(i).v;
            int v2 = e.v;
            String newEdge = v1 + " " + v2;
            Edge toFind = new Edge(newEdge);
            if(graph.get(v1).contains(toFind)){
                triangleCount++;
                if(edgeWeight.containsKey(toFind)){
                    Integer inc = new Integer(edgeWeight.get(toFind).intValue() + 1);
                    edgeWeight.replace(toFind, inc);
                }
                if(!edgeWeight.containsKey(toFind)){
                    Integer nput = new Integer(1);
                    edgeWeight.put(toFind, nput);
                }
                if(edgeWeight.containsKey(graph.get(e.u).get(i))){
                    Integer inc1 = new Integer(edgeWeight.get(graph.get(e.u).get(i)).intValue() + 1);
                    edgeWeight.replace(graph.get(e.u).get(i), inc1);
                }
                if(!edgeWeight.containsKey(graph.get(e.u).get(i))){
                    Integer nput1 = new Integer(1);
                    edgeWeight.put(graph.get(e.u).get(i), nput1);
                }
                if(edgeWeight.containsKey(e)){
                    Integer inc2 = new Integer(edgeWeight.get(e).intValue() + 1);
                    edgeWeight.replace(e, inc2);
                }
                if(!edgeWeight.containsKey(e)){
                    Integer nput2 = new Integer(1);
                    edgeWeight.put(e, nput2);
                }
            }
        }*/
        time++;

    }
    public void counter2(Edge e){
        randomReplace(e);
    }
    public void randomReplace(Edge addMe){
        Random rand = new Random();
        int bias = rand.nextInt(time) + 1;
        if(bias<=samSize){
            int index = rand.nextInt(edges.size());
            Edge toRemove = edges.get(index);
            if(graph.containsKey(toRemove.u)){//
                if(graph.get(toRemove.u).contains(toRemove.v)){
                    graph.get(toRemove.u).remove(toRemove.v);
                }
            }
            if(graph.containsKey(toRemove.v)){
                if(graph.get(toRemove.v).contains(toRemove.u)){
                graph.get(toRemove.v).remove(toRemove.u);
                }
            }
            if(edgeWeight.containsKey(toRemove)){
            triangleCount = triangleCount - edgeWeight.get(toRemove);
            }
            counter1(addMe);
        }
        else{
            time++;
        }
    }
    public double factorials(double n){
        int ret = 1;
        for(int i =1;i<(int)n;i++){
            ret = ret*i;
        }
        return (double)ret;
    }
    public int getEstimate() { // You shouldn't return 0 ;-) returns triangle count
    if(time >= samSize){
        double num = factorials((double)(time - 2));
        double denom1 = factorials((double)(samSize-2));
        double denom2 = factorials ((double)(((time-3)-(samSize-3))+1));
        double numerator = num/(denom1*denom2);
        double num2 = factorials((double)(time+1));
        double denom21 = factorials((double)(samSize+1));
        double denom22 = factorials((double)((time-samSize)+1));
        double denominator = num2/(denom21*denom22);
        double pi_t = numerator/denominator;
        estimate = (int)(((double)triangleCount)/pi_t);
        return (int)(((double)triangleCount)/pi_t);
    }
    estimate = triangleCount;
    return triangleCount; 
    }
    
}
