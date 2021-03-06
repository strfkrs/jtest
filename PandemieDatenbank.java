
    import java.util.ArrayList;
    import java.io.*;
    import java.security.*;
    import java.util.*;
    import java.text.SimpleDateFormat;
    import java.util.Scanner;
    public class PandemieDatenbank
    {
            /** Datum des Eintrags */
        private String datum;
        /** Anzahl der erkrankten Personen */
        private int erkranktePersonen;
        /** Anzahl der erkrankten Personen */
        private int erkranktePersonenVortag = 0;
        /**Prozentanzahl zu Vortag*/
        private float prozent;

        private ArrayList<String> list = new ArrayList<String>();


        /**
         * Konstructor für eine leere Datenbank
         */
        public PandemieDatenbank(){}


        private boolean enthaeltListeDatum( String date ) // NEU; überprüft jeden string einer liste ob in ihm der teilstring date vorkommt
        {

           for ( String s : this.list )
           {
                if ( s.contains( date ) )
                {
                    return true;
                }
           }
            return false;
        }

        /** läd die Datenbank von einem File */
        public void loadFromFile(String filename)
        {
            this.list = new ArrayList<String>();
            //this.erkranktePersonenVortag = 0;

            try
            {
                BufferedReader br = new BufferedReader(new FileReader (filename));

                String line;

                while ( ( line = br.readLine() ) != null)
                {
                     String[] token = line.split(";");
                     this.add( token[0].replace("\"",""), Integer.parseInt( token[1] )); // nutze eigenen member damit anzahlVortag mititeriert wird
                     System.out.println(line);
                }

                br.close();
            }
            catch (IOException e)
            {
                System.out.println("FEHLER");
            }
    }

    /** speichert die Datenbank in ein File */
    public void savetoFile(String filename)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter( new FileWriter(filename, true) );

            for ( String l : this.list )
            {
                bw.write( l );
            }

            bw.close();
        }  catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * fügt einen Eintrag in die Datenbank ein
     * für ein Datum darf es nur einen Eintrag geben
     * @param datum ... Datum des Eintrags
     * @param erkranktePersonen ... Anzahl der erkrankten Personen
     */
    public void add(String datum, int erkranktePersonen) throws InvalidParameterException
    {
       try
       {
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(datum); // wirft eine exception wenn datum falsch formatiert
            if ( this.enthaeltListeDatum( datum ) )
            {
                throw new InvalidParameterException();
            }

            this.prozent = 0;
            if ( this.erkranktePersonenVortag != 0 )
            {
               this.prozent =( ( (float)erkranktePersonen/ (float)this.erkranktePersonenVortag  ) * 100 ) -100;
            }
            this.datum = "\"" + datum + "\"";
            this.erkranktePersonen = erkranktePersonen;
            this.erkranktePersonenVortag = erkranktePersonen;                            // schreibe gestern = heute

            this.list.add( String.format(Locale.ENGLISH, "%s;%d;%.2f\n", this.datum, this.erkranktePersonen, this.prozent ) ); // schreibe in liste

        } catch (Exception e) {
            throw new InvalidParameterException();
        }
    }

    /** Datenbankinhalt wird in einen String gespeichert
     * @return der komplette Datenbankinhalt im vorgegeben Format .
     * Format:
     * "<Datum>";<anzahl erkrankte Personen>;<Zunahme in % gegenüber dem Vortag>                               // TODO: hier stehen ; im beispiel , --> ; implementiert
     *
     * Beispielausgabe:
     * "15.3.2020",860,0
     * "16.3.2020",1018,18.38
     * "17.3.2020",1332,30.85
     * "18.3.2020",1646,19.07
     * "19.3.2020",2013,22.29
     * "20.3.2020",2388,18.62
     * "21.3.2020",2814,17.84
     * "22.3.2020",3282,16.63
     *
    */

    public String toString() {

        String s = "";
        for ( String ds : this.list )
        {
            s += ds;
        }
        return s;
    }

}