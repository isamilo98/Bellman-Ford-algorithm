public class BellmanFord{

    
    /**
     * Utility class. Don't use.
     */
    public class BellmanFordException extends Exception{
        private static final long serialVersionUID = -4302041380938489291L;
        public BellmanFordException() {super();}
        public BellmanFordException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception class for BellmanFord algorithm
     * 
     * Use this to specify a negative cycle has been found 
     */
    public class NegativeWeightException extends BellmanFordException{
        private static final long serialVersionUID = -7144618211100573822L;
        public NegativeWeightException() {super();}
        public NegativeWeightException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception class for BellmanFord algorithm
     *
     * Use this to specify that a path does not exist
     */
    public class PathDoesNotExistException extends BellmanFordException{
        private static final long serialVersionUID = 547323414762935276L;
        public PathDoesNotExistException() { super();} 
        public PathDoesNotExistException(String message) {
            super(message);
        }
    }
    
    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    BellmanFord(WGraph g, int source) throws BellmanFordException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         *  
         *  When throwing an exception, choose an appropriate one from the ones given above
         */
        
        /* YOUR CODE GOES HERE */
     
      int V =  g.getNbNodes();//get the numbers of vertices
      distances = new int [V]; //set to the right size
      predecessors = new int [V]; //set to the right size
      this.source = source; //set the source  
      int temp_distance = 0;// create a variable that will hold the weight
      
      
      //set the distance of the source to 0 and others to infinite and also predecessors
      for(int i = 0; i < distances.length; i++) {
       if(i == source) {
         distances[source] = 0; //set the distance of the source to 0
         predecessors[i] = source;
       } else { //infinite distance for nodes other than the source
         distances[i] = Integer.MAX_VALUE;//infinite value
         predecessors[i] = -1;//reference value to say that this node has no predecessor
       }
      }
      
    for (int i = 1; i <= V-1; i++) {//we have to relax the edges V-1 times
      for (Edge e : g.getEdges()) {//iterate through each edge
        int node_0 = e.nodes[0];//get the first node
        int node_1 = e.nodes[1];//get the second node
        temp_distance = distances[node_0] + e.weight; // update the temporary distance 

        //compare the temp distance to the distance of the second node, if its lower then we relax
        if (distances[node_0] != Integer.MAX_VALUE && temp_distance < distances[node_1]) {
          distances[node_1] = temp_distance;//relax the edge
          predecessors[node_1] = node_0;//set the predecessor of the node
        }
      }
    }
    
    //to check if there is a negative cycle
    for(Edge e : g.getEdges()) {
      int u = e.nodes[0];//get the first node
      int v = e.nodes[1];//get the second node
      
      //if the value change, then there is a negative cycle
      if(distances [u] != Integer.MAX_VALUE && distances[u] + e.weight < distances[v]) {
        throw new NegativeWeightException();//return the exception
      }
    }  
           
    }

    public int[] shortestPath(int destination) throws BellmanFordException{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Exception is thrown
         * Choose appropriate Exception from the ones given 
         */

        /* YOUR CODE GOES HERE (update the return statement as well!) */
      
      int[] list = new int[distances.length];//create an array to hold the predecessors
      
  
      int counter = 0;//counter to know the exact lenght of the list array
      list[counter] = destination;//add first the destination in the list
      counter++; //increment the counter
      int current_node = predecessors[destination]; //current node keep track of the predecessor of the node
      
      
      if(current_node == destination) {//if the destination is also the source
        int[] array = new int[1];//create an array with the size of list
        array[0] = destination;//put the node in the array
        return array;//return the array
      }
      
      if(current_node == source) {//if the predecessor of the destination is the source
        list[counter] = current_node;
        counter++;
      }
      
      while(current_node != source) {//execute until the current node is equal to the source
        
        if(current_node == -1) {//if the predessors of the node is -1
          throw new PathDoesNotExistException();//means that the path doesn't exist
        }
        
        list[counter] = current_node;//add the current node to the list
        counter++;//update the counter
        current_node = predecessors[current_node];//update current node with the predecessor of the previous one
        
        if(current_node == source) {//if the current node is equal to the source
          list[counter] = current_node;
          counter++;//update the counter
          break;//exit the while loop
        }
                
      }
      
     
      int [] shortest_path = new int [counter];//create a new array to return the solution
      int counter2 = 0;//counter to use like index for the new array created
      //iterate through the list in reverse order to put in the right order
      for(int i = counter-1; i >= 0; i--) { 
        shortest_path[counter2] = list[i];
        counter2++;//increment the counter
      }
          
      return shortest_path;//retun the shortest path array
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

      String file = args[0];
      WGraph g = new WGraph(file);
      try{
          BellmanFord bf = new BellmanFord(g, g.getSource());
          bf.printPath(g.getDestination());
      }
      catch (BellmanFordException e){
          System.out.println(e);
      }

 }  
}
