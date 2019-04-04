package mst;

import java.util.*;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import mst.Mstgraph.Kredge;

import java.io.*;

public class Mstgraph {
	
	int Verti, KEdges; // initialization to store the number of vertices and edges.
	Kredge edge[]; // initialization to store array of edge  objects.
	
	// class for union find data structure.
    class unionf
    {
        int parent, rank;  // declaration of parent and rank variables.
    }; // end of union find class
    
    // class for storing edge parameters of kruskal's algorithm
    class Kredge
    {
		String alphasource,alphadestination;  // managing the alphabets of the vertices
        int source, destination;  // Edge with source and destination
        int weight;     // Weight of the edges
 
    };
    
    Mstgraph(int v, int e)   // mst graph
    {
    	Verti = v;  //no of vertices.
    	KEdges = e; // no of edges.
        edge = new Kredge[KEdges];  // creating edge object.
        for (int i=0; i<e; ++i)   // e represent the no of edges.
            edge[i] = new Kredge();  // iterating over list of edges and creating edge object for all edges.
    }
    
    // find operation to check whether two components are in the same connected component or not
    int find(unionf findsubsets[], int j)
    {
    	//By path compression
        if (findsubsets[j].parent != j)   
        	findsubsets[j].parent = find(findsubsets, findsubsets[j].parent);
        return findsubsets[j].parent;
    }  // end of find operation
 
    // Union function which takes two subsets and merges them into one using union by rank concept.
    void Union(unionf unionsubsets[], int l, int m)
    {
        int rootofl = find(unionsubsets, l);  // find operation on first component
        int rootofm = find(unionsubsets, m);  // find operation on second component
 
        // Below condition takes care of attaching one tree of smaller height to larger height.
        if (unionsubsets[rootofl].rank < unionsubsets[rootofm].rank)  
        	unionsubsets[rootofl].parent = rootofm;
        else if (unionsubsets[rootofl].rank > unionsubsets[rootofm].rank)
        	unionsubsets[rootofm].parent = rootofl;
        // if the rank of both the roots are same then we can make either one as parent and other as root.
        else
        {
        	unionsubsets[rootofm].parent = rootofl;
        	unionsubsets[rootofl].rank++;
        }
    } // end of union operation
    
    
    // 
    void MstKruskalsFunc()  //start of the mst kruskal's algorithm 
    {
    	Kredge kruskout[] = new Kredge[Verti];  // kruskout variable to store the output of the algorithm.
        int e = 0;  // variable e is used for output of the algorithm result.(initialization)
        int i = 0;  // variable i is used for the started edges.(initialization)
        for (i=0; i<Verti; ++i)  // iterate over list of vertices.
        	kruskout[i] = new Kredge();  // edge object creation.
        @SuppressWarnings("unused")
        Kredge[] sortedge;   // initialize sortedge weight variable of kredge object array type.
        Sortweight sw = new Sortweight();  // class to sort edges by weight. and its initialization.
        sortedge=sw.sort(edge);  // calling sort function of the sortweight class.
        // now the sortedge varible has list of sorted edges by weight.
        
        unionf connectedcomp[] = new unionf[Verti];  // creation of connected components equal to the size of the vertices.
        for(i=0; i<Verti; ++i)   // iterating over list of vertices.
        	connectedcomp[i]=new unionf();  // unionf object creation containing parent and rank variables.
 
        // creating subsets equal to no of vertices with one element.
        for (int v = 0; v < Verti; ++v) // iterating over list of vertices.
        {
        	connectedcomp[v].parent = v;  // setting the parent variable.
        	connectedcomp[v].rank = 0;  // setting the rank variable.
        }
 
        i = 0; // variable to iterate over the edges.
        // no of edges will be no of vertices minus 1 property of the spanning tree.
        while (e < Verti - 1)  // iterate over no of vertices minus one.
        {
          
        	Kredge consecutiveedgesbyweightsorted = new Kredge();
        	consecutiveedgesbyweightsorted = edge[i++]; // increment over next edge in the edge list 
 
            int s1 = find(connectedcomp, consecutiveedgesbyweightsorted.source); // find the set containing the source.
            int s2 = find(connectedcomp, consecutiveedgesbyweightsorted.destination); //find the set containing connected component of the destination.
            if (s1 != s2)  // if both the sets are different then it dosen't form a cycle so we can do union operation.
            {
            	kruskout[e++] = consecutiveedgesbyweightsorted;
                Union(connectedcomp, s1, s2);  // doing union of s1 and s2 sets.9connected components).
            }
            // if they belong to the same connected component then move to the next iteration discarding the edge.
        }
        PrintWriter out; // initializing print writer to output the result to kruskal's out file.
		try {
			out = new PrintWriter(new FileWriter("./kruskal.out")); // creating of print writer and file writer class to output to the kruskal's file.
			out.println(Verti-1); // no of outputs will be equal to the no of vertices-1 as per the spanning property of trees.
		        for (i = 0; i < e; ++i)  // iterating over the no of algorithm found edges.
		            out.println("("+kruskout[i].alphasource+"," + 
		            		kruskout[i].alphadestination+")=" + kruskout[i].weight);
		    out.close();
		    // closing the reference object to print writer class.
		} catch (IOException pe) {
			// if the file is not found or any exception occurs the below line displays the appropriate error message.
			pe.printStackTrace();
		}
       
    }
    
