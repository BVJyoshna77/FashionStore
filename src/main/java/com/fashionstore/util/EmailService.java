package com.fashionstore.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    private static volatile String cachedSenderEmail = null;
    private static volatile String cachedAppPassword = null;

    public static String getSenderEmail() {
        if (cachedSenderEmail != null) {
            return cachedSenderEmail;
        }
        cachedSenderEmail = resolveConfig("FASHIONSTORE_GMAIL");
        return cachedSenderEmail;
    }

    public static String getAppPassword() {
        if (cachedAppPassword != null) {
            return cachedAppPassword;
        }
        String password = resolveConfig("FASHIONSTORE_GMAIL_APP_PASSWORD");
        if (password != null) {
            cachedAppPassword = password.replace(" ", "").trim();
        }
        return cachedAppPassword;
    }

    /**
     * Resolves configuration by checking:
     * 1. System environment variable (process level)
     * 2. JVM system property (-Dkey=value)
     * 3. Windows User Registry (HKCU\Environment) for persistent Windows variables
     */
    public static String resolveConfig(String key) {
        String val = System.getenv(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        val = System.getProperty(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        // Fallback for Windows GUI apps (Eclipse/Tomcat) before full restart
        return getFromWindowsRegistry(key);
    }

    private static String getFromWindowsRegistry(String key) {
        if (!System.getProperty("os.name", "").toLowerCase().contains("win")) {
            return null;
        }
        try {
            Process process = new ProcessBuilder("reg", "query", "HKCU\\Environment", "/v", key).start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(key)) {
                        String[] parts = line.trim().split("\\s+", 3);
                        if (parts.length >= 3) {
                            return parts[2].trim();
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Checks if Gmail SMTP credentials are configured.
     */
    public static boolean isConfigured() {
        String email = getSenderEmail();
        String password = getAppPassword();
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }

    /**
     * Sends an OTP verification email to the user.
     * Never logs or exposes the plain OTP.
     */
    public static void sendOtpEmail(String toEmail, String recipientName, String plainOtp) throws MessagingException {
        String senderEmail = getSenderEmail();
        String appPassword = getAppPassword();

        if (senderEmail == null || senderEmail.isEmpty() || appPassword == null || appPassword.isEmpty()) {
            throw new IllegalStateException("Gmail configuration missing: Please set FASHIONSTORE_GMAIL and FASHIONSTORE_GMAIL_APP_PASSWORD environment variables.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.connectiontimeout", "10000"); // 10s timeout
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(senderEmail, "FashionStore"));
        } catch (UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(senderEmail));
        }

        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail.trim()));
        message.setSubject("FashionStore - Your Account Verification Code", "UTF-8");

        String displayName = (recipientName != null && !recipientName.trim().isEmpty())
                ? recipientName.trim()
                : "Valued Customer";

        String htmlContent = buildEmailTemplate(displayName, plainOtp);
        message.setContent(htmlContent, "text/html; charset=UTF-8");

        // Send message
        Transport.send(message);
    }

    private static String buildEmailTemplate(String displayName, String otp) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head><meta charset='UTF-8'></head>"
                + "<body style='margin:0; padding:0; background-color:#f1f5f9; font-family:-apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif; color:#1e293b;'>"
                + "  <table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f1f5f9; padding:30px 15px;'>"
                + "    <tr>"
                + "      <td align='center'>"
                + "        <table width='100%' cellpadding='0' cellspacing='0' style='max-width:560px; background-color:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 4px 18px rgba(0,0,0,0.08);'>"
                + "          <!-- Header -->"
                + "          <tr>"
                + "            <td style='background-color:#0f172a; padding:28px 30px; text-align:center;'>"
                + "              <h1 style='margin:0; font-size:26px; font-weight:800; color:#ffffff; letter-spacing:1px; text-transform:uppercase;'>"
                + "                Fashion<span style='color:#f59e0b;'>Store</span>"
                + "              </h1>"
                + "              <p style='margin:6px 0 0 0; color:#94a3b8; font-size:13px;'>Email Verification Code</p>"
                + "            </td>"
                + "          </tr>"
                + "          <!-- Content -->"
                + "          <tr>"
                + "            <td style='padding:35px 30px;'>"
                + "              <h2 style='margin:0 0 16px 0; font-size:20px; font-weight:700; color:#0f172a;'>Hello " + escapeHtml(displayName) + ",</h2>"
                + "              <p style='margin:0 0 20px 0; font-size:15px; line-height:1.6; color:#475569;'>"
                + "                Thank you for joining <strong>FashionStore</strong>. Please enter the following 6-digit verification code to confirm your email and complete your registration:"
                + "              </p>"
                + "              <!-- OTP Box -->"
                + "              <div style='background-color:#f8fafc; border:2px dashed #cbd5e1; border-radius:10px; padding:22px 10px; text-align:center; margin:25px 0;'>"
                + "                <span style='font-family:Courier New, Courier, monospace; font-size:36px; font-weight:800; letter-spacing:12px; color:#0f172a;'>"
                + otp
                + "                </span>"
                + "              </div>"
                + "              <p style='margin:0 0 10px 0; font-size:14px; color:#64748b; line-height:1.5;'>"
                + "                <strong>Notice:</strong> This code is valid for <strong>5 minutes</strong> only and can be used once. You have a maximum of 5 verification attempts."
                + "              </p>"
                + "              <p style='margin:0 0 20px 0; font-size:13px; color:#94a3b8; line-height:1.5;'>"
                + "                If you did not attempt to register at FashionStore, please disregard this email. Never share this code with anyone."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "          <!-- Footer -->"
                + "          <tr>"
                + "            <td style='background-color:#f8fafc; padding:20px 30px; text-align:center; border-top:1px solid #e2e8f0; font-size:12px; color:#94a3b8;'>"
                + "              &copy; FashionStore. All rights reserved. This is an automated email, please do not reply."
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
