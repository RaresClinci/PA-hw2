import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;


public class Scandal {
	// valoare mai mare decat orice distanta din graf
	public static final long INF = Long.MAX_VALUE;

	// n = numar de noduri, m = numar de muchii
	int n, m;

	// clasa regula care descrie o restrictie intre 2 persoane
	class Rule {
		int x, y, c;

		Rule(int _x, int _y, int _c) {
			x = _x;
			y = _y;
			c = _c;
		}
	}

	// vector de restrictii
	ArrayList<Rule> rules = new ArrayList<>();

	public void solve() {
		readInput();
		writeOutput(guestList());
	}

	private void readInput() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("scandal.in"));
			int x, y, c;

			String line = reader.readLine();
			String[] tok = line.split(" ");

			// citire dimensiuni
			n = Integer.parseInt(tok[0]);
			m = Integer.parseInt(tok[1]);

			// citire restrictii
			for (int i = 0; i < m; i++) {
				line = reader.readLine();
				tok = line.split(" ");

				rules.add(new Rule(Integer.parseInt(tok[0]), Integer.parseInt(tok[1]),
						Integer.parseInt(tok[2])));
			}


		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeOutput(ArrayList<Integer> result) {
		try {
			PrintWriter pw = new PrintWriter(new File("scandal.out"));

			pw.printf(String.format("%d\n", result.size()));
			for (Integer gst : result) {
				pw.printf(String.format("%d\n", gst));
			}
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// functie care parseaza regulile intr-un graf de implicatii logice
	private ArrayList<Integer>[] parseGraph() {
		ArrayList<Integer>[] adj = new ArrayList[2 * n + 1];

		for (int i = 1; i <= 2 * n; i++) {
			adj[i] = new ArrayList<>();
		}

		for (Rule r : rules) {
			if (r.c == 0) {
				// x | y == !x -> y | !y -> x
				adj[r.x + n].add(r.y);
				adj[r.y + n].add(r.x);
			} else if (r.c == 1) {
				// !x -> !y si y -> x
				adj[r.x + n].add(r.y + n);
				adj[r.y].add(r.x);
			} else if (r.c == 2) {
				// !y -> !x si x -> y
				adj[r.y + n].add(r.x + n);
				adj[r.x].add(r.y);
			} else if (r.c == 3) {
				// !x | !y
				adj[r.x].add(r.y + n);
				adj[r.y].add(r.x + n);
			}
		}

		return adj;
	}


	// primul dfs folosit de kosaraju pentru crearea stackului
	public void dfs1(ArrayList<Integer>[] adj, int node, boolean[] visited, Stack<Integer> comp) {
		Stack<Integer> stack = new Stack<>();
		stack.push(node);

		while (!stack.isEmpty()) {
			int x = stack.pop();

			if (!visited[x]) {
				visited[x] = true;
				comp.push(x);
				for (int neigh : adj[x]) {
					if (!visited[neigh]) {
						stack.push(neigh);
					}
				}
			}
		}
	}

	// al doilea dfs folosit de kosaraju pentru obtinerea componentelor tare conexe
	public void dfs2(ArrayList<Integer>[] adj, int node, boolean[] visited,
		ArrayList<Integer> comp) {
		Stack<Integer> stack = new Stack<>();
		stack.push(node);

		while (!stack.isEmpty()) {
			int x = stack.pop();

			if (!visited[x]) {
				visited[x] = true;
				comp.add(x);
				for (int neigh : adj[x]) {
					if (!visited[neigh]) {
						stack.push(neigh);
					}
				}
			}
		}
	}

	// functie de inversare a grafului => schimbam sensul arcelor
	private ArrayList<Integer>[] reverseGraph(ArrayList<Integer>[] adj, int n) {
		ArrayList<Integer>[] newAdj = new ArrayList[n + 1];
		for (int i = 1; i <= n; i++) {
			newAdj[i] = new ArrayList<>();
		}

		for (int node = 1; node <= n; node++) {
			for (Integer neigh : adj[node]) {
				newAdj[neigh].add(node);
			}
		}

		return newAdj;
	}

	// algoritmul lui kosaraju de obtinere a componentelor tare conexe
	private ArrayList<ArrayList<Integer>> kosaraju(int n, ArrayList<Integer>[] adj) {
		boolean[] visited1 = new boolean[n + 1];
		boolean[] visited2 = new boolean[n + 1];
		Stack<Integer> order = new Stack<>();

		// obtinem stiva cu ordinea
		for (int i = 1; i <= n; i++) {
			if (!visited1[i]) {
				dfs1(adj, i, visited1, order);
			}
		}

		// inversam graful
		ArrayList<Integer>[] revAdj = reverseGraph(adj, n);

		// obtinem componentele conxe
		ArrayList<ArrayList<Integer>> allComp = new ArrayList<>();
		while (!order.empty()) {
			int x = order.pop();

			if (!visited2[x]) {
				ArrayList<Integer> comp = new ArrayList<>();

				dfs2(revAdj, x, visited2, comp);
				allComp.add(comp);

			}
		}
		return allComp;
	}


	private ArrayList<Integer> guestList() {
		// obtinem componentele conexe
		ArrayList<Integer>[] adj = parseGraph();
		ArrayList<ArrayList<Integer>> allComp = kosaraju(2 * n, adj);

		// transformam vectorul de componente conexe intr-un vector caracteristic care spune
		// in ce componenta e fiecare nod
		int[] comp = new int[2 * n + 1];
		for (int i = 0; i < allComp.size(); i++) {
			ArrayList<Integer> cp = allComp.get(i);
			for (Integer inv : cp) {
				comp[inv] = i;
			}
		}

		// alegem invitatii
		ArrayList<Integer> guest = new ArrayList<>();
		for (int inv = 0; inv <= n; inv++) {
			if (comp[inv] > comp[inv + n]) {
				guest.add(inv);
			}
		}

		return guest;
	}

	public static void main(String[] args) {
		new Scandal().solve();
	}
}
