
    import java.util.ArrayList;
    import java.io.*;
    import java.security.InvalidParameterException;
    import java.util.*;
    import java.text.SimpleDateFormat;
    import java.lang.StringBuilder;

    public class PandemieDatenbank
    {
        /** Datum des Eintrags */
        //private String datum;
        /** Anzahl der erkrankten Personen */
        //private int erkranktePersonen;
        /** Anzahl der erkrankten Personen */
        //private int erkranktePersonenVortag = 0;
        /**Prozentanzahl zu Vortag*/
        //private float prozent;

         static public class Datensatz implements Serializable
         {
            private static final long serialVersionUID = 1234L; // beim de/serialisieren hilft das java versionen zu unterscheiden
            public Date datum; // datum des datensatzes
            public int erkranktePersonen; // summe am tag des datensatzes
            public static int erkranktePersonenVortag; // static!

            @Override
            public String toString()
            {
               return String.format( "\"%s\";%d;%.2f",
                                     new SimpleDateFormat( "dd.MM.yyyy" ).format( this.datum ),
                                     this.erkranktePersonen,
                                     ( Datensatz.erkranktePersonenVortag > 0 )
                                       ? ( ( (float) this.erkranktePersonen / (float) Datensatz.erkranktePersonenVortag ) * 100 ) -100
                                       : 0
                                   );
            }
         }

        private ArrayList<Datensatz> list = new ArrayList<Datensatz>();

        public PandemieDatenbank(){}

        public void loadFromFile(String filename)
        {
            this.list.clear();

            try( FileInputStream fs = new FileInputStream(filename);
                 ObjectInputStream is = new ObjectInputStream( fs ); )
            {
               while( true )
               {
                  this.list.add( (Datensatz) is.readObject() );
               }
            }
            catch( EOFException e )
            {
               // System.out.println("OK");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("FEHLER - fehlerhafte daten" +e);
            }
            catch (IOException e)
            {
                System.out.println("FEHLER - konnte datei nicht öffnen" + e);
            }
    }

    public void savetoFile(String filename)
    {
        try( FileOutputStream fs = new FileOutputStream( filename, false );
             ObjectOutputStream os = new ObjectOutputStream( fs ); )
        {
            for ( Datensatz datensatz : this.list )
            {
               os.writeObject( datensatz );
               System.out.println( datensatz );
            }
        }  catch ( IOException e ) {
            System.out.print(String.format("%s %s", e.getMessage(), e.getCause() ));
        }
    }

    public void add(String datum, int erkranktePersonen) throws InvalidParameterException
    {
       try
       {
            Datensatz neuerDatensatz = new Datensatz();

            neuerDatensatz.datum = new SimpleDateFormat("dd.MM.yyy").parse( datum ); // objekt wirft exception wenns format beim parsen nicht zampasst

            this.list.forEach( existierenderEintrag ->
                     { if ( existierenderEintrag.datum == neuerDatensatz.datum ) throw new InvalidParameterException(); } );

            neuerDatensatz.erkranktePersonen = erkranktePersonen;

            this.list.add( neuerDatensatz );
            System.out.println( neuerDatensatz );

        } catch ( Exception e ) {
            throw new InvalidParameterException();
        }
    }

    public String toString() {

        // verwende stringbuilder um die einzelnen tage zu verketten und zum schluss einen gesamtstring zurückzugeben
        // ist performanter als händisch mit +=
        StringBuilder builder = new StringBuilder();

        for ( Datensatz datensatz : this.list )
        {
            builder.append( datensatz );
            builder.append( '\n' );
        }
        return builder.toString();
    }

}