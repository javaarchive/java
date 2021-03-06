import java.io.*;
import java.util.*;
public class dining {
	public static int[] dijkstra(Map<Pair, Integer> cost, List<List<Integer>> graph, final int N, int source) {
		int[] dist = new int[N];
		PriorityQueue<Integer> visited = new PriorityQueue<>(N,new Comparator<Integer>() {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				return Integer.compare(dist[arg0], dist[arg1]);
				//return 0;
			}
		});	
		
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[N-1] = 0; // Starting value
		//nodeQueue.add(N-1);
		visited.add(source);
		int u;
		while(!visited.isEmpty()) {
			//int u = nodeQueue.remove(); // Get next node
			u = visited.remove();
			List<Integer> adj = graph.get(u);
			for(int node:adj) {
				//if(dist[node] == Integer.MAX_VALUE) 
				{ // If this node has not been visited yet
					int edgeDist = cost.get(new Pair(u,node));
					int totalDist = dist[u] + edgeDist;
					if (totalDist < dist[node]) 
					{
	                    dist[node] = totalDist; 
						visited.add(node);
					}
					//nodeQueue.add(node);
				}
			}
		}
		return dist;
	}
	public static void main(String[] args) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader("1.in"));
		StringTokenizer st = new StringTokenizer(f.readLine());
		int N,M,K;
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		final int numOfCows =  N - 1;
		List<List<Integer>> graph = new ArrayList<>(M+1);
		for(int i = 0; i < M+1; i ++) {
			graph.add(new ArrayList<>());
		}
		Map<Pair,Integer> cost = new HashMap<>(); 
		for(int i =0 ; i < M; i ++) {
			st = new StringTokenizer(f.readLine());
			int start = Integer.parseInt(st.nextToken());
			int end = Integer.parseInt(st.nextToken());
			int edgeCost = Integer.parseInt(st.nextToken());
			graph.get(start-1).add(end-1);
			graph.get(end-1).add(start-1);
			cost.put(new Pair(start-1,end-1), edgeCost);
			cost.put(new Pair(end-1,start-1), edgeCost);
		}
		//System.out.println("Cost              :    "+cost);
		Map<Pair,Integer> costWithHaybales = new HashMap<>(cost); 
		System.out.println("Graph as an edgelist:  "+graph);
		int[] taste = new int[K];
		List<Integer> bales = new ArrayList<>();
		for(int i = 0; i < K; i ++) {
			st = new StringTokenizer(f.readLine());
			int index = Integer.parseInt(st.nextToken()) - 1;
			bales.add(index);
			int value = Integer.parseInt(st.nextToken());
			taste[i] = value;
		}
		f.close();
		//int[] empty = new int[N];
		//Arrays.fill(empty, Integer.MAX_VALUE);
		//empty[N-1] = 0;
		int[] distOrig,distTo;
		distOrig = dijkstra(cost, graph, N, N - 1);
		//for(int bale: graph.get(N-1)) {
		//	graph.get(bale).remove((Object) (N-1));
		//}
		//graph.get(N-1).clear();
		for(int i = 0 ; i < K;  i++) {
			int target = bales.get(i);
			graph.get(target).add(N);
			graph.get(N).add(target);
			//System.out.println("Dist: "+distOrig[target]+" Taste: "+taste[i]);
			//System.out.println(target+" -- "+(N-1) + " connected with weight "+(distOrig[target] - taste[i]));
			costWithHaybales.put(new Pair(target,N), distOrig[target] - taste[i]);
			//costWithHaybales.put(new Pair(N,target), distOrig[target] - taste[i]);
		}
		//System.out.println("Modified New Graph  :  "+graph);
		System.out.println("Cost              :    "+cost);
		System.out.println("Cost with haybales:    "+costWithHaybales);
		distTo = dijkstra(costWithHaybales, graph, N+1, N);
		System.out.println("Output          :" + Arrays.toString(distOrig));
		System.out.println("Output(haybales):" + Arrays.toString(distTo));
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("dining.out")));
		for(int i  =0 ; i < numOfCows; i ++) {
			if(distTo[i] <= distOrig[i]) {
				pw.println(1);
			}else {
				pw.println(0);
			}
		}
		pw.close();
	}
}
// Order does matter pair
class Pair{
	int x,y;
	public Pair(int x,int y) {
		this.x =x;
		this.y =y;
	}
	@Override
	public String toString() {
		return "("+this.x+","+this.y+")";
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pair) {
			Pair p = (Pair) obj;
			if((p.x == this.x && p.y == this.y)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode(){
		return Integer.hashCode(this.x) * (Integer.hashCode(this.y)+1);
	}
}