    // returns the set containing the algorithm prim's picking edge list.
    Set<Entry<String, String>> prim(Primgraph g,String root_pi) {
    	// map collection to store the root of the vertices of the graph.
        Map<String, String> Rootvar = new HashMap<String, String>();
        // map collection set to store the key of all the vertices of the graph.
        Map<String, Integer> keyvar = new HashMap<String, Integer>();
        initialcondition(g, Rootvar, keyvar); // to set the root to null and key to infinity initially.
        keyvar.put(root_pi, 0); // set only one of the first vertex (source) key to zero to proceed with the algorithm.
        // initializing primoutmapset variable of type linked hashmap to store the list of output edges from prim algo.
        Map<String, String> primoutmapset = new LinkedHashMap<String, String>();
        // variable initialization Que of type hashset to hold all the vertices that are not in the primoutmapset variable.
        Set<String> Que = new HashSet<String>(g.graph.keySet()); // this gets rid of considering cycles in the graph.
        // continue to loop until the Que is not empty...
        while (!Que.isEmpty()) {
        	//finding minimum  edge from the Que.
            String u = findminlablevertex(Que, keyvar);
            // remove the minimum edge from the queue.
            Que.remove(u); // avoid cycles.
            // if the root of the minimum vertex is not equal to null then add this edge corresponding to vertex to the output of prim set.
            if (Rootvar.get(u) != null) {
            	primoutmapset.put(u, Rootvar.get(u)); // add edge and its details to the output set.
            }
            // Initializing vertex of Prim vertex class type and setting it with the minimum vertex corresponding to minimum edge.
            Primvertex vertex = g.graph.get(u);
             Set<Entry<Primvertex, Integer>> adjvertexset =
                    vertex.neighbours.entrySet(); // this is used to get the list of adjacent vertices of the vertex u to w .
            for (Entry<Primvertex, Integer> k : adjvertexset) {
            	//if due to the inclusion of the new edge to the primoutputset we need to update the labels of the neighbours.
                if (Que.contains(k.getKey().name)  // check if the que contains the vertex or not
                    && k.getValue() < keyvar.get(k.getKey().name)) { // new label is less than the old label then update.
                    keyvar.put(k.getKey().name, k.getValue()); //update  the key.
                    Rootvar.put(k.getKey().name, u); //update the root of the vertex.
                }
            }

        }  // end of the while loop.
        return primoutmapset.entrySet();  // return the set of edges from the prim algorithm.

    }
    //initial condition of initializing root and key of vertices of graph.
 	private void initialcondition(Primgraph gr, Map<String, String> Rootvar,
            Map<String, Integer> keyvar) { // set the vertex root to null and key of vertex to infinity to proceed.
        for (String a : gr.graph.keySet()) { // iterate over the vertex set of the graph.
        	Rootvar.put(a, null);  //set the root to null.
        	keyvar.put(a, Integer.MAX_VALUE); // set the key of the vertices to infinity which is max value of integer.
        }
    }// end of initial conditions ..
 	
 	//function to find the minimum vertex lable.
 	private String findminlablevertex(Set<String> que,
             Map<String, Integer> keyvar) { // function of finding minimum vertex starts...
        Set<Entry<String, Integer>> fs = keyvar.entrySet();
        String minlablevertex = ""; // minlablevertex variable declaration.
        int minvar = Integer.MAX_VALUE; // minvar variable initialization with max value of integer.
        for (Entry<String, Integer> l : fs) { // iterating over the entry set of the keys to get the min vertex.
            if (que.contains(l.getKey()) && l.getValue() < minvar) { // condition check does the que contains the vertex and is the value less than the min var value.
            	minvar = l.getValue(); // if the vertex value is less then update the minvar variable value.
            	minlablevertex = l.getKey(); // minlablevertex variable  is assigned with the vertex of minvariable.
            }
        } // end of the for loop
        return minlablevertex;  // return the minimum vertex
    } // end of function....

