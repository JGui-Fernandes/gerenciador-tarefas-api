package br.com.unisinos.gerenciador_tarefas.constants;

public final class ErrorMessages {

    private ErrorMessages(){}

    public static final String INVALID_EMAIL = "O campo email é inválido";
    public static final String INVALID_BIRTH_DATE = "O campo data de nascimento é inválido";
    public static final String INVALID_PHONE_NUMBER = "O campo telefone é inválido";
    public static final String INVALID_ROLE = "Role inválida";

    public static final String INVALID_DEADLINE = "O campo data de expiração é inválido";
    public static final String INVALID_STATUS = "Status inválido";
    public static final String ASSIGNEE_NOT_FOUND = "Usuário responsável não encontrado";
    public static final String PARTICIPANT_NOT_FOUND = "Um ou mais participantes não foram encontrados";

    public static final String REUSED_EMAIL = "Este email já está sendo utilizado";
    public static final String REUSED_PHONE = "Este telefone já está sendo utilizado";

    public static final String FILL_ALL_FIELDS = "Preencha todos os campos obrigatórios";

    public static final String UNLOGGED_USER = "Apenas usuários logados podem consultar essa informação";
    public static final String NOT_ALLOWED_USER = "Você não tem acesso à essas informações";

    public static final String USER_NOT_FOUND_BY_ID = "Nenhum usuário encontrado com esse ID";
    public static final String NO_REGISTERED_USER = "Nenhum usuário cadastrado";

    public static final String TASK_NOT_FOUND_BY_ID = "Nenhuma tarefa encontrada com esse ID";
    public static final String TASK_NOT_FOUND_BY_USER = "Nenhuma task encontrada para este usuário";

    public static final String INVALID_LOGIN = "Email ou senha inválidos";

    public static final String INVALID_TOKEN = "Token inválido ou expirado";

}
