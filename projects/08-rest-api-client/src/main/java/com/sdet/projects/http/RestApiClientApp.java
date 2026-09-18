package com.sdet.projects.http;

public class RestApiClientApp {

    public static void main(String[] args) throws Exception {
        JsonPlaceholderClient client = new JsonPlaceholderClient();

        String successUrl = JsonPlaceholderClient.BASE_URL + "/users/1";
        System.out.println("GET " + successUrl);
        User user = client.fetchUser(1);
        System.out.println("status=200");
        System.out.println("id=" + user.id());
        System.out.println("name=" + user.name());
        System.out.println("username=" + user.username());

        String missingUrl = JsonPlaceholderClient.BASE_URL + "/users/999";
        System.out.println("GET " + missingUrl);
        try {
            client.fetchUser(999);
        } catch (NonSuccessStatusException exception) {
            System.out.println("Non-200 response: " + exception.statusCode());
        }
    }
}
