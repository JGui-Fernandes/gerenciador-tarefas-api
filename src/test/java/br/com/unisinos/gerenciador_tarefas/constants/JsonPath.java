package br.com.unisinos.gerenciador_tarefas.constants;

public class JsonPath {

    private JsonPath(){}

    public static final String ID = "$.id";
    public static final String NAME = "$.name";
    public static final String CREATEDAT = "$.createdAt";
    public static final String UPDATEDAT = "$.updatedAt";

    public static final String TASK_DESCRIPTION = "$.description";
    public static final String TASK_DEADLINE = "$.deadline";
    public static final String TASK_STATUS = "$.status";
    public static final String TASK_CREATOR = "$.creator";
    public static final String TASK_ASSIGNEE = "$.assignee";
    public static final String TASK_PARTICIPANTS = "$.participants";

    public static final String ERROR_STATUS_CODE = "$.statusCode";
    public static final String ERROR_MESSAGE = "$.message";
}
