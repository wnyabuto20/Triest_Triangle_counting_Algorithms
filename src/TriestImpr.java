import java.util.*;
import java.io.*;
public class TriestImpr implements DataStreamAlgo {
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */

    // Sample size
    int sampleSize;
    
    // Record of edges and number of triangles containing them
    HashMap<HashSet<Integer>,Integer> edgeGraph = new HashMap<HashSet<Integer>,Integer>();
    
    // map of vertices and their neighbors
    HashMap<Integer,HashSet<Integer>> graphImpr = new HashMap<Integer,HashSet<Integer>>();
    
    // D
    int dvalue = 0;
    public int estimate = 0;
    // time
    public int time = 0;
    public File f;
    public FileOutputStream fos;
    public PrintWriter pw;
	public TriestImpr(int samsize) {
        sampleSize = samsize;
    }
    public int getest(){
        return estimate;
    }
    public int gettime(){
        return time;
    }
	public void handleEdge(Edge edge) {
        HashSet<Integer> newEdgeentry = new HashSet<Integer>();
        Integer u = new Integer(edge.u);
        Integer v = new Integer(edge.v);
        newEdgeentry.add(u);
        newEdgeentry.add(v);

        // Check if new edge exists already
        if (!edgeGraph.containsKey(newEdgeentry) && !u.equals(v)) {
            time++;
            // Check if both vertices exist
            HashSet<Integer> u_neighbors = new HashSet<Integer>();
            HashSet<Integer> v_neighbors = new HashSet<Integer>();           
            if (graphImpr.containsKey(u)) {
                Iterator<Integer> iterate_u = graphImpr.get(u).iterator();
                while (iterate_u.hasNext()) {
                    u_neighbors.add(iterate_u.next());
                }
            } 
            if (graphImpr.containsKey(v)) {
                Iterator<Integer> iterate_v = graphImpr.get(v).iterator();
                while (iterate_v.hasNext()) {
                    v_neighbors.add(iterate_v.next());
                }
            } 
            // Update graphImpr
            u_neighbors.add(v);
            v_neighbors.add(u);
            u_neighbors.retainAll(v_neighbors);
            int numTriangles = u_neighbors.size();
            double n_t1 = (double)(time-1) / sampleSize;
            double n_t2 = (double)(time-2) / (sampleSize-1);
            double n_t = n_t1*n_t2;
            dvalue += numTriangles*n_t;

            if (time <= sampleSize) {
                // Update edgeGraph
                edgeGraph.put(newEdgeentry, numTriangles);

                // Update nodes in graphImpr
                HashSet<Integer> u_value = new HashSet<Integer>();
                HashSet<Integer> v_value = new HashSet<Integer>();
                /**
                 * update the entry under u and v in the graph of neighbors
                 */
                if (graphImpr.containsKey(u)) {
                    u_value = graphImpr.get(u);
                    u_value.add(v); 
                    graphImpr.replace(u, u_value);
                }
                if (graphImpr.containsKey(v)) {
                    v_value = graphImpr.get(v); 
                    v_value.add(u); 
                    graphImpr.replace(v, v_value);   
                } 
                /**
                 * create new entries for u and v
                 */
                if(!graphImpr.containsKey(u)){
                    u_value.add(v); 
                    graphImpr.put(u, u_value);
                } ////
                if (!graphImpr.containsKey(v)) {
                    v_value.add(u); 
                    graphImpr.put(v, v_value);
                }
            } else {
                // Decide to remove edge or not
                double p = Math.random();
                if (p < (double)sampleSize/time) {
                    // Get random edge
                    List<HashSet<Integer>> key_array = new ArrayList<HashSet<Integer>>(edgeGraph.keySet());
                    Random r = new Random();
                    HashSet<Integer> toRemove = key_array.get(r.nextInt(key_array.size()));

                    // Remove edge from edgeGraph
                    edgeGraph.remove(toRemove);

                    // Update graphImpr
                    Iterator<Integer> iter = toRemove.iterator();
                    Integer past_u = iter.next();
                    Integer past_v = iter.next();
                    HashSet<Integer> previous_u = graphImpr.get(past_u);
                    HashSet<Integer> previous_v = graphImpr.get(past_v);
                    if (previous_u != null) {
                        previous_u.remove(past_v);
                    }
                    if (previous_v != null) {    
                        previous_v.remove(past_u);
                    }
                    graphImpr.put(past_u, previous_u);
                    graphImpr.put(past_v, previous_v);

                    // Update vertices in graphImpr
                    HashSet<Integer> u_value = new HashSet<Integer>();
                    HashSet<Integer> v_value = new HashSet<Integer>();
                    if (graphImpr.containsKey(u)) {
                        u_value = graphImpr.get(u);
                        u_value.add(v); 
                        graphImpr.replace(u, u_value);
                    }
                    if (graphImpr.containsKey(v)) {
                        v_value = graphImpr.get(v);
                        v_value.add(u); 
                        graphImpr.replace(v, v_value);     
                    } 
                    if (!graphImpr.containsKey(u)) {
                        u_value.add(v); 
                        graphImpr.put(u, u_value);
                    }
                    if (!graphImpr.containsKey(v)) {
                        v_value.add(u); 
                        graphImpr.put(v, v_value);     
                    } 
                    // Update edgeGraph
                    edgeGraph.put(newEdgeentry, numTriangles);
                }
            }

        }
    }

	public int getEstimate() {
        estimate = dvalue;
        return dvalue;
    } 
    
}