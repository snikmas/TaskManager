package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpTaskServer {

    HttpServer httpServer;


    public void runServer() throws Exception{

        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);


        // here: tasks/<resourses>, like task
        // tasks/task/create OR delete OR change? - for tasks GET
        // tasks/subtask/ - subtask GET
        // /tasks - get all tasks GET
        // tasks/history - get history GET
        // for tasks/create/change - POST requiest
        // tasks/delete - DELETE
        // parameters put to the body iin json format, in the request use req?taskId

        httpServer.createContext("/tasks", new handleMenu());
        httpServer.start();
//        URI uri1 = URI.create("https://localhost:8080/tasks/task"); // ye.. no need

        // get?
        // get tasks
        // get by id
    }
}

// rules:
// create a server
// bind a port
// create a context: link/end point + handler
// start server
// + write handler
// use httpexchange to send reponse headres + output stream write to send a body
class handleMenu implements  HttpHandler{
    @Override
    // tasks
    // here: servers thing
    public void handle(HttpExchange httpExchange) throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        // GET tasks/task/
        // GET tasks/task/?id=
        // GET tasks/subtask/epic?id=
        // GET tasks/tasks/history
        // GET tasks/ - by priority
        // POST tasks/task/body:{task}
        // DELETE  tasks/task/?id
        // DELETE  tasks/task/
        // tasks/subtasks or tasks/epic


        URI uri = httpExchange.getRequestURI();
        String method = httpExchange.getRequestMethod();
        String path = uri.getPath(); // path go there without any parameters
        String params = uri.getQuery();
        String[] pathParts = path.split("/");
        String response = "";

        // 1st case: if params == 0'
        if(params == null) {
            // tasks handler, shows tasks
            StringBuilder output = new StringBuilder();
            List<Task> tasks = Managers.getDefault().getAllTasks();
            for(Task task : tasks){
                output.append(gson.toJson(task)).append('\n');
            }
        }


        switch(method){
            case "GET" -> {
                httpExchange.sendResponseHeaders(200, 0);

                if(params != null){
                    System.out.println("params in not null");
                    return;
                }

                if(pathParts.length == 2){
                    // shows by priority
                    // it returns a list.. list to.. json?

                    System.out.println("working");
                    List<Task> list = Managers.getDefault().getTaskByPriority();
                    String jsonList = gson.toJson(list);
                    byte[] resBytes = jsonList.getBytes(StandardCharsets.UTF_8);
                    httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                    httpExchange.sendResponseHeaders(200, resBytes.length);
                    try(OutputStream os = httpExchange.getResponseBody()){
                        os.write(resBytes);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

                else System.out.println("params is null");
                System.out.println("path boejcts: " + pathParts.length);
                System.out.println("path[0]: " + pathParts[0]);
                System.out.println("path[1]: " + pathParts[1]);
                System.out.println("path[2]: " + pathParts[2]);


                System.out.println(path);
                String str = "434";

            }
            case "POST" -> {
                System.out.println(path);

            }
            case "DELETE" -> {
                System.out.println(path);
            }
        }


        







    }
}