	public static void main(String[] args) throws Exception { // main function
			try {
				String filepath=""; //initializing filepath variable to store the path
                JFileChooser chooser = new JFileChooser(); // java effects file chooser class declaration
                chooser.setCurrentDirectory(new java.io.File(".")); // set the current directory
                chooser.setDialogTitle("Algo_Project_Dialogbox"); // set the name of the dialog box to open
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // setting the file selection mode as a file.
                chooser.setAcceptAllFileFilterUsed(true); //set acceptallfilefilterused as true.
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { // condition to tell whether file is present or not.
                  filepath = chooser.getSelectedFile().toString(); // save the selected path of the selected file
                } else {
                  System.out.println("No Selection made..... Something went wrong... "); // error message if something goes wrong.
                }
                FileReader file = new FileReader(filepath); // Initializing file reader to get the
		        @SuppressWarnings("resource")
				BufferedReader readinputfile = new BufferedReader(file); //Initializing buffer reader class.
		        String linebyline = readinputfile.readLine(); // get the first line.
		        ArrayList<String> storelinebyline = new ArrayList<>(); //initialize an array of list to sore line by line of the input file.
		        String[] containsvertex; // initialize string array to contain the list of vertices.
		        while(linebyline != null) { // read till the end of the file line by line.
		        	storelinebyline.add(linebyline); // add to the array list variable.
		        	linebyline = readinputfile.readLine(); // go to the next line and iterate.
		        }
		        int n= Integer.parseInt(storelinebyline.get(0)); //Initialize variable n to get the no of vertices.
		        String verti = storelinebyline.get(1); //read the second line to get the vertices names.
		        containsvertex = verti.split(","); // split the vertices into the string array seperated by commas.
		        HashMap<String, Integer> vertmap = new HashMap<>(); // Initialize vertmap hash to index the vertexes.
		        for(int i=0;i<containsvertex.length;i++) { // run over the vertexes and update in the map.
		        	vertmap.put(containsvertex[i], i); // update the key and the value pairs.
		        }
		        int m= Integer.parseInt(storelinebyline.get(2));// initialize the variable m with the number of edges.
		        Mstgraph graph = new Mstgraph(n, m); // setup the mst graph with the no of edges and no of vertices.
		        Mstgraph duplicate = new Mstgraph(n, m);
		        for(int i=0;i<m;i++) { // iterate over the no of edges.
		        	String regexdo = storelinebyline.get(i+3).replace("(", "").replace(")=", ","); // regex work to get src,dest,weight.
		        	String[] srcdestandweight=regexdo.split(","); //split by comma and put in string array.
		        	duplicate.edge[i].source = graph.edge[i].source = vertmap.get(srcdestandweight[0]); //put the source index
		        	duplicate.edge[i].destination = graph.edge[i].destination = vertmap.get(srcdestandweight[1]); //put the destination index
		        	duplicate.edge[i].weight = graph.edge[i].weight = Integer.parseInt(srcdestandweight[2]); //put the weight of the edges.
		        	duplicate.edge[i].alphasource = graph.edge[i].alphasource = srcdestandweight[0]; //put the source variable
		        	duplicate.edge[i].alphadestination = graph.edge[i].alphadestination =srcdestandweight[1];  //put the destination variable.
		        } // end of the for loop..
		       graph.MstKruskalsFunc(); // compute the kruskal's algorithm and display the output to the kruskal.out file.
		       ArrayList<Primedge> listedges = new ArrayList<>(); //create an array list with name edges.
		       for(int i=0;i<m;i++) { // iterate over list of edges.
		    	   String regexdo = storelinebyline.get(i+3).replace("(", "").replace(")=", ","); // regex work to get src,dest,weight.
		        	String[] srcdestandweight=regexdo.split(","); //split by comma and put in string array.
		        	listedges.add(new Primedge(srcdestandweight[0], srcdestandweight[1], Integer.parseInt(srcdestandweight[2]))); // add these information of the edges to prim edge array list.
		       }// end of the for loop.
	        Primgraph primgraph = new Primgraph(listedges); // create a prim graph with the list of edges information.
	        PrintWriter primout; //print writer type variable initialization.
	        try {
			    primout = new PrintWriter(new FileWriter("./prim.out"));  // print writer to output the result to prim.out file.
			    primout.println(n-1); // display the no of edges in the prim output.
			    Set<Entry<String, String>> output = graph.prim(primgraph,duplicate.edge[0].alphasource); //calling prim function with the primgraph and the root as the first source edge in the edge set.
			        for (Entry<String, String> entry : output) { // loop over the prim output edges for display.
			        	for(Primedge i:listedges) { // loop over the list of edges.
			        		if((i.source.equalsIgnoreCase(entry.getValue()) && i.destination.equalsIgnoreCase(entry.getKey())) || (i.destination.equalsIgnoreCase(entry.getValue()) && i.source.equalsIgnoreCase(entry.getKey()))) // if the edges matches display the edges with the weight.
			        			primout.println("("+entry.getValue()+"," + 
			        					 entry.getKey()+")=" +i.weight);   // display prim output
			        	}
			        }
			        primout.close(); // close the print writer.
		        } // closing of the try block.
		        catch (IOException pst) { //catch block to catch the error.
					pst.printStackTrace();// display error in case of any error while writing.
				}
		}
		catch (Exception e) { //catch block to catch the error.
			System.out.println(e); // print the error..
			System.out.println("Something went wrong.Provide proper input to generate output"); // user error message.
		}

	}

}

