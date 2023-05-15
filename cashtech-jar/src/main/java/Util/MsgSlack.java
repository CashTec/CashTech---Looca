package Util;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;

public class MsgSlack {
    private static String webHookUrl = "https://hooks.slack.com/services/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";
    private static String oAuthToken = "xoxb-5174947311859-5203680117137-A0UFYkWBV2baWdfGKrzRwSaS\n";
    private static String slackChannel = "outros-assuntos";

    public void enviar(String mensagem) throws Exception {
        if(mensagem != null) {
            sendMessage(mensagem);
        }
    }
    public static void sendMessage(String message) throws Exception {
        StringBuilder msgbuilder = new StringBuilder();

        msgbuilder.append(message);

        String msg = msgbuilder.toString();

        Payload payload = Payload.builder()
                .text(msg)
                .channel(slackChannel)
                .build();

        Slack slack = Slack.getInstance();

        WebhookResponse response = slack.send(webHookUrl, payload);

        System.out.println(response.getMessage());
    }
}
