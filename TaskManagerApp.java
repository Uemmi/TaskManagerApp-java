import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private boolean completed;

    public Task(String name) {
        this.name = name;
        this.completed = false;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markAsCompleted() {
        completed = true;
    }
}

class TaskManager {
    private List<Task> tasks;
    private String fileName;

    public TaskManager(String fileName) {
        this.fileName = fileName;
        tasks = loadTasks();
        listTasks();
    }

    private List<Task> loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(tasks);
            System.out.println("Tasks saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
        listTasks();
        saveTasks();
    }

    public void completeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task completedTask = tasks.remove(index);
            completedTask.markAsCompleted();
            tasks.add(completedTask);
            listTasks();
            saveTasks();
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            listTasks();
            saveTasks();
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void deleteCompleted() {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isCompleted()) {
                tasks.remove(i);
                i--;
            }
        }
        listTasks();
        saveTasks();
    }

    public void listTasks() {
        System.out.println("------ Tasks ------");
        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println(
                        (i + 1) + ". " + task.getName() + " - " + (task.isCompleted() ? "Completed" : "Incomplete"));
            }
        }
        System.out.println("-------------------");
    }
}

public class TaskManagerApp {
    public static void main(String[] args) {
        String fileName = "tasks.dat";
        TaskManager taskManager = new TaskManager(fileName);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Task Manager!");

        while (true) {
            System.out.print(
                    "Enter command (add TaskName, complete TaskIndex, delete TaskIndex, delete completed, list, exit): ");
            String inputLine = scanner.nextLine().trim().toLowerCase();

            if (inputLine.startsWith("add")) {
                String taskName = inputLine.substring(4).trim();
                taskManager.addTask(new Task(taskName));
                System.out.println("Task added successfully.");
            } else if (inputLine.startsWith("complete")) {
                int index = Integer.parseInt(inputLine.substring(9).trim()) - 1;
                taskManager.completeTask(index);
                System.out.println("Task marked as completed.");
            } else if (inputLine.startsWith("delete completed")) {
                taskManager.deleteCompleted();
                System.out.println("Task deleted successfully.");
            } else if (inputLine.startsWith("delete")) {
                int index = Integer.parseInt(inputLine.substring(7).trim()) - 1;
                taskManager.deleteTask(index);
                System.out.println("Task deleted successfully.");
            } else if (inputLine.startsWith("list")) {
                taskManager.listTasks();
            } else if (inputLine.equals("exit")) {
                taskManager.saveTasks(); // Save tasks before exiting
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }

        scanner.close();
    }
}
