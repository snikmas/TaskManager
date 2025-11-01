package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Utils.Utils;
import com.google.gson.Gson;
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
import java.util.List;

public class HttpTaskServer {

    HttpServer httpServer;

    public void runServer(){
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
    public void handle(HttpExchange httpExchange){

        String method = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();

        // log request
        System.out.println(method + " " + path);

        // /tasks or /tasks/123
        String[] pathParts = path.split("/");
        String response = "";

        Long taskId = -1L;

        try {
            // get if there're only one task
            if(method.equalsIgnoreCase("GET")){
                if(pathParts.length == 2){
                    response = Utils.outputAllTasks(new ArrayList<>(Managers.getDefault().getAllTypesTasks().values()));
                }else if(pathParts.length > 2){
                    String queries = requestURI.getQuery();
                    if(queries != null){
                        String pairs[] = queries.split("=");
                        if(pairs.length > 1){
                            taskId = Long.parseLong(pairs[1]);
                        }
                        response = Utils.outputTaskInfo(Managers.getDefault().getTaskById(taskId));
                    }
                } else {
                    response = "Invalid request!";
                }
                httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            } else if(method.equalsIgnoreCase("DELETE")){
                if(pathParts.length > 2){
                    String queries = requestURI.getQuery();
                    if(queries != null){
                        String[] pairs = queries.split("=");
                        if(pairs.length > 1){
                            taskId = Long.parseLong(pairs[1]);
                        }
                        Managers.getDefault().deleteTaskById(taskId);
                    }
                }
            } else if (method.equalsIgnoreCase("POST")){
                String typeTask = pathParts[2];
                // new update delete? here only new and update
                String action = pathParts[3];
                if(action.equals("new")){
                    Task task = null;
                    switch (typeTask){
                        case "task" -> {
                            task = new Task();
                        }
                        case "subtask" -> {
                            task = new Subtask();
                        }
                        case "epic" -> {
                            task = new Epic();
                        }
                    }
                    Managers.getDefault().createTask(task);
                } else if(action.equals("update")){
                    String queries = requestURI.getQuery();
                    System.out.println(queries);
                    if(queries != null){
                        String[] pairs = queries.split("=");
                        if(pairs.length > 1){
                            taskId = Long.parseLong(pairs[1]);
                        }
                    }
                    Task task = Managers.getDefault().getTaskById(taskId);
                    Managers.getDefault().updateTask(task, taskId);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        // get the second one /subtask-?
        // with this thing, get method. get/post etc
        // tasks/task/new?id=1
        // tasks/task/update?id=1
        // tasks/task/deletee?id=1
    }
}
