# Tradutor de GLC para Chomsky
Esse projeto tem com objetivo transformar uma gramática livre de contexto (GLC) G em uma GLC G' equivalente na forma normal de Chomsky.

## Autores
- Desenvolvedores: [Pedro Vaz](https://github.com/vazConnected) e [Ana Julia Velasque](https://github.com/anajvelasque);
- Mentor: [Andrei Rimsa](https://github.com/rimsa).

## Biblioteca Externa Utilizada
- [JSON in Java](https://github.com/stleary/JSON-java/), por [Sean Leary](https://github.com/stleary/).

## Entrada e Saída
Esse programa recebe um arquivo JSON com uma gramárica no seguinte formato:
```
{ "glc": [
    [<array com os identificadores das regras>],
    [<array com os elementos do alfabeto>],
    [
        [<identificador da regra>, <transicao>],
        [<identificador da regra>, <transicao>],
        ...
        [<identificador da regra>, <transicao>]
    ],
    <regra inicial>
]}
```

Como saída, é passado à saída padrão um JSON no mesmo formato do arquivo de entrada.

## Descrição de Implementação
O programa recebe uma especificação de uma GLC G = (V, Σ, R, P) no formato JSON como consta no exemplo abaixo:

- G
```
P -> ABC | bCC
A -> aAA | BB
B -> λ
C -> ABC | b
```

- G.json
```
{ "glc": [
    ["P", "A", "B", "C"],
    ["a", "b"],
    [
        ["P", "ABC"],
        ["P", "bCC"],
        ["A", "aAA"],
        ["A", "BB"],
        ["B", "#"],
        ["C", "ABC"],
        ["C", "b"]
    ],
    "P"
]}
```

Para a transformação de G para G', devem ser realizadas as seguintes etapas:
1. Eliminar regras λ;
2. Eliminar regras unitárias;
3. Eliminar variáveis inúteis;
4. Modificar cada regra X -> w, |w| >= 2, de forma que ela contenha apenas
variáveis;
5. Substituir cada regra X -> Y1Y2...Yn, n >= 3, em que cada Yi é uma variável,
pelo conjunto de regras: X -> Y1Z1, Z1 -> Y2Z2, ..., Zn-2 -> Yn-1Yn, em que Z1, Z2, ..., Zn-2 são novas variáveis.
