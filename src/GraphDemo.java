/**
 * Demonstrates the calculation of shortest paths in the US Highway
 * network, showing the functionality of GraphProcessor and using
 * Visualize
 * To do: Add your name(s) as authors
 * @author Akshal Jain, Shreyans Gupta
 */

 import java.io.FileInputStream;
 import java.util.*;
 
 public class GraphDemo {
     public static void main(String[] args) throws Exception {
         GraphProcessor graph = new GraphProcessor();
         Scanner scan = new Scanner(System.in);
         graph.initialize(new FileInputStream("data/usa.graph"));

         System.out.println("Enter your city of departure: ");
         String departure = scan.nextLine();
         double[] cord = locate(new FileInputStream("data/uscities.csv"), departure);
         Point deppoint = new Point(cord[0], cord[1]);

         System.out.println("\nEnter your city of arrival: ");
         String arrival = scan.nextLine();
         cord = locate(new FileInputStream("data/uscities.csv"), arrival);
         Point arrpoint = new Point(cord[0], cord[1]);

         //for timer
         double timer = System.nanoTime();

         Point nearestdep = graph.nearestPoint(deppoint);
         Point nearestarr = graph.nearestPoint(arrpoint);
         List<Point> route = graph.route(nearestdep, nearestarr);
         double finaldist = graph.routeDistance(route);

         double timetaken = System.nanoTime();
         double finaltime = (timetaken - timer)/1e6;

         System.out.println("The route between your two cities is " + finaldist + " miles long");
         System.out.println("It took " + finaltime + " milliseconds to calculate the closest points, shortest path, and distance along the path");
         
         //drawing all requirements
         Visualize visualize = new Visualize("data/usa.vis","images/usa.png");
         visualize.drawPoint(deppoint);
         visualize.drawPoint(arrpoint);
         visualize.drawRoute(route);
     }
 
     public static double[] locate(FileInputStream file, String location) throws Exception {
         double[] csvfind = new double[2];
         Scanner read = new Scanner(file);
         
         while(read.hasNextLine()) {
             String[] cityname = read.nextLine().split(",");
             
             if(cityname[0].toLowerCase().equals(location.split(" ")[0].toLowerCase()))  {
                if(cityname[1].toLowerCase().equals(location.split(" ")[1].toLowerCase())) {
                 csvfind[0] = Double.parseDouble(cityname[2]);
                 csvfind[1] = Double.parseDouble(cityname[3]);

                 return csvfind; 
                }
             }
         }

         throw new Exception("This city is not in the database");
     }
 }