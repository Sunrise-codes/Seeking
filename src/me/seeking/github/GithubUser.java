package me.seeking.github;

public class GithubUser {
    private String username;
    private String avatarUrl;

    private int uid;

    public GithubUser(String username, String avatarUrl, int uid) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.uid = uid;
    }
}
