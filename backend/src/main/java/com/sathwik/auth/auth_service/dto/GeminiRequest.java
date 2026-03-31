package com.sathwik.auth.auth_service.dto;

import java.util.List;

public class GeminiRequest {

    private List<Content> contents;

    public GeminiRequest(String prompt) {
        this.contents = List.of(new Content(prompt));
    }

    public List<Content> getContents() {
        return contents;
    }

    public static class Content {
        private List<Part> parts;

        public Content(String text) {
            this.parts = List.of(new Part(text));
        }

        public List<Part> getParts() {
            return parts;
        }
    }

    public static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}