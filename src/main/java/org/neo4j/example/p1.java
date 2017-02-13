package myproc;

import java.util.List;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.ResourceIterator;

import org.neo4j.procedure.*;

/**
 * This is an example how you can create a simple user-defined function for Neo4j.
 */
public class p1
{
	@Context
    public GraphDatabaseService graphDB;    

	public enum RelationshipTypes implements RelationshipType {
    	CONNECTED;
    }

    public enum Labels implements Label {
    	NODE,
    	REACHED;
    }


	@UserFunction
	//@Procedure(value = "myproc.p1", mode = Mode.WRITE)
	@Description("myproc.p1(['s1','s2',...], delimiter) - join the given strings with the given delimiter.")
    public void p1(
            @Name("strings") List<String> strings,
            @Name(value = "delimiter", defaultValue = ",") String delimiter) {
		
		System.out.println("T01, here in the new procedure!");

		/*
        // Begin some search
            // Find all users
            ResourceIterator<Node> nodes = graphDB.findNodes( Labels.Node );
            System.out.println( "Nodes:" );
            while( nodes.hasNext() )
            {
                Node node = nodes.next();
                System.out.println( "\t" + node.getProperty( "id" ) );
            }
		*/

		// Initialize
		// node 0 -> reached
			Node node_next = graphDB.findNode(Labels.Node, "id", 0);
			System.out.println("get the node:"+ node_next);
			//node_next.setProperty( "reached", 1 );
			node_next.addLabel(REACHED);
		// EdgeFrontier 0->outedge
			//TODO load into mem/ add a property
			// just give one more property to nodes?
			List edge_frontier = new ArrayList();
			for (Relationship line_from_node_next: node_next.getRelationships( CONNECTED )) {
				edge_frontier.add(line_from_node_next);
			}

		// While loop  edgelist not empty
		while (!empty(edge_frontier)) {
			// find a smallest relationship
			node_next = get_smallest(edge_frontier);
			
			// add node and update edges(delete, add)
			node_next.addLabel(REACHED);
				// delete edges 
			update_edge_frontier(edge_frontier, node_next);
				// add edges (to not reached ones)

		}
		// Printout according to extended property of the relationship

		// return String.join(delimiter, strings);
    	return;
	}
}
