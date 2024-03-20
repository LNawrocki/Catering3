package pl.nawrockiit.catering3.email;

import java.util.List;

public interface EmailService {
    String sendMailRecipient(EmailDetails emailDetails, String[] recipients);

    String sendMailRecipientList(EmailDetails emailDetails, List<String> recipients);
}
