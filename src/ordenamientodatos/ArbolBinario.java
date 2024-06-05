package ordenamientodatos;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ArbolBinario {

    private Nodo raiz;
    private int criterio;

    public ArbolBinario(Nodo raiz) {
        this.raiz = raiz;
    }

    public ArbolBinario() {
    }

    public Nodo getRaiz() {
        return raiz;
    }
    
    public int getCriterio() {
        return criterio;
    }

    public void setCriterio(int criterio) {
        this.criterio = criterio;
    }
    
    public boolean insertarNodo(Nodo n) {
        if (raiz == null) {
            raiz = n;
            return true;
        }

        Nodo actual = raiz;
        Nodo padre;

        while (true) {
            padre = actual;

            // Si el documento es igual, no se permite la inserción
            if (n.getDocumento().equals(actual.getDocumento())) {
                return false;

            } else if (!Documento.esMayor(n.getDocumento(), actual.getDocumento(), criterio)) {
                // Si el documento es menor, avanza hacia el subárbol izquierdo
                actual = actual.izquierdo;
                if (actual == null) {
                    padre.izquierdo = n;
                    return true;
                }
            } // Si el documento es mayor, avanza hacia el subárbol derecho
            else {
                actual = actual.derecho;
                if (actual == null) {
                    padre.derecho = n;
                    return true;
                }
            }
        }
    }

    public void recorrerInorden(Nodo n) {
        if (n != null) {
            recorrerInorden(n.izquierdo);
            System.out.println(n.getDocumento().getNombreCompleto() + " " + n.getDocumento().getDocumento());
            recorrerInorden(n.derecho);
        }
    }
    
    public void mostrar(JTable tbl) {
        String[][] datos = null;
        if (raiz != null) {
            datos = new String[Documento.documentos.size()][Documento.encabezados.length];

            Nodo n = raiz;
            Nodo predecesor;
            int fila = -1;

            while (n != null) {
                if (n.izquierdo == null) {
                    fila++;
                    datos[fila][0] = n.getDocumento().getApellido1();
                    datos[fila][1] = n.getDocumento().getApellido2();
                    datos[fila][2] = n.getDocumento().getNombre();
                    datos[fila][3] = n.getDocumento().getDocumento();

                    n = n.derecho;
                } else {
                    // Encuentra el nodo predecesor
                    predecesor = n.izquierdo;
                    while (predecesor.derecho != null && predecesor.derecho != n) {
                        predecesor = predecesor.derecho;
                    }

                    // Si el predecesor aún no ha sido enlazado al nodo actual, enlázalo y avanza a la izquierda
                    if (predecesor.derecho == null) {
                        predecesor.derecho = n;
                        n = n.izquierdo;
                    } else {
                        // Si el predecesor ya está enlazado al nodo actual, rompe el enlace, procesa el nodo y avanza a la derecha
                        predecesor.derecho = null;
                        fila++;
                        datos[fila][0] = n.getDocumento().getApellido1();
                        datos[fila][1] = n.getDocumento().getApellido2();
                        datos[fila][2] = n.getDocumento().getNombre();
                        datos[fila][3] = n.getDocumento().getDocumento();
                        n = n.derecho;
                    }
                }
            }

        }
        DefaultTableModel dtm = new DefaultTableModel(datos, Documento.encabezados);
        tbl.setModel(dtm);
    }

    // Método para buscar un documento en el árbol binario
    public List<Documento> buscar(String criterioBusqueda) {
        List<Documento> resultados = new ArrayList<>();
        buscarEnArbol(raiz, criterioBusqueda, resultados);
        return resultados;
    }

    // Método recursivo para buscar un documento en el árbol binario
    private void buscarEnArbol(Nodo nodo, String criterioBusqueda, List<Documento> resultados) {
        if (nodo != null) {
            buscarEnArbol(nodo.izquierdo, criterioBusqueda, resultados);

            // Verificar si el documento cumple con el criterio de búsqueda
            if (cumpleCriterio(nodo.getDocumento(), criterioBusqueda)) {
                resultados.add(nodo.getDocumento());
            }

            buscarEnArbol(nodo.derecho, criterioBusqueda, resultados);
        }
    }

    // Método para verificar si un documento cumple con el criterio de búsqueda
    private boolean cumpleCriterio(Documento documento, String criterioBusqueda) {
        // Convertir tanto el nombre completo del documento como el criterio de búsqueda a minúsculas
        String nombreCompleto = documento.getNombreCompleto().toLowerCase();
        String criterio = criterioBusqueda.toLowerCase();

        // Verificar si el nombre completo del documento contiene el criterio de búsqueda
        boolean contieneNombre = nombreCompleto.contains(criterio);

        // Verificar si el tipo de documento del documento contiene el criterio de búsqueda
        boolean contieneDocumento = documento.getDocumento().toLowerCase().contains(criterio);

        // Retornar true si el documento cumple con alguno de los criterios de búsqueda, false de lo contrario
        return contieneNombre || contieneDocumento;
    }
}