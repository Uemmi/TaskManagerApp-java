import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class TaskManager {
    private List<Task> tasks;
    private String fileName;

    public TaskManager(String fileName) {
        this.fileName = fileName;
        File file = new File(fileName);
        if (!file.exists()) {
            tasks = new ArrayList<>();
            saveTasks();
        } else {
            tasks = loadTasks();
            listTasks();
        }
    }

    public TaskManager(List<Task> tasks) {
        this.tasks = tasks;
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
        for (int i = tasks.size() - 1; i >= 0; i--) {
            if (tasks.get(i).isCompleted()) {
                tasks.remove(i);
            }
        }
        listTasks();
        saveTasks();
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println((i + 1) + ". " + task.getName() + " - " + (task.isCompleted() ? "[x]" : "[]"));
            }
        }
    }
}