// start of the prim vertex class.
class Primvertex { // prim vertex class to hold the name of the vertex use full for display purposes.
    String name; // initializing a variable called as name to hold the name of the variable.
    public Map<Primvertex, Integer> neighbours =new HashMap<Primvertex, Integer>(); // Hash map to hold the neighboring edges of the vertex.
    public Primvertex(String name) { // constructor of the prim vertex to initialize the name of the vertex.
        this.name = name; // write the name of vertex to current object.
    }
}// end of the primvertexclass.

//start of the prim edge class
class Primedge {
    String source, destination; // initializing source and destination variables to hold the edge info.
    int weight = Integer.MAX_VALUE; // Initially the weight is initialized to max int.
    public Primedge(String src, String dest,int weight) { // constructor of the prim edge class.
        this.source = src; // current source object initialization.
        this.destination = dest; // current destination object initialization.
        this.weight = weight; // current weight object initialization.
    }
}// end of the primedgeclass.

//start of primgraph class.
class Primgraph {
    Map<String, Primvertex> graph; // initializing map collection 
    public Primgraph(ArrayList<Primedge> edges) { // primgraph constructor 
        this.graph = new HashMap<String, Primvertex>(edges.size()); // initialize the graph current object variable with hashmap of length of edges.
        // iterate over the list of edges.
        for (Primedge ed : edges) { // start of the for loop.
            if (!this.graph.containsKey(ed.source)) // if the graph dosen't contain the source then add it to the graph.
                this.graph.put(ed.source, new Primvertex(ed.source));
            if (!this.graph.containsKey(ed.destination)) // if the graph dosen't contain the destination then add it to the graph.
                this.graph.put(ed.destination, new Primvertex(ed.destination));
        }// end of the for loop.
        for (Primedge el : edges) { // start of the for loop.
            this.graph.get(el.source).neighbours.put(this.graph.get(el.destination), el.weight); // for all edges of source vertex put all the neighbors for the source vertex along with the weight of the edge.
            this.graph.get(el.destination).neighbours.put(this.graph.get(el.source), el.weight); // for all edges of destination vertex put all the neighbors for the destination vertex along with the weight of the edge.
        }// end of the for loop...
    }// end of the class constructor...
}// end of the primgraphedge class....

class Sortweight{  // sortedgeweight class to sort the edges using heap sort....
	 public Kredge[] sort(Kredge[] array) // sort function of the sortweight class to sort the edges.
	    {
	        int k = array.length; // length of the array edges....
	        // Build heap from the array of edges...
	        for (int i = k / 2 - 1; i >= 0; i--)
	            heapify(array, k, i); // do heapify to construct heap structure.
	        // extract the elements from the heap one by one.
	        for (int j=k-1; j>=0; j--)// j variable keeps on decreasing.
	        {
	        	Kredge temp = array[0]; // swap process.
	        	array[0]=array[j];
	        	array[j]=temp;
	            heapify(array, j, 0); // doing heapify on the reduced set
	        }
			return array; //return the sorted edge list.
	        
	    }// end of the sort function.
	 
	 void heapify(Kredge[] array, int n, int i) // hapify function....
	    {
	        int lr = i;  // Initialize lr as the root
	        int left = 2*i + 1;  // lest subtree
	        int right = 2*i + 2;  // right sub tree
	        // If left child is larger than root the do the if statement
	        if (left < n && array[left].weight > array[lr].weight)
	        	lr = left; // assign left to lr
	        // If right child is larger than the largest root.
	        if (right < n && array[right].weight > array[lr].weight)
	        	lr = right; //assign right to lr
	        // if the largest is the root then do the if statement
	        if (lr != i) // lr not equal to i
	        {
	            Kredge swap = array[i]; // swapping.
	            array[i] = array[lr];
	            array[lr] = swap;
	            heapify(array, n, lr); // heapify is executed recursively ...
	        }
	    } // end of the heapify function...
}// end of the sort weight class....

