package br.com.unisinos.gerenciador_tarefas.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {

    private static final ObjectMapper mapper =
            new ObjectMapper()
                    .registerModule(
                            new JavaTimeModule()
                    );

    public static String toJson(Object object)
            throws Exception {

        return mapper.writeValueAsString(object);
    }
}