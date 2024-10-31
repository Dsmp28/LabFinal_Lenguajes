package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class AnalizadorSintactico {
    private Grafo grafo;
    private Estado estadoInicial;

    private List<String> cadenasValidas;
    private List<String> cadenasInvalidas;

    //Stacks
    public AnalizadorSintactico(Grafo grafo, Estado estadoInicial) {
        this.grafo = grafo;
        this.estadoInicial = estadoInicial;
        this.cadenasValidas = new ArrayList<>();
        this.cadenasInvalidas = new ArrayList<>();
    }

    public void leerArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("\n*******************************************");
                if (validarCadena(linea)) {
                    System.out.println("La cadena \"" + linea + "\" es válida.\n");
                    cadenasValidas.add(linea);
                } else {
                    System.out.println("La cadena \"" + linea + "\" no es válida.\n");
                    cadenasInvalidas.add(linea);
                }
                mostrarArbolDerivacion(linea);
                System.out.println();
                mostrarTablaTransiciones(linea);
                Scanner scanner = new Scanner(System.in);

                //Aqui va la parte de limpiar consola
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
                ClearConsole();
            }
            System.out.println("\n*******************************************");
            System.out.println("Cadenas válidas: " + cadenasValidas.size());
            for (String cadena : cadenasValidas) {
                System.out.println(cadena);
            }
            System.out.println("\nCadenas inválidas: " + cadenasInvalidas.size());
            for (String cadena : cadenasInvalidas) {
                System.out.println(cadena);
            }

        } catch (IOException e) {
            System.out.println("Error: No se encontró el archivo prueba.txt, por favor verifique la ruta.");
        }
    }
    public static void ClearConsole(){
        try{
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean validarCadena(String cadena) {
        // Dividir la cadena en dos cintas intercaladas
        StringBuilder cinta1 = new StringBuilder();
        StringBuilder cinta2 = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            if (i % 2 == 0) {
                cinta1.append(cadena.charAt(i));
            } else {
                cinta2.append(cadena.charAt(i));
            }
        }

        Estado estadoActual = estadoInicial;
        boolean esValida = true;

        // Proceso de validación original
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
                System.out.println("Error: No se encontró una transición válida para el carácter '" + c + "' desde el estado " + estadoActual.getNombre() + "\n");
                esValida = false;
                break;
            }
        }
        // Imprimir las dos cintas al final del proceso
        System.out.println("Cinta 1: " + cinta1);
        System.out.println("Cinta 2: " + cinta2);
        System.out.println();

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
            if (indice > 0){
                prefijo += (ultimo ? "         " : "│        ");
            }else {
                prefijo += (ultimo ? "    " : "│   ");
            }

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


