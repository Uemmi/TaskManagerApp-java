import java.util.Scanner;

public class TaskManagerApp {
    private final TaskManager taskManager;

    public TaskManagerApp(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Task Manager!");

        while (true) {
            System.out.print("Enter command (add, complete, delete, delete completed, list, exit): ");
            String inputLine = scanner.nextLine().trim().toLowerCase();

            if (inputLine.startsWith("add")) {
                String taskName = inputLine.substring(4).trim();
                taskManager.addTask(new Task(taskName));
                System.out.println("Task added successfully.");
            } else if (inputLine.startsWith("complete")) {
                String input = inputLine.substring(9).trim();
                taskManager.completeTask(input);
                System.out.println("Task marked as completed.");
            } else if (inputLine.startsWith("delete completed")) {
                taskManager.deleteCompleted();
                System.out.println("Tasks deleted successfully.");    
            } else if (inputLine.startsWith("delete")) {
                String input = inputLine.substring(7).trim();
                taskManager.deleteTask(input);
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

    public static void main(String[] args) {
        String fileName = "tasks.dat";
        TaskManager taskManager = new TaskManager(fileName);
        TaskManagerApp app = new TaskManagerApp(taskManager);
        app.start();
    }
}