package com.eaapps.thebesacademy.Model;

/**
 * Created by eslamandroid on 3/12/18.
 */

public class ItemUser {
    Profile profile;
    Messages messages;

    public ItemUser(Profile profile, Messages messages) {
        this.profile = profile;
        this.messages = messages;
    }

    public ItemUser(Profile profile) {
        this.profile = profile;
    }

    public ItemUser(Messages messages) {
        this.messages = messages;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}
