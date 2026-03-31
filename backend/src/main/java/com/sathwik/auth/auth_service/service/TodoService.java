package com.sathwik.auth.auth_service.service;

import com.sathwik.auth.auth_service.entity.TodoEntity;
import com.sathwik.auth.auth_service.repository.TodoRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoRepository todoRepo;
    private final AiService aiService;

    public TodoService(TodoRepository todoRepo,
                       AiService aiService) {
        this.todoRepo = todoRepo;
        this.aiService = aiService;
    }

    public TodoEntity updateTodoWithAI(TodoEntity todo,boolean fetchAi) {

        // 🔥 AI Logic Here
        if (todo.isAiEnabled() && fetchAi) {

            String prompt = """
You are an intelligent productivity assistant.

A user has created the following todo:

Title:
%s

Description:
%s

Your task is to generate a structured AI summary that adds value beyond the original title and description.

The summary must:

above all if it is a simple data fetching task fetch and give the data to user nothing else need to be done apart from this.
1. Clearly explain what this task likely involves.
2. Break it into actionable sub-steps if applicable.
3. Mention important concepts related to the task.
4. Highlight common mistakes or things to be careful about.
5. Suggest useful tools, techniques, or best practices.
6. Keep it concise but information-dense.
7. Write in clear, simple language so the user can quickly understand it later.

Do NOT repeat the original title and description verbatim.
Add meaningful expansion and practical insight.

Format the output in clean bullet points or short sections.
""".formatted(todo.getTitle(), todo.getDescription());
            String generatedContent =
                    aiService.askLLM(prompt);

            todo.setAiContent(generatedContent);
        }
        return todoRepo.save(todo);
    }
}