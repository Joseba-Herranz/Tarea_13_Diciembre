package proyecto_Hibernate;

public class MenuHandler {
    private HibernateHandler hibernateHandler;

    public MenuHandler() {
        this.hibernateHandler = new HibernateHandler();
    }

    public void showMenu() {
        System.out.println("--------------------------------");
        System.out.println("1. Cargar base de datos desde fichero");
        System.out.println("2. Mostrar base de datos");
        System.out.println("3. Volcar base de datos a XML");
        System.out.println("4. Salir");
        System.out.println("--------------------------------");
    }

    public void handleOption(int option) {
        switch (option) {
            case 1:
                hibernateHandler.cargarBaseDeDatos();
                break;
            case 2:
            	hibernateHandler.mostrarBaseDeDatos();
                break;
            case 3:
            	hibernateHandler.volcarBaseDeDatos();
                break;
            case 4:
                System.out.println("Saliendo del programa...");
                System.exit(0);
            default:
                System.out.println("Opción no válida");
        }
    }
}



