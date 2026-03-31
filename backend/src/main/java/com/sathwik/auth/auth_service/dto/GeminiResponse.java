package com.sathwik.auth.auth_service.dto;

import java.util.List;

public class GeminiResponse {

    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    // Added Setter
    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public static class Candidate {
        private Content content;

        public Content getContent() {
            return content;
        }

        // Added Setter
        public void setContent(Content content) {
            this.content = content;
        }
    }

    public static class Content {
        private List<Part> parts;

        public List<Part> getParts() {
            return parts;
        }

        // Added Setter
        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        private String text;

        public String getText() {
            return text;
        }

        // Added Setter
        public void setText(String text) {
            this.text = text;
        }
    }
}