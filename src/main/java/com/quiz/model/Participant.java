package com.quiz.model;

/**
 * Participant (Student) model
 */
public class Participant {
    private int participantId;
    private String name;
    private String email;
    private String contact;
    private String password; // plain-text per request (not secure)

    public Participant() {}

    public Participant(int participantId, String name, String email, String contact, String password) {
        this.participantId = participantId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
    }

    public int getParticipantId() { return participantId; }
    public void setParticipantId(int participantId) { this.participantId = participantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}