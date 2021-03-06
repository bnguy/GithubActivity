package ecs189.querying.github;

import ecs189.querying.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 10/1/2017.
 */
public class GithubQuerier {

    private static final String BASE_URL = "https://api.github.com/users/";

    public static String eventsAsHTML(String user) throws IOException, ParseException {
        List<JSONObject> response = getEvents(user);
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        int i = 0;
        for (JSONObject event : response) {
            // Get event type
            String type = event.getString("type");
            // Get created_at date, and format it in a more pleasant style
            String creationDate = event.getString("created_at");
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
            Date date = inFormat.parse(creationDate);
            String formatted = outFormat.format(date);
            ArrayList<String> commitList = new ArrayList<String>();
            JSONArray listCommits = event.getJSONObject("payload").getJSONArray("commits");
            for(int j = 0; j < listCommits.length(); j++){
                commitList.add(listCommits.getJSONObject(j).getString("sha") + " " + listCommits.getJSONObject(j).getString("message"));
            }

            // Add type of event as header
            sb.append("<h3 class=\"type\">");
            sb.append(type);
            sb.append("</h3>");
            // Add formatted date
            sb.append(" on ");
            sb.append(formatted);
            sb.append("<br />");

            for(int j = 0; j < listCommits.length(); j++){
                sb.append(commitList.get(j));
                sb.append("<br />");
            }

            // Add collapsible JSON textbox (don't worry about this for the homework; it's just a nice CSS thing I like)
            sb.append("<a data-toggle=\"collapse\" href=\"#event-" + i + "\">JSON</a>");
            sb.append("<div id=event-" + i + " class=\"collapse\" style=\"height: auto;\"> <pre>");
            sb.append(event.toString());
            sb.append("</pre> </div>");

        }
        sb.append("</div>");
        return sb.toString();
    }

    private static List<JSONObject> getEvents(String user) throws IOException {
        int typeCount = 0;
        int pageCount = 1;
        List<JSONObject> eventList = new ArrayList<JSONObject>();

        while (typeCount < 10){
            String url = BASE_URL + user + "/events?page=" + pageCount;
            System.out.println(url);
            JSONObject json = Util.queryAPI(new URL(url));
            System.out.println(json);
            JSONArray events = json.getJSONArray("root");
            for (int i = 0; i < events.length() && typeCount < 10; i++) {
                if ((events.getJSONObject(i)).getString("type").equals("PushEvent")) {
                    typeCount++;
                    eventList.add(events.getJSONObject(i));
                }
            }
        }

        return eventList;
    }
}