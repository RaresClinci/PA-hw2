import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Numarare {
	public static final int NMAX = (int) 1e5 + 5;
	public static final int MOD = (int) 1000000007;
	int m, n;
	ArrayList<Integer>[] adj1 = new ArrayList[NMAX];
	ArrayList<Integer>[] adj2 = new ArrayList[NMAX];
	ArrayList<Integer>[] commAdj = new ArrayList[NMAX];
	boolean[] visited;
	Stack<Integer> stack = new Stack<>();

	public void solve() {
		readInput();
		writeOutput(numPaths());
	}

	private void writeOutput(long result) {
		try {
			PrintWriter pw = new PrintWriter(new File("numarare.out"));
			pw.printf(String.format("%d", result));
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void readInput() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("numarare.in"));

			int i, x, y;
			String line = reader.readLine();
			String[] tok = line.split(" ");

			// citim dimensiunile
			n = Integer.parseInt(tok[0]);
			m = Integer.parseInt(tok[1]);

			// initiem valorile matricilor de adiacenta
			for (i = 1; i <= n; i++) {
				adj1[i] = new ArrayList<>();
				adj2[i] = new ArrayList<>();
				commAdj[i] = new ArrayList<>();
			}

			// citire graf 1
			for (i = 0; i < m; i++) {
				line = reader.readLine();
				tok = line.split(" ");

				x = Integer.parseInt(tok[0]);
				y = Integer.parseInt(tok[1]);

				adj1[x].add(y);
			}

			// citire graf 2
			for (i = 0; i < m; i++) {
				line = reader.readLine();
				tok = line.split(" ");

				x = Integer.parseInt(tok[0]);
				y = Integer.parseInt(tok[1]);

				adj2[x].add(y);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// calcul intersectie grafuri => lista adiacenta cu muchii comune
	private void graphIntersection() {
		for (int node = 1; node <= n; node++) {
			for (Integer neigh : adj1[node]) {
				if (adj2[node].contains(neigh)) {
					commAdj[node].add(neigh);
				}
			}
		}
	}

	// algoritm dfs care pastreaza in stack ordinea de parcurgere
	void dfs(int node) {
		Stack<Integer> order = new Stack<>();
		order.push(node);

		while (!order.isEmpty()) {
			int curr = order.peek();

			if (!visited[curr]) {
				visited[curr] = true;
				for (int neighbor : commAdj[curr]) {
					if (!visited[neighbor]) {
						order.push(neighbor);
					}
				}
			} else {
				stack.push(order.pop());
			}
		}
	}

	// aloritm de sortare topologica
	private ArrayList<Integer> topSort() {
		ArrayList<Integer> topsort = new ArrayList<>();

		visited = new boolean[n + 1];
		stack = new Stack<>();

		// initializa cu -1
		for (int i = 1; i <= n; i++) {
			visited[i] = false;
		}

		// parcurgere
		for (int i = 1; i <= n; i++) {
			if (!visited[i]) {
				dfs(i);
			}
		}

		// asezare in ordinea topologica
		while (!stack.empty()) {
			int node = stack.pop();
			topsort.add(node);
		}
		return topsort;
	}

	private long numPaths() {
		// aplicam sortare topologica pe intersectia grafurilor
		graphIntersection();
		ArrayList<Integer> topsort = topSort();

		// modelam problema dp, unde dp[i] tine numarul de drumuri pana la n(n are evident un drum)
		long[] dp = new long[n + 1];
		dp[n] = 1;

		for (int i = topsort.size() - 1; i >= 0; i--) {
			for (Integer neigh : commAdj[topsort.get(i)]) {
				// adunam la nodul curent numarul de drumuri al vecinilor
				dp[topsort.get(i)] = (dp[topsort.get(i)] + dp[neigh]) % MOD;
			}
		}

		// rezultatul se va afla in dp[1]
		return dp[1];
	}

	public static void main(String[] args) {
		new Numarare().solve();
	}
}
