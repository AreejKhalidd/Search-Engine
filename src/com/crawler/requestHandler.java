package com.crawler;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class requestHandler implements HttpHandler {
    static final int PORT = 4000;
    static final String HOST = "localhost";

    public requestHandler() {
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);
        server.setExecutor(Executors.newCachedThreadPool());
        server.createContext("/results", new requestHandler());
        server.start();
        System.out.println("Server started on port "+ PORT);
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
            if ("GET".equals(he.getRequestMethod())) {
                System.out.println("I got a get request!"); //
                doGet(he); // The java file received data, and it worked. the receiving part.
            }
            /*
            else if ("POST".equals(he.getRequestMethod())) {
                System.out.println("I got a post request!"); // Receiving the string from json
                doPost(he); // still working on this, but it should send data and display it on the site. i think it got something? idk.
            }
             */
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
    }

    /*
        private void doPost(HttpExchange he) throws IOException, SQLException {
            // Get the search query from the interface
            InputStream inputStream = he.getRequestBody();
            String searchQueryJSON = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // Convert to JSON
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson g = builder.create();
            ArrayList<String> sq = g.fromJson(searchQueryJSON, new TypeToken<ArrayList<String>>(){}.getType());

            //--------------------------------------------------------------------------------------------------------------
            System.out.println("searchQueryJSON = " + searchQueryJSON);
            System.out.println("sq = " + sq);
            //--------------------------------------------------------------------------------------------------------------

            // Lookup the database for relevant results
            String response = "";
            //DB here
            // sendToInterface(he, response, 0);
        }
    */

    //////////////////////////////////////// suggs
   /* private String handleSuggs(String inq) throws SQLException, ClassNotFoundException {
        //String suggs = "";
        List<String> allSuggs = new ArrayList<>();
        Database db = new Database();
        ResultSet rs = db.RetreiveSuggs(inq);
        while (true) {
            try {
                if(rs.next())
                {
                    String result = rs.getString("query");
                    allSuggs.add(result);
                }
                else if(!rs.next()){
                    break;
                }
            } catch (SQLException t) { }
        }
       String out = (new Gson()).toJson(allSuggs);
       return out;
        //return "";
    }*/



    private void doGet(HttpExchange he) throws IOException, SQLException, ClassNotFoundException {

        // Get the search query from the request parameters
        String query = he.getRequestURI().getQuery();
        System.out.println("Received query = " + query);

        // Lookup the database for suggestions
        // String query = param.substring(6, param.length());
        ArrayList<String> result;
        if (!query.isEmpty()) {
            System.out.println("Not empty!");
            Queryprocessor qp = new Queryprocessor();
            result = qp.getInput(query);
            // result = queryprocessor.finalResult;
            System.out.println("Received result = " + result + " and size " + result.size());
            int resultCount = result.size();
            if (result == null) {
                String resultString = "null";
                sendToInterface(he, result, 0);
            }
            // String resultString = "\"" + String.join("\" \"", result) + "\"";
            System.out.println("resultString = " + result);
            sendToInterface(he, result, resultCount);
        }
    }

    private void sendToInterface(HttpExchange he, ArrayList<String> response, int resultCount) throws IOException {
        OutputStream outputStream = he.getResponseBody();

        Headers headers  = he.getResponseHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Content-Type", "application/json");
        int statusCode = 200;
        he.sendResponseHeaders(statusCode, 0);

        String responseJsonText = "";
        System.out.println("sending " + response + response.size()); // Add syntax here to format the output to be json?
        if (resultCount >0) {
            responseJsonText = "{ \"results\": [ ";
            for (int i = 0; i < response.size(); i=i+4)
            {
                responseJsonText = responseJsonText + "{ \"id\": \"" + response.get(i) + "\", "
                        + "\"url\": \"" + response.get(i+1) + "\", "
                        + "\"title\": \"" + response.get(i+2) + "\", "
                        + "\"description\": \"" + response.get(i+3) + "\" } ";

                if (i+4 != response.size()) {
                    responseJsonText = responseJsonText + ",  ";
                }
            }
            responseJsonText = responseJsonText + " ] }";
        }

        outputStream.write(responseJsonText.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}