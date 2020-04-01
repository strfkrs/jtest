
public class Main {
    public static void main(String[] args) {
         PandemieDatenbank db = new PandemieDatenbank();
         db.add("20.12.2020",40);
         db.add("1.12.2020",22);
         db.add("1.11.2020",3);
         db.savetoFile("i.txt");
         db.loadFromFile("i.txt");
        System.out.println(db.toString());
    }
}