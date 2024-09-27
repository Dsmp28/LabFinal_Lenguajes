package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorSintactico {
    private Grafo grafo;
    private Estado estadoInicial;

    public AnalizadorSintactico(Grafo grafo, Estado estadoInicial) {
        this.grafo = grafo;
        this.estadoInicial = estadoInicial;
    }

    public void leerArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (validarCadena(linea)) {
                    System.out.println("La cadena \"" + linea + "\" es válida.");
                } else {
                    System.out.println("La cadena \"" + linea + "\" no es válida.");
                }
                mostrarArbolDerivacion(linea);
            }
        } catch (IOException e) {
            System.out.println("Error: No se encontró el archivo prueba.txt, por favor verifique la ruta.");
        }
    }

    public boolean validarCadena(String cadena) {
        Estado estadoActual = estadoInicial;
        for (char c : cadena.toCharArray()) {
            boolean transicionEncontrada = false;
            for (Arista arista : grafo.getAristas()) {
                if (arista.getEstadoOrigen().equals(estadoActual) && arista.getCondicion().equals(String.valueOf(c))) {
                    estadoActual = arista.getEstadoDestino();
                    transicionEncontrada = true;
                    break;
                }
            }
            if (!transicionEncontrada) {
                System.out.println("Error: No se encontró una transición válida para el carácter '" + c + "' desde el estado " + estadoActual.getNombre());
                return false;
            }
        }
        return true;
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
}


