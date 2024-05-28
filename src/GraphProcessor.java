import java.security.InvalidAlgorithmParameterException;
import java.util.*;
import java.io.FileInputStream;

/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * @author Brandon Fain, Shreyans Gupta, Akshal Jain
 *
 */
public class GraphProcessor {
    private Map<String, Point> names = new HashMap<>();
    private Map<Point, Set<Point>> graph = new HashMap<>();
    
    public void initialize(FileInputStream file) throws Exception {
        int verticescount = 0;
        int edgescount = 0;

        Scanner scan = new Scanner(file);
        if(scan.hasNextInt()) 
            {verticescount = scan.nextInt();
            edgescount = scan.nextInt();}
        else
            {scan.close();
            throw new Exception("file is not formatted correctly");}
        scan.nextLine();

        Map<Integer, Point> impmap = new HashMap<>();

        for(int x = 0; x < verticescount; x++) {
            String[] loclonglat = scan.nextLine().split(" ");
            String location = loclonglat[0];                       
            Point pt = new Point(Double.parseDouble(loclonglat[1]), Double.parseDouble(loclonglat[2]));
            graph.put(pt, new HashSet<Point>());                   
            names.put(location, pt);                                  
            impmap.put(x, pt);                                      
        }
        for(int y = 0; y < edgescount; y++) {
            String[] edgelist = scan.nextLine().split(" ");           
            graph.get(impmap.get(Integer.parseInt(edgelist[0]))).add(impmap.get(Integer.parseInt(edgelist[1])));
            graph.get(impmap.get(Integer.parseInt(edgelist[1]))).add(impmap.get(Integer.parseInt(edgelist[0])));
        }
        scan.close();
    }

    public Point nearestPoint(Point p) {
        double closedist = -100;
        Point closept = null;
        for(Point ptcheck:graph.keySet()) {
            if(closedist == -100) 
                {closept = ptcheck;
                closedist = p.distance(ptcheck);
                continue;}
            if(closedist > p.distance(ptcheck)) {
                closept = ptcheck;
                closedist = p.distance(ptcheck);
            }
        }
        return closept;
    }

    public double routeDistance(List<Point> route) {
        double pathdist = 0;
        for(int x = 0; x < route.size()-1; x++) {
            pathdist = pathdist + route.get(x).distance(route.get(x+1));
        }
        return pathdist;
    }

    public boolean connected(Point p1, Point p2) {
        if(!graph.containsKey(p1))
            {return false;}
        if(!graph.containsKey(p2))
            {return false;}
        
        Stack<Point> left = new Stack<>();
        Set<Point> done = new HashSet<>();
        Point rn = p1;

        left.add(rn);

        while(!left.isEmpty()) {
            rn = left.pop();
            for(Point every:graph.get(rn)) {
                if(every.equals(p2))
                    {return true;}
                if(!done.contains(every))
                    {done.add(every);
                    left.push(every);}
            }
        }
        return false;
    }

    public List<Point> route(Point start, Point end) throws InvalidAlgorithmParameterException {
        if(start.equals(end)) {
            throw new InvalidAlgorithmParameterException("No path between start and end");
        }
        if(connected(start, end) == false) {
            throw new InvalidAlgorithmParameterException("No path between start and end");
        }

        List<Point> charted = new LinkedList<>();
        Map<Point,Point> done = new HashMap<>(algo(start));
        Point rn = end;
        charted.add(rn);

        while(!rn.equals(start)) {
            rn = done.get(rn);
            charted.add(0, rn);
        }

        return charted;
    }
    
    private Map<Point, Point> algo(Point start) {
        Map<Point, Point> done = new HashMap<>();
        Map<Point, Double> dist = new HashMap<>();
        Comparator<Point> wtcomp = (x,y) -> (int)(dist.get(x) - dist.get(y));
        PriorityQueue<Point> prique = new PriorityQueue<>(wtcomp);
        Point rn = start;
        dist.put(rn, 0.0);
        prique.add(rn);

        while(!prique.isEmpty()) {
            rn = prique.remove();
            for(Point every:graph.get(rn)) {
                double wt = rn.distance(every);

                if(!dist.containsKey(every)) {
                    dist.put(every, dist.get(rn) + wt);
                    prique.add(every);
                    done.put(every, rn);
                }

                if(dist.get(rn) + wt < dist.get(every) ) {
                    dist.put(every, wt + dist.get(rn));
                    prique.add(every);
                    done.put(every, rn);
                }
            }
        }
        return done;
    }
}


