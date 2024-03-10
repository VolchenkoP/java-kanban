
public class Subtask extends Task {

    private int epicIdForThisSubtask;
    public Subtask(String name, String description) {
        super(name, description, Status.NEW);
        epicIdForThisSubtask = 0;
    }

    public void setEpicIdForThisSubtask(int id) {
        if (epicIdForThisSubtask == 0){
            epicIdForThisSubtask = id;
        } else {
            System.out.println("У данной подзадачи уже есть Эпик");
        }
    }

    public int getEpicIdForThisSubtask() {
        return epicIdForThisSubtask;
    }

    public void deleteEpicId() {
        if (epicIdForThisSubtask != 0) {
            epicIdForThisSubtask = 0;
        } else {
            System.out.println("У данной подзадачи нет Эпика");
        }
    }
}
