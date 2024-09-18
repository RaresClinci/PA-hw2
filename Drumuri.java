import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


public class Drumuri {
	// valoare mai mare decat orice distanta din graf
	public static final long INF = Long.MAX_VALUE;

	// n = numar de noduri, m = numar de muchii
	int n, m;

	public class Pair implements Comparable<Pair> {
		public int destination;
		public long cost;

		Pair(int _destination, long _cost) {
			destination = _destination;
			cost = _cost;
		}

		public int compareTo(Pair rhs) {
			return Long.compare(cost, rhs.cost);
		}
	}

	ArrayList<Pair>[] adj;
	int x, y, z;

	public void solve() {
		readInput();
		writeOutput(bestSubGraph());
	}

	private void readInput() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("drumuri.in"));
			int n1, n2, n3;

			// citire dimensiuni
			String line = reader.readLine();
			String[] tok = line.split(" ");

			n = Integer.parseInt(tok[0]);
			m = Integer.parseInt(tok[1]);

			// initializare liste de adiacenta
			adj = new ArrayList[n + 1];
			for (int i = 1; i <= n; i++) {
				adj[i] = new ArrayList<>();
			}

			// citire muchi
			for (int i = 0; i < m; i++) {
				line = reader.readLine();
				tok = line.split(" ");

				n1 = Integer.parseInt(tok[0]);
				n2 = Integer.parseInt(tok[1]);
				n3 = Integer.parseInt(tok[2]);

				adj[n1].add(new Pair(n2, n3));
			}

			// citire x, y, z
			line = reader.readLine();
			tok = line.split(" ");
			x = Integer.parseInt(tok[0]);
			y = Integer.parseInt(tok[1]);
			z = Integer.parseInt(tok[2]);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeOutput(long result) {
		try {
			PrintWriter pw = new PrintWriter(new File("drumuri.out"));
			pw.printf(String.format("%d", result));
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// algoritmul lui dijkstra pentru distante minime dintr-un nod sursa
	private ArrayList<Long> dijkstra(int source, ArrayList<Pair>[] adj) {
		ArrayList<Long> d = new ArrayList<>();

		// initializare vector distante
		for (int i = 0; i <= n; i++) {
			d.add(INF);
		}

		PriorityQueue<Pair> que = new PriorityQueue<>();
		que.add(new Pair(source, 0));
		d.set(source, (long) 0);

		while (!que.isEmpty()) {
			Pair node = que.remove();

			// relaxarea drumurilor pana la neigh prin rerutare prin node
			for (Pair neigh : adj[node.destination]) {
				if (d.get(node.destination) + neigh.cost < d.get(neigh.destination)) {
					d.set(neigh.destination, d.get(node.destination) + neigh.cost);

					que.add(new Pair(neigh.destination, d.get(neigh.destination)));
				}
			}
		}

		return d;
	}

	// functie inversare graph => schimbam sensul muchilor
	private ArrayList<Pair>[] reverseGraph() {
		ArrayList<Pair>[] newAdj = new ArrayList[n + 1];
		for (int i = 1; i <= n; i++) {
			newAdj[i] = new ArrayList<>();
		}

		for (int node = 1; node <= n; node++) {
			for (Pair neigh : adj[node]) {
				newAdj[neigh.destination].add(new Pair(node, neigh.cost));
			}
		}

		return newAdj;
	}

	private long bestSubGraph() {
		// distanta de la x / y la fiecare nod
		ArrayList<Long> xCost = dijkstra(x, adj);
		ArrayList<Long> yCost = dijkstra(y, adj);

		// distanta de la orice nod la z
		ArrayList<Long> zCost = dijkstra(z, reverseGraph());

		// cautam cel mai bun nod intermediar pentru a conecta x si y cu z
		long min = Long.MAX_VALUE;
		for (int i = 1; i <= n; i++) {
			if (xCost.get(i) != INF && yCost.get(i) != INF && zCost.get(i) != INF) {
				if (min > xCost.get(i) + yCost.get(i) + zCost.get(i)) {
					min = xCost.get(i) + yCost.get(i) + zCost.get(i);
				}
			}
		}
		return min;
	}

	public static void main(String[] args) {
		new Drumuri().solve();
	}
}
