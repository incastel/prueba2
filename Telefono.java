import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Clase Telefono
 * Descripcion: me gustas Justin Bieber. Odio a Metallica, eso no es música.
 * @author Pablo Moreno Pradas
 * @version 14.04.2015
 */
public class Telefono extends JFrame
{
    private static final int ESPACIO = 10;
    private static final Object [] VALOR_BOTONES = {1, 2, 3, 4, 5, 6, 7, 8, 9, "B", 0, "L", "R", "LL", "C"};
    private static final String MENSAJE_LLAMADA = "¡ L L A M A N D O !";
    private static final String NUMERO_FIJO = "941987654";
    
    private JTextField display;
    private JPanel panelBotones;
    private Stack<String> pila;
    private int numeroActual;
    private long tiempoLlamada;
    private ArrayList<String> listaTiempos;
    
    /**
     * Constructor de objetos de la clase Telefono
     */
    public Telefono()
    {
        super("");
        numeroActual = 0;
        pila = new Stack<String>();
        listaTiempos = new ArrayList<String>();
        crearVentana();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((d.width / 2) - (this.getWidth() / 2), (d.height / 2) - (this.getHeight() / 2));
        setVisible(true);
    }
    
    /**
     * Crea la ventana
     */
    private void crearVentana()
    {
        // Contenido de la ventana
        JPanel contenedor = (JPanel) this.getContentPane();
        BorderLayout layout = new BorderLayout(ESPACIO, ESPACIO);
        contenedor.setLayout(layout);
        contenedor.setBorder(new EmptyBorder(ESPACIO, ESPACIO, ESPACIO, ESPACIO));
        
        // Campo de texto
        display = new JTextField();
        contenedor.add(display, layout.NORTH);
        
        // Panel de botones
        GridLayout grid = new GridLayout(5,3, ESPACIO, ESPACIO);
        panelBotones = new JPanel();
        panelBotones.setLayout(grid);        
        agregarBotones();
        contenedor.add(panelBotones, layout.CENTER);
        
        // Prepara la ventana
        pack();
    }
    
    /**
     * Agrega los botones al panel de botones
     */
    private void agregarBotones()
    {
        for (final Object valor : VALOR_BOTONES){
            JButton boton = new JButton(valor.toString());
            boton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        accion(valor);
                    }
                }
            );
            
            // Asigna el ToolTip según el contenido del botón
            if (valor instanceof String){
                String accion = valor.toString();
                if (accion.equals("B")){
                    boton.setToolTipText("Borra el último dígito");
                } else if (accion.equals("L")){
                    boton.setToolTipText("Limpia la pantalla");
                } else if (accion.equals("R")){
                    boton.setToolTipText("Repite el último número marcado");
                } else if (accion.equals("LL")){
                    boton.setToolTipText("Realiza una llamada");
                } else if (accion.equals("C")){
                    boton.setToolTipText("Cierra el teléfono");
                }
            } else {
                boton.setToolTipText("Marca el número " + valor);
            }
            panelBotones.add(boton);
        }
    }
    
    /**
     * Realiza la acción correspondiente al objeto dado
     */
    private void accion(Object contenido)
    {
        if (contenido instanceof String){
            String accion = contenido.toString();
            if (accion.equals("B")){
                borrarUltimoNumero();
            } else if (accion.equals("L")){
                limpiarTexto();
            } else if (accion.equals("R")){
                repetir();
            } else if (accion.equals("LL")){
                llamar();
            } else if (accion.equals("C")){
                cerrar();
            }
        } else {
            agregarNumero(contenido);
            //int numero = (Integer) contenido;
            //agregarNumero(numeroAleatorio(numero));
            //agregarNumeroFijo();
        }
    }
    
    /**
     * Agrega el número al campo de texto
     * @param  contenido El contenido del botón
     */
    private void agregarNumero(Object contenido)
    {
        if (! display.getText().equals("¡ L L A M A N D O !")){
            display.setText(display.getText() + contenido);
        }
    }
    
    /**
     * Borra el último dígito del campo de texto
     */
    private void borrarUltimoNumero()
    {
        String textoActual = display.getText();
        if (textoActual.length() > 0){
            display.setText(textoActual.substring(0, textoActual.length() - 1));
        }
    }
    
    /**
     * Limpia el campo de texto
     */
    private void limpiarTexto()
    {
        display.setText("");
        long tiempoTotal = (System.currentTimeMillis() - tiempoLlamada) / 1000;
        int minutos = (int)tiempoTotal / 60;
        int segundos = (int)tiempoTotal % 60;
        listaTiempos.add((minutos >= 10 ? minutos : "0" + minutos) + ":" + (segundos >= 10 ? segundos : "0" + segundos));
    }
    
    /**
     * Repite el último número marcado
     */
    private void repetir()
    {
        if (! pila.empty()){
            display.setText(pila.pop());
        }
    }
    
    /**
     * Realiza una llamada
     */
    private void llamar()
    {
        pila.push(display.getText());
        display.setText(MENSAJE_LLAMADA);
        tiempoLlamada = System.currentTimeMillis();
    }
    
    /**
     * Cierra la aplicación
     */
    private void cerrar()
    {
        mostrarListaTiempos();
        System.exit(0);
    }
    
    /**
     * Genera un número aleatorio distinto del número original
     * @param numeroOriginal El número original
     * @return El nuevo número
     */
    private int numeroAleatorio(int numeroOriginal)
    {
        Random generador = new Random();
        int nuevoNumero = generador.nextInt(10);
        if (nuevoNumero == numeroOriginal){
            return numeroAleatorio(numeroOriginal);
        }
        return nuevoNumero;
    }
    
    /**
     * Agrega el digito correspondiente del número fijo
     */
    private void agregarNumeroFijo()
    {
        String numero = NUMERO_FIJO.charAt(numeroActual) + "";
        agregarNumero(Integer.parseInt(numero));
        numeroActual = (numeroActual + 1) %  NUMERO_FIJO.length();
    }
    
    /**
     * Muestra la lista de tiempos de las llamadas realizadas
     */
    private void mostrarListaTiempos()
    {
        if (listaTiempos.size() > 0){
            for (String tiempo : listaTiempos){
                System.out.println(tiempo);
            }
        }
    }
}