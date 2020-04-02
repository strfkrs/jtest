
public class Main {
    public static void main(String[] args) {
         PandemieDatenbank db = new PandemieDatenbank();
         db.add("20.12.2020",40);
         db.add("1.12.2020",22);
         db.add("1.11.2020",3);
         System.out.println("toString");
         System.out.println(db.toString());
         System.out.println("saving");
         db.savetoFile("x.bin");
         System.out.println("loading");
         db.loadFromFile("x.bin");
        System.out.println(db.toString());
    }
}