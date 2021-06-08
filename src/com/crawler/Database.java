package com.crawler;
//import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {
    private static  Connection Conn ;
    private static AtomicInteger increment= new AtomicInteger(1);
    private int id; //id

    public Database() throws SQLException, ClassNotFoundException {
        //Conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/prooooooooj","root",null);
        Class.forName("com.mysql.jdbc.Driver");
        Conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/crawler_2?characterEncoding=latin1&useConfigs=maxPerformance","root",null);
if(Conn==null){System.out.println("eroooooooooooooooooor");}


    }

    public boolean isAddedURLInPagesTable(String U) throws SQLException {
        try{
            String q = "SELECT * from `pages_table` WHERE `url`  = ? ";
            PreparedStatement st = Conn.prepareStatement(q);
            st.setString(1,U);
            ResultSet rs = st.executeQuery();
            System.out.println("Is visited");
            return rs.next();
        } catch (SQLException e)
        {
            return true;
        }

    }

    public synchronized boolean InsertPageToPagesTable(String URL,String d) throws SQLException {

        // System.out.println("madkhlshhhhhhh");
        if(! isAddedURLInPagesTable(URL)){
            System.out.println("gowa iffffffffffff");
            String q = "INSERT INTO `pages_table` ( `url`, `doc`) VALUES (? ,?)";
            PreparedStatement st = Conn.prepareStatement(q);
            //this.id=increment.incrementAndGet();
            // st.setInt(1,this.id);
            //System.out.println("Insert ID");
            st.setString(1,URL);
            System.out.println("Insert URL");
            st.setString(2, d);
            System.out.println("Insert doc");
            System.out.println("before exeeeeee");
            st.execute();
            System.out.println("exeeeeeeeeeeeeeeeee");
            System.out.println("Done inserting new page into the table");

            return true;
        }
        else
        {
            // URL of this page already added
            return false;
        }
    }


    public static String getByDoc(String u){
        try {
            //"SELECT * from `pages_table` WHERE `Url`  = ? ";
            String q = "SELECT doc FROM `pages_table` WHERE `url` = ? ";
            PreparedStatement st = Conn.prepareStatement(q);
            st.setString(1,u);
            String doc = null;
            ResultSet rs = st.executeQuery();

            if (rs.next())
                doc = rs.getString("doc");

            return doc;
        }
        catch (Exception e) {
            return "";
        }
    }



    public int countPagesInPagesTable() throws SQLException { //count rows
        System.out.println("countPagesInPagesTable");
        try {
            String q = "SELECT COUNT(*) FROM `pages_table`";
            PreparedStatement ps = Conn.prepareStatement(q);
            ResultSet rs = null;
            rs = ps.executeQuery();
            rs.next();
            int c = rs.getInt(1);
            return c;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void InsertUrlsTable(String u){ //backup
        System.out.println("InsertUrlsTable");
        try{
            String q = "INSERT INTO `urls_table` (`id`, `url`) VALUES (NULL,?)"+
                    "ON DUPLICATE KEY UPDATE url = ?";
            PreparedStatement ps = Conn.prepareStatement(q);
            ps.setString(1,u);
            ps.setString(2,u);
            ps.execute();
        }  catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException){
            }
            else {
                e.printStackTrace();
            }
        }
    }

    public int UrlsTableCount(){ // pages backup count
        System.out.println("UrlsTableCount");

        try {
            String q = "SELECT COUNT(*) FROM `urls_table`";
            PreparedStatement ps = Conn.prepareStatement(q);
            ResultSet rs = null;
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean PageInUrlsTable(String url){
        System.out.println("PageInUrlsTable");
        try{
            String q = "SELECT * FROM `urls_table` WHERE url = ?";
            PreparedStatement ps = Conn.prepareStatement(q);
            ps.setString(1,url);
            ResultSet rs=ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return true;
        }
    }

    public ResultSet GetPageFromUrlsTable(){
        System.out.println("GetPageFromUrlsTable ");
        try{
            String q = "SELECT * FROM `urls_table`";
            PreparedStatement ps = Conn.prepareStatement(q);
            return  ps.executeQuery();

        } catch (SQLException e) {
            return null;
        }
    }

    public void DeleteUrlFromUrlsTable (String u){
        System.out.println("DeleteUrlFromUrlsTable ");
        try{
            String q = "DELETE FROM `urls_table` WHERE `url` = ?";
            PreparedStatement ps = Conn.prepareStatement(q);
            ps.setString(1,u);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public void RemoveTables() throws SQLException {
        System.out.println("Removeeeeeeeeeeeeeee");
        String q = "DELETE FROM `pages_table`";
        String qq = "DELETE FROM `urls_table`";
        PreparedStatement ps=Conn.prepareStatement(q);
        ps.execute();
        PreparedStatement pss=Conn.prepareStatement(qq);
        pss.execute();

    }
    /////////////////////////////////////suggs
    public static void insertNewSugg(String in){
        try{
            String newq = "INSERT INTO `Suggs` `query`=?";
            PreparedStatement st=Conn.prepareStatement(newq);
            st.setString(1,in);
            st.execute();
        } catch (SQLException ee) {
            if (ee instanceof SQLIntegrityConstraintViolationException){
            }
            else { }
        }

    }


    public ResultSet RetreiveSuggs(String input) {
        try {
            String inquery = "SELECT * FROM `Suggs` WHERE value LIKE ? LIMIT 3";
            PreparedStatement st = Conn.prepareStatement(inquery);
            st.setString(1,input+"%");
            ResultSet suggs = st.executeQuery();
            return suggs;
        } catch (SQLException q) {
            return null;
        }
    }
    ///////////////////////////////////////////////////////
//Indexer queries
public synchronized boolean insert_url(String query){ ///check????????????//
      //  int x1=0;
    try{
        String SQL = "INSERT INTO `url` (`link`,`startIndexing`,`endIndexing`) VALUE (?,?,?)";
        PreparedStatement ps = Conn.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
        ps.setString( 1, query );
        ps.setInt( 2, 0);
        ps.setInt( 3, 0 );
        ps.executeUpdate();
        System.out.print("Inserted Successfully\n");
        return true;
    }
    catch(SQLException ex){
        System.out.print("Insertion Failed "+ex);
        return false;
    }
}

    static void set1stIndex(String url) throws SQLException {
        try {
            String SQL = "UPDATE url set startIndexing=1 WHERE link=?";
            PreparedStatement ps = Conn.prepareStatement(SQL);
            ps.setString(1, url);
            ps.executeUpdate();
        }
        catch (Exception e) {

        }
    }

    static public String get1stUnindex() throws SQLException {
        try {
            String SQL = "SELECT link FROM url WHERE endIndexing=0 and "
                    + "startIndexing=0 LIMIT 1";
            PreparedStatement ps = Conn.prepareStatement(SQL);
            String url = null;
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                url = rs.getString("link");

            return url;
        } catch (Exception e) {
            return "";

        }
    }

    static public void insertWords(ArrayList<Word> searchList, String url,
                                   String title, String desc, Integer wordCounter) throws SQLException {
        String SQL = "INSERT INTO indexing (url, word, title, descrip, header1, header2, header3, "
                + "header4, header5, header6, par, tiTag) VALUES ";
        for(int i=0; i<searchList.size(); i++) {
            SQL=SQL+"(?,?,?,?,?,?,?,?,?,?,?,?)";
            if(i!=searchList.size()-1)
                SQL=SQL+",";
        }
        PreparedStatement ps = Conn.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
        int size = searchList.size()*12;
        int count = 0;
        for(int i=1; i<=size; i++) {
            switch(i%12) {
                case 1: {
                    ps.setString(i, url);
                    break;
                }
                case 2: {
                    ps.setString(i, searchList.get(count).word);
                    break;
                }
                case 3: {
                    ps.setString(i, title);
                    break;
                }
                case 4: {
                    ps.setString(i, desc);
                    //System.out.print("j");
                    break;
                }
                case 5: {
                    ps.setInt(i, searchList.get(count).header1);
                    break;
                }
                case 6: {
                    ps.setInt(i, searchList.get(count).header2);
                    break;
                }
                case 7: {
                    ps.setInt(i, searchList.get(count).header3);
                    break;
                }
                case 8: {
                    ps.setInt(i, searchList.get(count).header4);
                    break;
                }
                case 9: {
                    ps.setInt(i, searchList.get(count).header5);
                    break;
                }
                case 10: {
                    ps.setInt(i, searchList.get(count).header6);
                    break;
                }
                case 11: {
                    ps.setInt(i, searchList.get(count).par);
                    break;
                }
                case 0: {
                    ps.setInt(i, searchList.get(count).title);
                    count++;
                    break;
                }
            }
        }
        ps.executeUpdate();
      //  SQL = "UPDATE url SET count =? WHERE link=? ";
      //  ps = Conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
      //  ps.setInt(1, wordCounter);
      //  ps.setString(2, url);
       // ps.executeUpdate();
    }

    static public void setLastIndex(String url) throws SQLException {
        try {
            String SQL = "UPDATE url set endIndexing = 1 WHERE link=?";
            PreparedStatement ps = Conn.prepareStatement(SQL);
            ps.setString(1, url);
            ps.executeUpdate();
        }
        catch (Exception e) {
        }
    }

    static public void incID() throws SQLException {
        try {
            String SQL = "SET @a = -1";
            PreparedStatement ps = Conn.prepareStatement(SQL);
            ps.executeUpdate();
            SQL = "UPDATE url SET id = @a:=@a+1";
            ps = Conn.prepareStatement(SQL);
            ps.executeUpdate();
        }catch (Exception e) {
        }
    }

    static public void removeUnIndexed() throws SQLException {
        String SQL="SELECT Link FROM url WHERE endIndexing = 0 and startIndexing = 1 LIMIT 6";
        PreparedStatement ps = Conn.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
        ResultSet rs = ps.executeQuery();
        String url = null;
        if(rs.next())
            url = rs.getString(1);

        if(url==null)
            return;

        SQL = "UPDATE url set startIndexing=0 WHERE endIndexing=0 and startIndexing=1";
        ps= Conn.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
        ps.executeUpdate();
    }




    //////////////////////// query
    public static boolean isSearchQueryExist(ArrayList<String> stemQuery) throws SQLException {
        String query = stemQuery.get(0);
        for (int i = 0; i < stemQuery.size() - 1; i++)
            query = query + " " + stemQuery.get(i + 1);
        String SQL = "SELECT * FROM indexing WHERE word=? LIMIT 1";
        PreparedStatement ps = Conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, query);
        ResultSet rs = ps.executeQuery();
        String checkString = "";
        if (rs.next()) {
            checkString = rs.getString(1);
        }
        if (checkString.isEmpty())
            return false;
        else
            return true;
    }

    public static  ArrayList<String> getQueryResult(ArrayList<String> stemQuery) throws SQLException {
        ArrayList<String> result = new ArrayList();
        String query = stemQuery.get(0);
        for(int i=0; i<stemQuery.size()-1; i++)
            query = query + " " + stemQuery.get(i+1);
        String SQL ="SELECT * FROM indexing WHERE word = ?";
        PreparedStatement ps= Conn.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
        ps.setString(1, query);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) { // Change below numbers during integration
            result.add(rs.getString(1)); // id
            // result.add(rs.getString(2)); // query
            result.add(rs.getString(2)); // url
            result.add(rs.getString(4)); // title
            result.add(rs.getString(5)); // description
        }
        // System.out.println(result);

        return result;
    }
    //// SAVE QUERIESSS..........


  /*  static public void removeUnIndexed() throws SQLException {
        String SQL="SELECT Link FROM url WHERE endIndexing = 0 and startIndexing = 1 LIMIT 6";
        PreparedStatement ps = Conn.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
        ResultSet rs = ps.executeQuery();
        String url = null;
        if(rs.next())
            url = rs.getString(1);

        if(url==null)
            return;

        SQL = "UPDATE url set startIndexing=0 WHERE endIndexing=0 and startIndexing=1";
        ps= Conn.prepareStatement( SQL, Statement.RETURN_GENERATED_KEYS );
        ps.executeUpdate();
    }*/

    /*static public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            Conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crawler-2?useSSL=false"
                    ,"root","");
            if (Conn != null)
                System.out.println("Connected...");

        } catch(ClassNotFoundException | SQLException ex) {
            System.out.print("Not Connected "+ex);
        }
    }*/
















   /* public static void main(String[] args) throws InterruptedException, SQLException, ClassNotFoundException {

//Class.forName("com.mysql.jdbc.Driver");

Database db=new Database();
        Database db2=new Database();
String url="wwwhellojjj1111dfg";

String title="title55551";
String content="conttt1";
int date=1;
String country="egypt1";
String meta="metaaa1";
String alt="alttt1";
        String h1="header11";
        String h2="header21";
        String h3="header31";
        String h4="header41";
        String h5="header51";
        String h6="header61";


//db.InsertPageToPagesTable(url,title,content,date,country,alt,meta,h1,h2,h3,h4,h5,h6);

db2.InsertPageToPagesTable(url,title,content,date,country,alt,meta,h1,h2,h3,h4,h5,h6);
        db2.InsertPageToPagesTable("aaaaaaaaaa",title,content,date,country,alt,meta,h1,h2,h3,h4,h5,h6);
        db2.InsertPageToPagesTable("aaaaaaa",title,content,date,country,alt,meta,h1,h2,h3,h4,h5,h6);

       }
*/



}
