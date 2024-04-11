import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class OperacionesDB {
    static Scanner scan = new Scanner(System.in);
    static java.sql.Connection con = DatabaseConnection.getInstance().getConnection();

    public OperacionesDB() {

    }

    public static void consultarPaises() throws SQLException {
        Statement st = con.createStatement();
        String sqlCodigosPais = "SELECT * FROM pais";
        ResultSet rs = st.executeQuery(sqlCodigosPais);
        System.out.println("--------------PAÍSES--------------");
        System.out.print("Código \t");
        System.out.println("Nombre\t");
        while (rs.next()) {
            System.out.printf("%-6d\t%s\n", rs.getInt(1), rs.getString(2));
        }
        st.close();
        rs.close();
    }

    public static void consultarJugadoresPais(int codPais) throws SQLException {
        Statement st = con.createStatement();
        String sqlCodigosPais = "SELECT * FROM jugador WHERE id_pais = " + codPais + ";";
        ResultSet rs = st.executeQuery(sqlCodigosPais);
        System.out.println("------------JUGADORES-------------");
        while (rs.next()) {
            System.out.printf("%-2d\t%-25s\t%-4d\t%-3d\t%-2d\t%s\n", rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6));
        }
        st.close();
        rs.close();
    }

    public static void nuevoPais(Pais pais) throws SQLException {
        if (!paisExiste(pais.getId())) {
            Statement st = con.createStatement();
            String sqlPais = "INSERT INTO pais VALUES (" + pais.getId() + ",'" + pais.getNombrePais() + "');";
            st.executeUpdate(sqlPais);
            st.close();
        } else {
            System.out.println("El país que está intentando introducir ya existe en la base de datos.");
        }
    }

    public static void nuevoJugador(Jugador jugador) throws SQLException {
        if (!jugadorExiste(jugador.getNombreJugador())) {
            Statement st = con.createStatement();
            String sqlJugador = "INSERT INTO jugador(nombre_jugador, año_nacimiento, altura, club, id_pais) VALUES ('" +
                    jugador.getNombreJugador() + "'," + jugador.getAñoNacimiento() + ","
                    + jugador.getAltura() + ",'" + jugador.getClub() + "',"
                    + jugador.getCodPais() + ");";
            st.executeUpdate(sqlJugador);
            st.close();
        } else {
            System.out.println("El jugador que está intentando introducir ya existe en la base de datos.");
        }
    }

    public static void eliminarPais(int codigoPais) throws SQLException {
        if (paisExiste(codigoPais)) {
            Statement st = con.createStatement();
            String sqlPais = "";
            st.executeUpdate(sqlPais);
            st.close();
        } else {
            System.out.println("El país que está intentando eliminar no existe.");
        }
    }

    public static void eliminarJugador(int codigoJugador) {

    }

    public static void cargarFichero(File fichero) throws SQLException {
        Statement st = con.createStatement();
        ArrayList<Jugador> jugadores = arrayJugadores(fichero);

        for (int i = 0; i < jugadores.size(); i++) {
            if (!paisExiste(jugadores.get(i).getCodPais())) {
                String sqlPais = "INSERT INTO pais VALUES (" + jugadores.get(i).getCodPais() + ",'" + jugadores.get(i).getNombrePais() + "');";
                st.executeUpdate(sqlPais);
            }

            if (!jugadorExiste(jugadores.get(i).getNombreJugador())) {
                String sqlJugador = "INSERT INTO jugador(nombre_jugador, año_nacimiento, altura, club, id_pais) VALUES ('" +
                        jugadores.get(i).getNombreJugador() + "'," + jugadores.get(i).getAñoNacimiento() + ","
                        + jugadores.get(i).getAltura() + ",'" + jugadores.get(i).getClub() + "',"
                        + jugadores.get(i).getCodPais() + ");";
                st.executeUpdate(sqlJugador);
            }
        }
        st.close();
    }

    public static boolean paisExiste(int codPais) throws SQLException {
        Statement st = con.createStatement();
        String sqlCodigosPais = "SELECT id_pais FROM pais WHERE id_pais = " + codPais + ";";
        ResultSet rs = st.executeQuery(sqlCodigosPais);
        rs.next();
        int resultado = rs.getInt(1);
        st.close();
        rs.close();
        return resultado != 0;
    }

    public static boolean jugadorExiste(String nombreJugador) throws SQLException {
        Statement st = con.createStatement();
        String sqlCodigosPais = "SELECT nombre_jugador FROM jugador WHERE nombre_jugador = '" + nombreJugador + "';";
        ResultSet rs = st.executeQuery(sqlCodigosPais);
        rs.next();
        String resultado = rs.getString("nombre_jugador");
        st.close();
        rs.close();
        return resultado != null;
    }

    public static ArrayList arrayJugadores(File f) {
        ArrayList<Jugador> jugadores = new ArrayList<>();

        try {
            DataInputStream f_in = new DataInputStream((new FileInputStream(f)));
            while(f_in.available() > 0) {
                jugadores.add(new Jugador(f_in.readInt(), f_in.readUTF(), f_in.readUTF(), f_in.readInt(), f_in.readFloat(), f_in.readUTF()));
            }
            f_in.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return jugadores;
    }
    public static Jugador datosJugador(int codpais) {
        System.out.println("Introducir el nombre del jugador:");
        String nombre = scan.nextLine();
        System.out.println("Introducir su año de nacemiento :");
        int año = scan.nextInt();
        System.out.println("Introducir su altura:");
        float altura = scan.nextFloat();
        scan.nextLine();
        System.out.println("Introducir su club");
        String club = scan.nextLine();
        return new Jugador(codpais,nombre,año,altura,club);
    }

    public static Pais datosPais() {
        System.out.println("Introducir el codigo del pais:");
        int codigo = scan.nextInt();
        scan.nextLine();
        System.out.println("Introducir el nombre del pais:");
        String pais = scan.nextLine();
        return new Pais(codigo,pais);
    }
    }
