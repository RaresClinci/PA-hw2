# Precizari
In redactarea temei, am utilizat laboratorul ca schelet(modul in care este structurata functia
de citire si de scriere). De asemenea, algoritmi de sortare topologica si Dijkstra au fost preluati 
din rezolvarile mele din laborator si prelucrati pentru putin pentru problemele date.


## Problema 1 - Numarare
### Descrierea rezolvarii
Prima etapa a rezolvarii consta in crearea `intersectiei` celor 2 grafuri(obtinerea unui graf doar cu
muchiile comune), graf pe care il vom numi G.

Mai departe, am aplicat sortarea topologica pe G.

Am privit in continuare problema ca una de `programare dinamica`. Am calculat numarul de drumuri pe care
il are fiecare nod pana la nodul n.
>Caz de baza: dp[n] = 1  -- exista un drum de la n la n
> 
> Pas de iteratie: dp[i] = sum[dp[neigh]]

Nodurile sunt luate in ordine invers topologica pentru a termina cu noduri care au gradul intern 0.

### Complexitate
Vom analiza complexitatea temporala pe bucati: Sortarea topologica are O(|V| + |E|), intersectia are
O((|V| + |E|)*|E|) - primele 2 foruri sunt |V| + |E|, iar contains este |E| - iar partea de programare dinamica are O(|V| + |E|).

Utilizam o lista de adiacenta pentru a tine graful G, deci complexitatea spatiala este O(|E|) cu si
fara date de intrare. Avem de asemenea vectorul dp, vectorul topsort si stiva stack cu O(|V|).

>Complexitate temporala: O(|V| * |E| + |E|^2)
> 
> Complexitate spatiala: O(|V| + |E|)

## Problema 2 - Trenuri
### Descrierea rezolvarii
Pentru reprezentare mai usoara, am asociat oraselor numere. Avem mapul `nodes` care face asocierea 
dintre numele unui oras si numarul corespunzator.

Prima oara sortam graful topologic.

Apoi initializam un vector de distante cu -inf, iar distanta sursei o setam ca 1.

In continuare, parcurgem topologic nodurile, si pentru fiecare nod, dam vecinilor ca distanta
distanta sa + 1.

Parcurgand topologic, ne asiguram ca nu exista o muchie care nu este luata in considerare in
calcularea drumului maxim pentru ca ar intra in sursa.

Marcam distanta sursei ca 1 pentru a-o marca ca nod de start.

dist[dest] va fi lungimea drumului maxim

### Complexitate
Sortarea topologica este realizata in O(|V| + |E|), iar calcularea distantelor este realizata in
O(|V| + |E|).

>Complexitate temporala: O(|V| + |E|)

Avem o stiva stack pentru dfs, vectorul toposort, mapul cu nume si vectorul de distante cu O(|V|). In
cazul in care luam in considerare si datele de intrare, avem si lista de adiacenta cu O(|E|).

> Complexitate spatiala fara date de intrare: O(|V|)
> 
> Complexitate spatiala cu date de intrare: O(|V| + |E|)

## Problema 3 - Drumuri Obligatorii
### Descrierea rezolvarii
Aplicam de 3 ori Dijkstra:
- Odata pentru a obtine distantele de la x la restul nodurilor(vectorul `xCost`)
- Odata pentru a obtine distantele de la y la restul nodurilor(vectorul `yCost`)
- Odata aplicat pe graful `invers` pentru a obtine distantele de la restul nodurilor la z(vectorul 
`zCost`)

Apoi, vedem care dintre nodurile grafului are costul minim atunci cand este intermediar pentru ca x
si y sa ajunga la z.

Costul descris pentru un nod `i` este: `xCost[i] + yCost[i] + zCost[i]`

Intorcem costul minim gasit.
### Complexitate
Algoritmul Dijkstra are complexitatea temporala O(|E| * log|V|), inversarea grafului are 
O(|E| + |V|), iar cautarea minimului are O(|V|).
> Complexiate temporala: O(|V| + |E|*log|V|)

Utilizam vectorii xCost, yCost si zCost cu complexitate spatiala: O(|V|), de asemenea, cream un graf
nou invers, cu complexitatea spatiala O(|E|).

> Complexitate spatiala: O(|V| + |E|)

## Problema 4 - Scandal
### Descrierea rezolvarii
Problema poate fi redusa la 2-SAT care poate fi rezolvata cautand componente tare conexe intr-un
graf de incluziune logica.

Prima oara am folosit functia `parseGraph` pentru a transforma restrictiile date intr-un graf de
incluziune logica.
- Clauza c = 0 se reduce la `x | y` care poate fi scrisa ca `!x -> y | !y -> x`
- Clauza c = 1 se reduce la `!x -> !y`, insa, pentru a functiona 2SAT, includem si negatia ei 
`x -> y`
- Clauza c = 2 se reduce la `!y -> !x`, insa, pentru a functiona 2SAT, includem si negatia ei
`y -> x`
- Clauza c = 3 se reduce la `!x | !y` care poate fi scrisa ca `x -> !y | y -> !x`

Mai departe, am determinat componentele conexe folosind algoritmul lui kosaraju si am transformat
vectorul de componente conexe intr-un vector unde comp[i] spune in ce componenta se afla nodul i.

In algoritmul lui kosaraju, primele componente generate nu sunt influentate de cele din fata, deci
sunt cele originale. Selectam ca invitati doar cei care nu sunt influentati de negatia lor.

### Complexitate
Avem |V| = 2 * n si |E| = 2 * m.

Parsarea grafului se realizeaza in complexitate temporala O(2*m) = O(|E|), algoritmul lui Kosaraju
are O(|V| + |E|), transformarea si alegerea invitatilor se realizeaza in O(|V|).

> Complexitate temporala: O(|V| + |E|)

Utilizam o lista de adiacenta care are complexitate spatiala de O(|E|) si listele de vizitati si de
componente conexe cu complexitate O(|V|)

> Complexitate spatiala: O(|V| + |E|)

