package utils;

public class Task {
     private String taskDetail;
     private String id;
     private String Completed;

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompleted() {
        return Completed;
    }

    public void setCompleted(String completed) {
        this.Completed = completed;
    }

    public Task(){

    }
    public Task(String taskDetail, String id, String completed) {
        this.taskDetail = taskDetail;
        this.id = id;
        Completed = completed;
    }


}
