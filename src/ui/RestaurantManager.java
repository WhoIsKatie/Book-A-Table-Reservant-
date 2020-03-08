package ui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import model.Customer;
import model.Reservation;
import model.Table;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class RestaurantManager implements Serializable {

    List<Table> allTables;
    HashMap<Integer, List<Reservation>> reservations;

//    initialize variable
     public RestaurantManager() {
         reservations = new HashMap<>();
         for (int i = 0; i < 24; i++) {
             reservations.put(i, new LinkedList<Reservation>());
         }
    }

//   corresponding table at time is added to the reservations hashmap
//    updates ui
    public void addReservation(Integer time, TableDisplay tableDisplay, Customer customer) {
         Boolean contains = false;
         if (time < 24) {
             List<Reservation> reservs = reservations.get(time);
             if (!reservs.isEmpty()) {
                 for (Reservation r : reservs) {
                     if (r.getTable().equals(tableDisplay.getTable())) {
                         contains = true;
                         break;
                     }
                 }
             }
             if (contains.equals(false)) {
                 reservations.get(time).add(new Reservation(tableDisplay.getTable(), customer));
                 tableDisplay.reserve();
             }
         }
    }

//    removes corresponding reservations
//    updates ui
    public void removeReservation(Integer time, TableDisplay tableDisplay) {
        List<Reservation> reservs = reservations.get(time);
        for (Reservation r: reservs) {
            if (r.getTable().equals(tableDisplay.getTable())) {
                reservations.get(time).remove(r);
                tableDisplay.free();
            }
        }
    }

    public List<Reservation> getReservations(Integer time) {
         if (time <= 24) {
             return reservations.get(time);
         }
         return null;
    }

    public void setReservation(Integer time, List<Reservation> reservationList) {
         reservations.put(time, reservationList);
    }

    public void addTable(Table table) {
         allTables.add(table);
    }

    public static void save(Serializable RestaurantManager, String fileName) {
         try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
             oos.writeObject(RestaurantManager);
         } catch (IOException e) {
             System.out.println("IOException caught");
         }
    }

    public static RestaurantManager load(String fileName) {
         try {
             ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
             return (RestaurantManager) ois.readObject();
         } catch (IOException e) {
             System.out.println("IOException caught");
             return null;
         } catch (ClassNotFoundException e) {
             System.out.println("ClassNotFoundException caught");
             return null;
         }
    }
  
//    @Override
//    public void save(PrintWriter printWriter) {
//        System.out.println("save run");
//        for (int i = 0; i < reservations.size(); i++) {
//            List<Reservation> reservationList = reservations.get(i);
//            for (int n = 0; n < reservationList.size(); i++) {
//                printWriter.print(reservationList.get(i));
//                if (i == reservationList.size()-1) {
//                    //empty
//                } else {
//                    printWriter.print(Reader.DELIMITER1);
//                }
//            }
//            if (i == reservations.size() - 1) {
//                //empty
//            } else {
//                printWriter.print(Reader.DELIMITER2);
//            }
//        }
//    }
}
