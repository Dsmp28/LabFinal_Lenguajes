package org.example;

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // Crear analizador sintáctico
        AnalizadorSintactico analizador = GrafoConstructor(grafo);

        // Leer y validar cadenas desde un archivo
        analizador.leerArchivo("src/main/resources/prueba.txt");
    }



    private static AnalizadorSintactico GrafoConstructor(Grafo grafo){
        // Crear estados
        Estado estado0 = new Estado(0, "q0");
        Estado estado1 = new Estado(1, "q1");
        Estado estado2 = new Estado(2, "q2");
        Estado estado3 = new Estado(3, "q3");
        Estado estado4 = new Estado(4, "q4");

        // Agregar estados al grafo
        grafo.agregarEstado(estado0);
        grafo.agregarEstado(estado1);
        grafo.agregarEstado(estado2);
        grafo.agregarEstado(estado3);
        grafo.agregarEstado(estado4);

        // Crear aristas
        Arista arista1 = new Arista(estado0, estado1, "a");
        Arista arista2 = new Arista(estado1, estado2, "b");
        Arista arista3 = new Arista(estado2, estado3, "a");
        Arista arista4 = new Arista(estado3, estado3, "a");
        Arista arista5 = new Arista(estado3, estado4, "*");
        Arista arista6 = new Arista(estado4, estado4, "#");
        Arista arista7 = new Arista(estado4, estado3, "a");
        Arista arista8 = new Arista(estado4, estado1, "b");
        Arista arista9 = new Arista(estado3, estado1, "b");

        // Agregar aristas al grafo
        grafo.agregarArista(arista1);
        grafo.agregarArista(arista2);
        grafo.agregarArista(arista3);
        grafo.agregarArista(arista4);
        grafo.agregarArista(arista5);
        grafo.agregarArista(arista6);
        grafo.agregarArista(arista7);
        grafo.agregarArista(arista8);
        grafo.agregarArista(arista9);

        return new AnalizadorSintactico(grafo, estado0);
    }
}