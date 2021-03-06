package com.anson.jupiter.servlet;

import com.anson.jupiter.external.TwitchClient;
import com.anson.jupiter.external.TwitchException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;


/**
 * restful three requirements
 * 1. doPost, doGet
 * 2. Implements specific function depending on specific URL
 * 3. No relation between different requests
 */
@WebServlet(name = "GameServlet", urlPatterns = {"/game"})
public class GameServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Using JSONObject to convert input stream to JSON format
    // 1. get data stream
    // 2. content stream to string
    // 3. convert string to json by JSONObject
    JSONObject jsonRequest = new JSONObject(IOUtils.toString(request.getReader()));

    String name = jsonRequest.getString("name");
    String developer = jsonRequest.getString("developer");
    String releaseTime = jsonRequest.getString("release_time");
    String website = jsonRequest.getString("website");
    float price = jsonRequest.getFloat("price");

    // print in console
    System.out.println("Name is: " + name);
    System.out.println("Developer is: " + developer);
    System.out.println("Release time is: " + releaseTime);
    System.out.println("Website is: " + website);
    System.out.println("Price is: " + price);

    // return JSON format to front-end
    response.setContentType("application/json");

    JSONObject jsonResponse = new JSONObject();
    jsonResponse.put("status", "ok");
    response.getWriter().print(jsonResponse);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
//    solution 1
//    String gameName = request.getParameter("gamename");
//    response.getWriter().print("Game is: " + gameName);

//    solution 2
//    return JSON format to front-end
//    response.setContentType("application/json");
//
//    JSONObject game = new JSONObject();
//    game.put("name", "World of Warcraft");
//    game.put("developer", "Blizzard Entertainment");
//    game.put("release_time", "Feb 11, 2005");
//    game.put("website", "https://www.worldofwarcraft.com");
//    game.put("price", 49.99);
//    response.getWriter().print(game);

//    solution 3: convert java object to JSON object via Jackson, it simplify solution 1
//    response.setContentType("application/json");
//    ObjectMapper mapper = new ObjectMapper();
//    Game game = new Game("World of Warcraft", "Blizzard Entertainment",
//        "Feb 11, 2005", "https://www.worldofwarcraft.com", 49.99);
//    response.getWriter().print(mapper.writeValueAsString(game));

    String gameName = request.getParameter("game_name");
    TwitchClient client = new TwitchClient();

    response.setContentType("application/json;charset=UTF-8");
    try {
      if (gameName != null) {
        response.getWriter()
            .print(new ObjectMapper().writeValueAsString(client.searchGame(gameName)));
      } else {
        response.getWriter().print(new ObjectMapper().writeValueAsString(client.topGames(0)));
      }
    } catch (TwitchException e) {
      throw new ServletException(e);
    }
  }
}
