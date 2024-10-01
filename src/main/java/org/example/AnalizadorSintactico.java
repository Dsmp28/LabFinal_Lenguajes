package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AnalizadorSintactico {
    private Grafo grafo;
    private Estado estadoInicial;

    //Stacks
    public AnalizadorSintactico(Grafo grafo, Estado estadoInicial) {
        this.grafo = grafo;
        this.estadoInicial = estadoInicial;
    }

    public void leerArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("*******************************************");
                if (validarCadena(linea)) {
                    System.out.println("La cadena \"" + linea + "\" es válida.");
                } else {
                    System.out.println("La cadena \"" + linea + "\" no es válida.");
                }
                mostrarArbolDerivacion(linea);
                mostrarTablaTransiciones(linea);
            }

        } catch (IOException e) {
            System.out.println("Error: No se encontró el archivo prueba.txt, por favor verifique la ruta.");
        }
    }

    public boolean validarCadena(String cadena) {
        Stack<Character> stackCaracteresValidos = new Stack<>();
        Stack<Character> stackEpsilon = new Stack<>();
        Stack<String> stackEstadosValidos = new Stack<>();

        Estado estadoActual = estadoInicial;
        boolean esValida = true;
        for (char c : cadena.toCharArray()) {
            boolean transicionEncontrada = false;
            for (Arista arista : grafo.getAristas()) {
                if (arista.getEstadoOrigen().equals(estadoActual) && arista.getCondicion().equals(String.valueOf(c))) {
                    estadoActual = arista.getEstadoDestino();
                    transicionEncontrada = true;
                    stackCaracteresValidos.push(c);
                    stackEstadosValidos.push(estadoActual.getNombre());
                    break;
                }
            }

            if (!transicionEncontrada) {
                if (estadoActual.esEpsilon()) {
                    transicionEncontrada = true;
                    stackEpsilon.push(c);
                }
                else if (!transicionEncontrada) {
                    System.out.println("Error: No se encontró una transición válida para el carácter '" + c + "' desde el estado " + estadoActual.getNombre());
                    esValida = false;
                    break;
                }
            }
        }
        System.out.println("Stack de caracteres válidos para la cadena \"" + cadena + "\": ");
        System.out.println(stackCaracteresValidos);
        System.out.println("Stack de estados válidos para la cadena \"" + cadena + "\": ");
        System.out.println(stackEstadosValidos);
        System.out.println("Stack de caracteres epsilon para la cadena \"" + cadena + "\": ");
        System.out.println(stackEpsilon);
        return esValida;
    }

    public void mostrarArbolDerivacion(String cadena) {
        Estado estadoActual = estadoInicial;
        List<String> derivaciones = new ArrayList<>();
        derivaciones.add(estadoInicial.getNombre());

        for (char c : cadena.toCharArray()) {
            boolean transicionEncontrada = false;
            for (Arista arista : grafo.getAristas()) {
                if (arista.getEstadoOrigen().equals(estadoActual) && arista.getCondicion().equals(String.valueOf(c))) {
                    derivaciones.add(arista.getCondicion() + " -> " + arista.getEstadoDestino().getNombre());
                    estadoActual = arista.getEstadoDestino();
                    transicionEncontrada = true;
                    break;
                }
            }
            if (!transicionEncontrada) {
                derivaciones.add("ERROR");
                break;
            }
        }

        System.out.println("Árbol de derivación:");
        imprimirDerivacion(derivaciones, 0, "", true);
    }

    private void imprimirDerivacion(List<String> derivaciones, int indice, String prefijo, boolean ultimo) {
        if (indice < derivaciones.size()) {
            String nodo = derivaciones.get(indice);
            System.out.println(prefijo + (ultimo ? "└── " : "├── ") + nodo);
            prefijo += (ultimo ? "    " : "│   ");

            if (!nodo.equals("ERROR") && indice + 1 < derivaciones.size()) {
                imprimirDerivacion(derivaciones, indice + 1, prefijo, indice + 2 == derivaciones.size());
            } else if (nodo.equals("ERROR")) {
                System.out.println(prefijo + "└── " + "ERROR en la derivación");
            }
        }
    }

    public void mostrarTablaTransiciones(String cadena) {
        Estado estadoActual = estadoInicial;
        List<String[]> transiciones = new ArrayList<>();
        boolean esValida = true;

        for (char c : cadena.toCharArray()) {
            boolean transicionEncontrada = false;
            for (Arista arista : grafo.getAristas()) {
                if (arista.getEstadoOrigen().equals(estadoActual) && arista.getCondicion().equals(String.valueOf(c))) {
                    String[] fila = {estadoActual.getNombre(), arista.getCondicion(), arista.getEstadoDestino().getNombre()};
                    transiciones.add(fila);
                    estadoActual = arista.getEstadoDestino();
                    transicionEncontrada = true;
                    break;
                }
            }
            if (!transicionEncontrada) {
                // Si no se encuentra una transición válida, agregamos la fila con "Error"
                String[] filaError = {estadoActual.getNombre(), String.valueOf(c), "Error"};
                transiciones.add(filaError);
                esValida = false;
                break;
            }
        }

        // Definir el ancho de las columnas
        int anchoColumna = 20;
        String separador = "+" + "-".repeat(anchoColumna) + "+" + "-".repeat(anchoColumna) + "+" + "-".repeat(anchoColumna) + "+";

        // Mostrar la tabla con formato
        System.out.println("Tabla de transiciones:");
        System.out.println(separador);
        System.out.format("|%-" + anchoColumna + "s|%-" + anchoColumna + "s|%-" + anchoColumna + "s|%n",
                centrarTexto("Estado Inicial", anchoColumna),
                centrarTexto("Valor de Transición", anchoColumna),
                centrarTexto("Estado Final", anchoColumna));
        System.out.println(separador);
        for (String[] fila : transiciones) {
            System.out.format("|%-" + anchoColumna + "s|%-" + anchoColumna + "s|%-" + anchoColumna + "s|%n",
                    centrarTexto(fila[0], anchoColumna),
                    centrarTexto(fila[1], anchoColumna),
                    centrarTexto(fila[2], anchoColumna));
            System.out.println(separador);
        }
    }

    // Método auxiliar para centrar el texto
    private String centrarTexto(String texto, int ancho) {
        if (texto.length() >= ancho) {
            return texto;  // Si el texto es más largo que el ancho, se deja igual
        }
        int espacios = (ancho - texto.length()) / 2;
        return " ".repeat(espacios) + texto + " ".repeat(ancho - texto.length() - espacios);
    }
}


