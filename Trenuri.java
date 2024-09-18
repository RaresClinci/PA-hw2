import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Trenuri {
	public static final int NMAX = (int) 1e5 + 5; // 10^5 + 5 = 100.005
	int m, n = 0;
	ArrayList<Integer>[] adj = new ArrayList[NMAX];
	HashMap<String, Integer> nodes;
	int[] visited;
	Stack<Integer> stack;
	String src, dest;

	public void solve() {
		readInput();
		writeOutput(longestPath());
	}

	private void writeOutput(int result) {
		try {
			PrintWriter pw = new PrintWriter(new File("trenuri.out"));
			pw.printf(String.format("%d", result));
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void readInput() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("trenuri.in"));
			String n1, n2, line = reader.readLine();
			String[] cities = line.split(" ");

			// initializare
			nodes = new HashMap<>();

			// adaugam cuvintele
			src = cities[0];
			nodes.put(src, ++n);

			dest = cities[1];
			nodes.put(dest, ++n);

			// numarul de edges
			line = reader.readLine();
			m = Integer.parseInt(line);

			for (int i = 0; i < m; i++) {
				line = reader.readLine();
				cities = line.split(" ");

				// a mai fost intalnit orasul? daca nu, il adaugam in hashmap
				n1 = cities[0];
				if (!nodes.containsKey(n1)) {
					nodes.put(n1, ++n);
				}

				if (adj[nodes.get(n1)] == null) {
					adj[nodes.get(n1)] = new ArrayList<>();
				}

				// idem
				n2 = cities[1];
				if (!nodes.containsKey(n2)) {
					nodes.put(n2, ++n);
				}

				if (adj[nodes.get(n2)] == null) {
					adj[nodes.get(n2)] = new ArrayList<>();
				}

				// adaugare muchie
				adj[nodes.get(n1)].add(nodes.get(n2));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// depth first search
	void dfs(Integer node) {
		visited[node] = 1;

		for (Integer neigh : adj[node]) {
			if (visited[neigh] == 0) {
				dfs(neigh);
			}
		}
		stack.push(node);
	}

	// algoritm de sortare topologica
	private ArrayList<Integer> topSort() {
		ArrayList<Integer> topsort = new ArrayList<>();

		visited = new int[n + 1];
		stack = new Stack<>();

		// parcurgere
		for (int city = 1; city <= n; city++) {
			if (visited[city] == 0) {
				dfs(city);
			}
		}

		// punere in ordine topologica
		while (!stack.empty()) {
			Integer node = stack.pop();
			topsort.add(node);
		}
		return topsort;
	}

	private int longestPath() {
		// sortam topologic
		ArrayList<Integer> topsort = topSort();

		// initiem vectorul de distante
		int[] dist = new int[n + 1];
		for (int node = 1; node <= n; node++) {
			dist[node] = Integer.MIN_VALUE;
		}
		dist[nodes.get(src)] = 1;

		// parcurgem nodurile topologic si dam vecinilor distanta nodului curent + 1
		for (Integer city : topsort) {
			for (Integer neigh : adj[city]) {
				if (dist[neigh] < dist[city] + 1) {
					dist[neigh] = dist[city] + 1;
				}
			}
		}

		return dist[nodes.get(dest)];
	}

	public static void main(String[] args) {
		new Trenuri().solve();
	}
}
