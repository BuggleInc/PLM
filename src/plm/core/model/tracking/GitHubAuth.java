package plm.core.model.tracking;

import com.github.javakeyring.Keyring;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import plm.core.model.Game;

/**
 * GitHub identity of the user, obtained with the device flow of a GitHub App (no client secret is needed). The permissions are those of the App.
 * The token lives in the system keyring when there is one, in the local properties otherwise.
 */
public final class GitHubAuth {
  private static final String SERVICE = "PLM", ACCOUNT = "github-token";
  private static final String PROP_TOKEN = "plm.github.token", PROP_LOGIN = "plm.github.login";
  private static final String PROP_CLIENT_ID = "plm.github.client.id";
  private static final HttpClient HTTP       = HttpClient.newHttpClient();

  /** The code that the user types on {@code verificationUri} to authorize the PLM. */
  public record DeviceCode(String deviceCode, String userCode, String verificationUri, int interval) {}

  private GitHubAuth() {}

  public static DeviceCode requestCode() throws IOException, InterruptedException
  {
    String clientId = Game.getProperty(PROP_CLIENT_ID);
    if (clientId.isEmpty())
      throw new IOException("The property " + PROP_CLIENT_ID + " is not set");
    JsonObject json = post("https://github.com/login/device/code", Map.of("client_id", clientId));
    if (json.has("error"))
      throw new IOException(json.toString());
    return new DeviceCode(json.get("device_code").getAsString(), json.get("user_code").getAsString(), json.get("verification_uri").getAsString(),
                          json.get("interval").getAsInt());
  }

  /** Waits until the user authorizes the PLM, then stores the token and returns the GitHub login. Interrupt to cancel. */
  public static String completeLogin(DeviceCode code) throws IOException, InterruptedException
  {
    int interval = code.interval();
    while (true) {
      Thread.sleep(interval * 1000L);
      JsonObject json =
          post("https://github.com/login/oauth/access_token", Map.of("client_id", Game.getProperty(PROP_CLIENT_ID), "device_code", code.deviceCode(),
                                                                     "grant_type", "urn:ietf:params:oauth:grant-type:device_code"));
      if (json.has("access_token")) {
        String token    = json.get("access_token").getAsString();
        HttpRequest req = HttpRequest.newBuilder(URI.create("https://api.github.com/user")).header("Authorization", "Bearer " + token).build();
        String login    = parse(HTTP.send(req, HttpResponse.BodyHandlers.ofString())).get("login").getAsString();
        store(token, login);
        return login;
      }
      String error = json.get("error").getAsString();
      if (error.equals("slow_down"))
        interval += 5;
      else if (!error.equals("authorization_pending"))
        throw new IOException(error);
    }
  }

  /** The token of the user, or null if she is not logged in. */
  public static String getToken()
  {
    try {
      String token = Keyring.create().getPassword(SERVICE, ACCOUNT);
      if (token != null)
        return token;
    } catch (Exception e) {
      // No usable keyring: the token may be in the properties
    }
    String token = Game.getProperty(PROP_TOKEN);
    return token.isEmpty() ? null : token;
  }

  /** The GitHub login of the user, or null if she is not logged in (or if her token got lost). */
  public static String getLogin()
  {
    String login = Game.getProperty(PROP_LOGIN);
    return login.isEmpty() || getToken() == null ? null : login;
  }

  public static void logout()
  {
    try {
      Keyring.create().deletePassword(SERVICE, ACCOUNT);
    } catch (Exception e) {
      // No keyring, or no token in it
    }
    Game.setProperty(PROP_TOKEN, "");
    Game.setProperty(PROP_LOGIN, "");
    Game.storeProperties();
  }

  private static void store(String token, String login)
  {
    boolean inKeyring = false;
    try {
      Keyring.create().setPassword(SERVICE, ACCOUNT, token);
      inKeyring = true;
    } catch (Exception e) {
      // No usable keyring: keep the token in the properties
    }
    Game.setProperty(PROP_TOKEN, inKeyring ? "" : token);
    Game.setProperty(PROP_LOGIN, login);
    Game.storeProperties();
  }

  private static JsonObject post(String url, Map<String, String> form) throws IOException, InterruptedException
  {
    String body =
        form.entrySet().stream().map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
    HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                          .header("Accept", "application/json")
                          .header("Content-Type", "application/x-www-form-urlencoded")
                          .POST(HttpRequest.BodyPublishers.ofString(body))
                          .build();
    return parse(HTTP.send(req, HttpResponse.BodyHandlers.ofString()));
  }

  private static JsonObject parse(HttpResponse<String> response) throws IOException
  {
    if (response.statusCode() >= 400)
      throw new IOException("GitHub answered HTTP " + response.statusCode() + " to " + response.uri() + ": " + response.body());
    return JsonParser.parseString(response.body()).getAsJsonObject();
  }
}
