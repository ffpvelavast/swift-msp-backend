-- Translations seed data — generated from master Excel export
-- Total rows: 399, max id: 414

INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (1, 'global-login', 'title', '{"de": "Melden Sie sich bei Ihrem Konto an", "en": "Sign in to your Account"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (2, 'global-login', 'description', '{"de": "Geben Sie Ihre E-Mail-Adresse ein, um mit Ihrem Konto zu starten", "en": "Enter your email address to get started with your account"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (3, 'global-login', 'subDescription', '{"de": "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed ut ante sed augue efficitur condimentum. Praesent eu luctus arcu", "en": "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed ut ante sed augue efficitur condimentum. Praesent eu luctus arcu"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (4, 'global-login', 'emailPlaceholder', '{"de": "E-Mail*", "en": "Email*"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (5, 'global-login', 'buttonText', '{"de": "Loslegen", "en": "Get Started"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (6, 'global-login', 'errorMessages.required', '{"de": "E-Mail ist erforderlich", "en": "Email is required"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (7, 'global-login', 'errorMessages.invalid', '{"de": "Bitte geben Sie eine gültige E-Mail ein", "en": "Please enter a valid email"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (8, 'global-login', 'errorMessages.pattern', '{"de": "Diese E-Mail-Domain ist nicht bei REX registriert. Ändern Sie Ihre E-Mail-Adresse, um auf Ihr Konto zuzugreifen...", "en": "This email domain is not registered in REX, Change your email address to access your account..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (9, 'global-login', 'errorMessages.invalidPassword', '{"de": "Ungültiges Passwort. Bitte versuchen Sie es erneut.", "en": "Invalid password. Please try again."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (10, 'global-login', 'loader.gifAlt', '{"de": "Wird geladen...", "en": "Loading..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (11, 'global-login', 'copyright', '{"de": "Copyright © 2024 REX. Alle Rechte vorbehalten.", "en": "Copyright © 2024 REX. All rights reserved."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (12, 'global-login', 'welcomeMessage', '{"de": "Hallo Petra Schmitz, Willkommen zurück!", "en": "Hello Petra Schmitz, Welcome back!"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (13, 'global-login', 'continueButton', '{"de": "Fortfahren", "en": "Continue"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (14, 'global-login', 'carouselTitle', '{"de": "Verwalten Sie Ihre Finanzen einfach", "en": "Manage your finances easily"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (15, 'global-login', 'carouselDescription', '{"de": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus.", "en": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (16, 'global-login', 'dontShowAgain', '{"de": "Nicht erneut anzeigen", "en": "Dont show this again"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (17, 'global-login', 'logoAlt', '{"de": "REX Logo", "en": "REX Logo"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (18, 'global-login', 'carouselImageAlt', '{"de": "Karussellbild", "en": "Carousel Image"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (19, 'login-component', 'title', '{"de": "Melden Sie sich bei Ihrem Konto an", "en": "Sign in to your Account"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (20, 'login-component', 'emailPlaceholder', '{"de": "Geben Sie Ihre E-Mail ein", "en": "Enter your Email"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (21, 'login-component', 'passwordPlaceholder', '{"de": "Geben Sie Ihr Passwort ein", "en": "Enter your Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (22, 'login-component', 'buttonText', '{"de": "Loslegen", "en": "Get Started"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (23, 'login-component', 'forgotPasswordText', '{"de": "Passwort vergessen", "en": "Forgot Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (24, 'login-component', 'errorMessages.emailRequired', '{"de": "E-Mail ist erforderlich", "en": "Email is required"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (25, 'login-component', 'errorMessages.passwordRequired', '{"de": "Passwort ist erforderlich", "en": "Password is required"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (26, 'login-component', 'errorMessages.emailInvalid', '{"de": "Bitte geben Sie eine gültige E-Mail ein", "en": "Please enter a valid email"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (27, 'login-component', 'biometricTitle', '{"de": "Möchten Sie die biometrische Anmeldung aktivieren?", "en": "Do you want to enable biometric login?"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (28, 'login-component', 'biometricDescription', '{"de": "Melden Sie sich bei Ihrem Schindler-Konto mit den biometrischen Anmeldedaten Ihres Telefons an.", "en": "Log in to your Schindler account using your phones biometric credentials."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (29, 'login-component', 'enableBiometricText', '{"de": "Biometrische Anmeldung aktivieren", "en": "Enable biometric login"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (30, 'login-component', 'skipForNow', '{"de": "Für jetzt überspringen", "en": "Skip for now"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (31, 'face-id-settings', 'settingsHeader', '{"de": "Einstellungen", "en": "Settings"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (32, 'face-id-settings', 'faceIdLoginTitle', '{"de": "Face-ID-Anmeldung", "en": "Face ID Login"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (33, 'face-id-settings', 'loginDescription', '{"de": "Melden Sie sich bei Ihrem Schindler-Konto mit den biometrischen Anmeldedaten Ihres Telefons an.", "en": "Log in to your Schindler account using your phones biometric credentials."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (34, 'face-id-settings', 'navigateBackButtonAlt', '{"de": "Zurück zum Dashboard", "en": "Back to Dashboard"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (35, 'splash-screen', 'slide1.title', '{"de": "Verwalten Sie Ihre Finanzen ganz einfach", "en": "Manage your finances easily"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (36, 'splash-screen', 'slide1.paragraph', '{"de": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus.", "en": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (37, 'splash-screen', 'slide2.title', '{"de": "Finanzberichte und Budgetierung", "en": "Financial Report and Budgeting"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (38, 'splash-screen', 'slide2.paragraph', '{"de": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus.", "en": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (39, 'splash-screen', 'slide3.title', '{"de": "Vereinfachen Sie Ihre Steuerzahlung", "en": "Make Easy your Tax Payment"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (40, 'splash-screen', 'slide3.paragraph', '{"de": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus.", "en": "Aenean congue purus dui, in fermentum odio consectetur non. Curabitur rutrum pretium velit, ut egestas dui pellentesque maximus."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (41, 'splash-screen', 'getStartedButton', '{"de": "Legen Sie los", "en": "Get Started"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (42, 'dashboard', 'title', '{"de": "Dashboard", "en": "Dashboard"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (43, 'forgot-password', 'pageTitle', '{"de": "Passwort vergessen", "en": "Forgot Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (44, 'forgot-password', 'emailPlaceholder', '{"de": "Geben Sie Ihre E-Mail ein", "en": "Enter your Email"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (45, 'forgot-password', 'errorMessage', '{"de": "Die E-Mail-Adresse existiert nicht. Bitte überprüfen Sie Ihre E-Mail und versuchen Sie es erneut.", "en": "The email address does not exist. Please check your email and try again."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (46, 'forgot-password', 'getOtpButtonText', '{"de": "OTP erhalten", "en": "Get OTP"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (47, 'forgot-password', 'backToLoginButtonText', '{"de": "Zurück zur Anmeldung", "en": "Back to Login"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (48, 'verify-email', 'title', '{"de": "Überprüfen Sie Ihre E-Mail", "en": "Verify Your Email"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (49, 'verify-email', 'description', '{"de": "Wir haben Ihnen einen OTP-Code gesendet an", "en": "We have sent you an OTP to"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (50, 'verify-email', 'emailHint', '{"de": "Bitte überprüfen Sie Ihren Posteingang und fügen Sie den Code unten ein.", "en": "Please check your inbox and paste the code below."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (51, 'verify-email', 'otpError', '{"de": "Der eingegebene OTP-Code ist falsch. Bitte versuchen Sie es erneut.", "en": "The OTP youve entered is incorrect. Please try again."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (52, 'verify-email', 'resendCode', '{"de": "Code erneut senden", "en": "Resend Code"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (53, 'verify-email', 'verifyButton', '{"de": "Bestätigen", "en": "Verify"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (54, 'verify-email', 'backToLogin', '{"de": "Zurück zur Anmeldung", "en": "Back to Login"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (55, 'verify-email', 'notificationTitle', '{"de": "Schindler", "en": "Schindler"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (56, 'verify-email', 'notificationMessage', '{"de": "Ihre Verifizierungsnummer lautet", "en": "Your verification number is"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (57, 'create-password', 'title', '{"de": "Neues Passwort erstellen", "en": "Create New Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (58, 'create-password', 'passwordPlaceholder', '{"de": "Passwort erstellen", "en": "Create Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (59, 'create-password', 'confirmPasswordPlaceholder', '{"de": "Passwort erneut eingeben", "en": "Re-Enter Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (60, 'create-password', 'errorMsg', '{"de": "Das erstellte Passwort und das erneut eingegebene Passwort stimmen nicht überein. Versuchen Sie es erneut.", "en": "Create Password and Re-Enter Password didnt match. Try again."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (61, 'create-password', 'submitButton', '{"de": "Passwort erstellen", "en": "Create Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (62, 'create-password', 'backToLoginButton', '{"de": "Zurück zur Anmeldung", "en": "Back to Login"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (63, 'password-success', 'title', '{"de": "Ihr Passwort wurde erfolgreich geändert!", "en": "Your Password Changed Successfully!"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (64, 'password-success', 'paragraph', '{"de": "Ihr Passwort wurde erfolgreich geändert.", "en": "Your password has been changed successfully."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (65, 'password-success', 'backToLoginButton', '{"de": "Zurück zur Anmeldung", "en": "Back to Login"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (66, 'reports-component', 'header.title', '{"de": "Ausgabenbericht", "en": "Expense Report"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (67, 'reports-component', 'viewAll', '{"de": "Alle anzeigen", "en": "View All"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (68, 'reports-component', 'reportsData', '{"de": [{"date1": "2024-09-23", "title1": "Reise nach Venedig", "status1": "Genehmigt"}, {"date2": "2024-09-20", "title2": "Geschenk für Kunden", "status2": "Abgelehnt"}, {"date3": "2024-09-18", "title3": "Kundentreffen in Mailand", "status3": "Überprüfung"}], "en": [{"date1": "2024-09-23", "title1": "Travel to Venice", "status1": "Approved"}, {"date2": "2024-09-20", "title2": "Gift to Client", "status2": "Rejected"}, {"date3": "2024-09-18", "title3": "Client Meeting Milano", "status3": "Review"}]}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (69, 'reports-component', 'expenseDetails.title', '{"de": "Reise nach Venedig", "en": "Travel to Venice"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (70, 'reports-component', 'expenseDetails.step1', '{"de": "Bericht erstellt", "en": "Report Created"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (71, 'reports-component', 'expenseDetails.step2', '{"de": "Zur Überprüfung gesendet", "en": "Sent to Review"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (72, 'reports-component', 'expenseDetails.step3', '{"de": "In Überprüfung", "en": "Under Review"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (73, 'reports-component', 'expenseDetails.step4', '{"de": "Genehmigt", "en": "Approved"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (74, 'reports-component', 'expenseDetails.approvalSummary.title', '{"de": "Genehmigungsbetrag", "en": "Approval Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (75, 'reports-component', 'expenseDetails.approvalSummary.submittedOn', '{"de": "eingereicht am", "en": "submitted on"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (76, 'reports-component', 'expenseDetails.approvalSummary.approvedOn', '{"de": "genehmigt am", "en": "approved on"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (77, 'reports-component', 'expenseDetails.approvalSummary.approvedBy', '{"de": "genehmigt von", "en": "approved by"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (78, 'reports-component', 'expenseDetails.approvalSummary.approvedByPerson', '{"de": "Christina", "en": "Christina"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (79, 'reports-component', 'expenseDetails.approvalSummary.details.category', '{"de": "Ausgabenkategorie", "en": "Expense Category"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (80, 'reports-component', 'expenseDetails.approvalSummary.details.nature', '{"de": "Ausgabenart", "en": "Expense Nature"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (81, 'reports-component', 'expenseDetails.approvalSummary.details.title', '{"de": "Ausgabentitel", "en": "Expense Title"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (82, 'reports-component', 'expenseDetails.approvalSummary.details.location', '{"de": "Ort", "en": "Location"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (83, 'reports-component', 'expenseDetails.approvalSummary.details.date', '{"de": "Ausgabendatum", "en": "Expense Date"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (84, 'reports-component', 'expenseDetails.approvalSummary.details.categoryName', '{"de": "Gastfreundschaft", "en": "Hospitality"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (85, 'reports-component', 'expenseDetails.approvalSummary.details.natureName', '{"de": "Teamtreffen", "en": "Team Meeting"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (86, 'reports-component', 'expenseDetails.approvalSummary.details.titleName', '{"de": "Kundentreffen in Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (87, 'reports-component', 'expenseDetails.approvalSummary.details.locationName', '{"de": "Deutschland", "en": "Germany"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (88, 'reports-component', 'expenseDetails.viewMoreDetails', '{"de": "Mehr Details anzeigen", "en": "View More Details"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (89, 'reports-component', 'filterData.header.title', '{"de": "Filter", "en": "Filter"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (90, 'reports-component', 'filterData.header.clearAllText', '{"de": "Alle löschen", "en": "Clear All"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (91, 'reports-component', 'filterData.statusSection.title', '{"de": "Status", "en": "Status"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (92, 'reports-component', 'filterData.statusSection.options', '{"de": [{"label1": "Genehmigt"}, {"label2": "Überprüfung"}, {"label3": "Abgelehnt"}], "en": [{"label1": "Approved"}, {"label2": "Review"}, {"label3": "Rejected"}]}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (93, 'reports-component', 'filterData.dateSection.title', '{"de": "Datum", "en": "Date"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (94, 'reports-component', 'filterData.locationSection.title', '{"de": "Ort", "en": "Location"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (95, 'reports-component', 'filterData.footer.applyButtonText', '{"de": "Anwenden", "en": "Apply"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (96, 'reports-component', 'pagination.previous', '{"de": "Vorherige", "en": "Previous"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (97, 'reports-component', 'pagination.next', '{"de": "Nächste", "en": "Next"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (98, 'notification-screen', 'title', '{"de": "Benachrichtigungen", "en": "Notifications"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (99, 'notification-screen', 'generalTitle', '{"de": "Allgemein", "en": "General"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (100, 'notification-screen', 'alertsTitle', '{"de": "Warnungen", "en": "Alerts"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (101, 'notification-screen', 'generalNotifications', '{"de": [{"heading": "Sie wurden zum Stellvertreter ernannt", "timestamp": "Vor 1 Stunde", "description": "Sie wurden als Stellvertreter für "Dr. Peter Mustermann" ernannt. Jetzt können Sie Erstattungen im Namen Ihres Managers einreichen."}, {"text": "Spesenabrechnung "Teamausflug April" wurde erstattet", "heading": "Genehmigt", "timestamp": "05. Mai 2023"}, {"text": "Vergessen Sie nicht, das Projekt-Update bis zum Ende des Tages einzureichen", "heading": "Erinnerung", "timestamp": "26. April 2023"}], "en": [{"heading": "Youve been appointed deputy", "timestamp": "1h ago", "description": "You have been assigned as the deputy for "Dr. Peter Mustermann". Now you can submit reimbursement on behalf of your manager."}, {"text": "Expense report "Team Outing April" has been reimbursed", "heading": "Approved", "timestamp": "05 May 2023"}, {"text": "Dont forget to submit the project update by end of day", "heading": "Reminder", "timestamp": "26 April 2023"}]}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (102, 'notification-screen', 'alertsNotifications', '{"de": [{"heading": "Ihr Team-Budget wurde erhöht.", "timestamp": "Vor 1 Stunde", "description": "Folgen Sie diesen Schritten, um ein Budget zu erstellen, das Ihre Mitarbeiterinitiativen unterstützt und die Gesamtleistung des Unternehmens steigert."}, {"text": "Stellen Sie sicher, dass Ihr Team die wichtigsten Schritte für ein reibungsloses Onboarding kennt.", "heading": "Neuer Einstellungsplan ist bereit für die Umsetzung.", "timestamp": "Vor 3 Tagen"}], "en": [{"heading": "Your team Budget has been increased.", "timestamp": "1h ago", "description": "Follow these steps to build a budget that supports your People initiatives and enhances overall company performance."}, {"text": "Ensure your team knows the key steps for smooth onboarding.", "heading": "New hiring plan is ready for implementation.", "timestamp": "3d ago"}]}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (103, 'deputy-profile-screen', 'header.backAlt', '{"de": "Zurück", "en": "Back"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (104, 'deputy-profile-screen', 'header.title', '{"de": "Mein Profil", "en": "My Profile"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (105, 'deputy-profile-screen', 'header.logoutAlt', '{"de": "Abmelden", "en": "Logout"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (106, 'deputy-profile-screen', 'profile.employeeCode', '{"de": "Mitarbeitercode", "en": "Employee Code"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (107, 'deputy-profile-screen', 'profile.employeeName', '{"de": "Aspen Rosser", "en": "Aspen Rosser"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (108, 'deputy-profile-screen', 'profile.companyName', '{"de": "Schindler Construction Pvt Ltd", "en": "Schindler Construction Pvt Ltd"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (109, 'deputy-profile-screen', 'profile.emailAddress', '{"de": "petra.schmitz@schindler.com", "en": "petra.schmitz@schindler.com"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (110, 'deputy-profile-screen', 'profile.changePasswordText', '{"de": "Passwort ändern", "en": "Change Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (111, 'deputy-profile-screen', 'accordion.managerAccordion.title', '{"de": "Manager-Profil", "en": "Manager Profile"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (112, 'deputy-profile-screen', 'accordion.managerAccordion.managerProfileAlt', '{"de": "Manager-Profil", "en": "Manager Profile"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (113, 'deputy-profile-screen', 'accordion.managerAccordion.managerName', '{"de": "Dr. Peter Mustermann", "en": "Dr. Peter Mustermann"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (114, 'deputy-profile-screen', 'accordion.managerAccordion.messageTitle', '{"de": "Sie wurden zum Stellvertreter ernannt", "en": "Youve been appointed deputy"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (115, 'deputy-profile-screen', 'accordion.managerAccordion.deputyAssignedMessageFirstHalf', '{"de": "Sie wurden als Stellvertreter für", "en": "You have been assigned as the deputy for"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (116, 'deputy-profile-screen', 'accordion.managerAccordion.deputyAssignedMessageSecondHalf', '{"de": "Nun können Sie Erstattungen im Namen Ihres Managers einreichen.", "en": "Now you can submit reimbursement on behalf of your manager."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (117, 'deputy-profile-screen', 'accordion.managerAccordion.timeAgo', '{"de": "Vor 1 Stunde", "en": "1h ago"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (118, 'deputy-profile-screen', 'accordion.managerAccordion.closeAlt', '{"de": "Schließen", "en": "Close"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (119, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.title', '{"de": "Favoriten", "en": "Favourite Beneficiaries"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (120, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.roundPlusAlt', '{"de": "Neuen Begünstigten hinzufügen", "en": "Add New Beneficiary"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (121, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.starAlt', '{"de": "Sternsymbol", "en": "Star Icon"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (122, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.salesTeam', '{"de": "Mein Verkaufsteam", "en": "My Sales Team"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (123, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.beneficiaryAlt', '{"de": "Begünstigtensymbol", "en": "Beneficiary Icon"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (124, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.editAlt', '{"de": "Bearbeiten-Symbol", "en": "Edit Icon"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (125, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.tripName', '{"de": "Italienreise", "en": "Italy Trip"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (126, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.tripAlt', '{"de": "Reisesymbol", "en": "Trip Icon"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (127, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.sharedBy', '{"de": "Geteilt von", "en": "Shared by"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (128, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.sharedByManager', '{"de": "Dr. Peter Mustermann", "en": "Dr. Peter Mustermann"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (129, 'deputy-profile-screen', 'accordion.favouriteBeneficiaries.moreAlt', '{"de": "Mehr Optionen", "en": "More Options"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (130, 'deputy-profile-screen', 'accordion.teamBudget.title', '{"de": "Team-Budget", "en": "Team Budget"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (131, 'deputy-profile-screen', 'accordion.teamBudget.managerName', '{"de": "Dr. Peter Mustermann", "en": "Dr. Peter Mustermann"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (132, 'deputy-profile-screen', 'accordion.teamBudget.notAllocated', '{"de": "Nicht zugewiesen", "en": "Not Allocated"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (133, 'deputy-profile-screen', 'accordion.teamBudget.usedAmount', '{"de": "Verwendeter Betrag", "en": "Used Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (134, 'deputy-profile-screen', 'accordion.teamBudget.balanceAmount', '{"de": "Restbetrag", "en": "Balance Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (135, 'manager-profile-screen', 'backAlt', '{"de": "Zurück", "en": "Back"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (136, 'manager-profile-screen', 'title', '{"de": "Mein Profil", "en": "My Profile"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (137, 'manager-profile-screen', 'logoutAlt', '{"de": "Abmelden", "en": "logout"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (138, 'manager-profile-screen', 'deputiesTitle', '{"de": "Meine Stellvertreter", "en": "My Deputies"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (139, 'manager-profile-screen', 'deputiesDescription', '{"de": "Sie können Ihren Stellvertreter hinzufügen, um Ihre Spesenabrechnungen zu verwalten", "en": "You can add your deputy to manage your expexpenses reports"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (140, 'manager-profile-screen', 'addDeputyButtonText', '{"de": "Stellvertreter hinzufügen", "en": "Add Deputy"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (141, 'manager-profile-screen', 'hospitalityTeam', '{"de": "Gastfreundschaft | Teammeeting", "en": "Hospitality | Team Meeting"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (142, 'manager-profile-screen', 'teamBudgetTitle', '{"de": "Team-Budget", "en": "Team Budget"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (143, 'manager-profile-screen', 'allotedAmountTitle', '{"de": "Zugewiesener Betrag", "en": "Allotted Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (144, 'manager-profile-screen', 'balanceAmountTitle', '{"de": "Restbetrag", "en": "Balance Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (145, 'manager-profile-screen', 'internalTitle', '{"de": "Intern", "en": "Internal"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (146, 'manager-profile-screen', 'externalTitle', '{"de": "Extern", "en": "External"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (147, 'manager-profile-screen', 'createPicklistDialog.title', '{"de": "Pickliste erstellen", "en": "Create Pick List"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (148, 'manager-profile-screen', 'createPicklistDialog.picklistName', '{"de": "Name der Pickliste", "en": "Pick List Name"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (149, 'manager-profile-screen', 'createPicklistDialog.createPicklistPlaceholder', '{"de": "Kundenmeeting Milano", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (150, 'manager-profile-screen', 'createPicklistDialog.addBeneficiariesTitle', '{"de": "Begünstigte hinzufügen", "en": "Add Beneficiaries"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (151, 'manager-profile-screen', 'createPicklistDialog.createButtonText', '{"de": "Erstellen", "en": "Create"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (152, 'manager-profile-screen', 'successfullyDialog.successfullyCreatedTitle', '{"de": "Erfolgreich erstellt", "en": "Successfully Created"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (153, 'manager-profile-screen', 'successfullyDialog.successfullyCreatedDescription', '{"de": "Sie haben die erste Pickliste erfolgreich erstellt.", "en": "You have successfully created first pick list."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (154, 'manager-profile-screen', 'successfullyDialog.successfullyCreatedButtonText', '{"de": "Pickliste erstellen", "en": "Create Pick List"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (155, 'manager-profile-screen', 'successfullyDialog.doLaterButtonText', '{"de": "Später machen", "en": "Ill do later"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (156, 'manager-profile-screen', 'user.empCode', '{"de": "Mitarbeitercode", "en": "Employee Code"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (157, 'manager-profile-screen', 'user.name', '{"de": "Dr. Peter Mustermann", "en": "Dr. Peter Mustermann"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (158, 'manager-profile-screen', 'user.company', '{"de": "Schindler Construction Pvt Ltd", "en": "Schindler Construction Pvt Ltd"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (159, 'manager-profile-screen', 'user.email', '{"de": "peter.mustermann@schindler.com", "en": "peter.mustermann@schindler.com"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (160, 'manager-profile-screen', 'user.changePassword', '{"de": "Passwort ändern", "en": "Change Password"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (161, 'manager-profile-screen', 'beneficiaries.title', '{"de": "Favoriten", "en": "Favourite Beneficiaries"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (162, 'manager-profile-screen', 'aiContent.name', '{"de": "Meine Abteilung", "en": "My Department"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (163, 'manager-profile-screen', 'aiContent.role', '{"de": "Projektmanager...", "en": "Project Manager..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (164, 'manager-profile-screen', 'aiContent.shareToDeputy', '{"de": "Mit Stellvertreter teilen", "en": "Share to Deputy"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (165, 'manager-profile-screen', 'sharedDeputiesList.title', '{"de": "Mit Stellvertretern geteilt", "en": "Shared To Deputies"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (166, 'manager-profile-screen', 'sharedDeputiesList.list', '{"de": [{"name": "Jaydon Rhiel Madsen", "team": "Gastfreundschaft | Teammeeting", "company": "Warner Bros"}, {"name": "John Doe", "team": "Marketing | Markenmanagement", "company": "Sony Pictures"}], "en": [{"name": "Jaydon Rhiel Madsen", "team": "Hospitality | Team Meeting", "company": "Warner Bros"}, {"name": "John Doe", "team": "Marketing | Brand Management", "company": "Sony Pictures"}]}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (167, 'add-deputy-screen', 'selectRights', '{"de": "Rechte auswählen", "en": "Select Rights"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (168, 'add-deputy-screen', 'checkboxIconAlt', '{"de": "Checkbox-Symbol", "en": "Checkbox Icon"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (169, 'add-deputy-screen', 'addDeputyAlt', '{"de": "Stellvertreter hinzufügen", "en": "Add Deputy"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (170, 'add-deputy-screen', 'schindlerLogoAlt', '{"de": "Schindler-Logo", "en": "Schindler Logo"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (171, 'add-deputy-screen', 'defaultRights', '{"de": "Standardrechte", "en": "Default Rights"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (172, 'add-deputy-screen', 'deleteAlt', '{"de": "Löschen", "en": "Delete"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (173, 'add-deputy-screen', 'searchAlt', '{"de": "Suchen", "en": "Search"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (174, 'add-deputy-screen', 'filterAlt', '{"de": "Filter", "en": "Filter"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (175, 'add-deputy-screen', 'successAnimationAlt', '{"de": "Erfolgsanimation", "en": "Success Animation"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (176, 'add-deputy-screen', 'addAnotherAlt', '{"de": "Weiteren hinzufügen", "en": "Add Another"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (177, 'add-deputy-screen', 'backAlt', '{"de": "Zurück", "en": "Back"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (178, 'add-deputy-screen', 'title', '{"de": "Stellvertreter Hinzufügen", "en": "Add Deputy"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (179, 'add-deputy-screen', 'deputyRights', '{"de": "Stellvertreterrechte", "en": "Deputy Rights"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (180, 'add-deputy-screen', 'addDeputiesButtonText', '{"de": "Stellvertreter Hinzufügen", "en": "Add Deputies"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (181, 'add-deputy-screen', 'createButtonText', '{"de": "Erstellen", "en": "Create"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (182, 'add-deputy-screen', 'searchPlaceholderText', '{"de": "Suchen...", "en": "Search..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (183, 'add-deputy-screen', 'favouriteTabTitle', '{"de": "Favoriten", "en": "Favorites"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (184, 'add-deputy-screen', 'contentDescription', '{"de": "Inhalt kommt hier", "en": "Content comes here"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (185, 'add-deputy-screen', 'internalTabTitle', '{"de": "Intern", "en": "Internal"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (186, 'add-deputy-screen', 'saveButtonText', '{"de": "Speichern", "en": "Save"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (187, 'add-deputy-screen', 'successfullyAddedTitle', '{"de": "Erfolgreich Hinzugefügt", "en": "Successfully Added"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (188, 'add-deputy-screen', 'successfullyAddedDescription', '{"de": "Sie haben erfolgreich Ihren Stellvertreter hinzugefügt", "en": "You have successfully added your deputy"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (189, 'add-deputy-screen', 'successfullyAddedButtonText', '{"de": "Stellvertreter Hinzufügen", "en": "Add Deputy"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (190, 'add-deputy-screen', 'doLaterButtonText', '{"de": "Später erledigen", "en": "Ill do later"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (191, 'create-expense-report-screen', 'reportSubmitTitle', '{"de": "Berichtseinreichung für", "en": "Report submitting for"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (192, 'create-expense-report-screen', 'createTitle', '{"de": "Spesenbericht Erstellen", "en": "Create Expense Report"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (193, 'create-expense-report-screen', 'createPlaceholderText', '{"de": "Auswählen", "en": "Select"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (194, 'create-expense-report-screen', 'expenseTitle', '{"de": "Spesenbericht Titel", "en": "Expense Report Title"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (195, 'create-expense-report-screen', 'expensePlaceholderText', '{"de": "Spesenbericht Titel", "en": "Expense Report Title"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (196, 'create-expense-report-screen', 'reportTitle', '{"de": "Berichtszeitraum", "en": "Report Date Range"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (197, 'create-expense-report-screen', 'expenseTitle1', '{"de": "Spesen -1", "en": "Expense -1"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (198, 'create-expense-report-screen', 'fieldTitle', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (199, 'create-expense-report-screen', 'dateTitle', '{"de": "Spesendatum: ", "en": "Expense Date: "}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (200, 'create-expense-report-screen', 'claimAmountTitle', '{"de": "Gesamter Erstattungsbetrag: ", "en": "Total Claim Amount: "}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (201, 'create-expense-report-screen', 'expenseTitle2', '{"de": "Spesen Hinzufügen - 2", "en": "Add Expense - 2"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (202, 'create-expense-report-screen', 'cancelButtonText', '{"de": "Abbrechen", "en": "Cancel"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (203, 'create-expense-report-screen', 'submitButtonText', '{"de": "Absenden", "en": "Submit"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (204, 'congratulations-screen', 'title', '{"de": "Herzlichen Glückwunsch!", "en": "Congratulations!"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (205, 'congratulations-screen', 'description', '{"de": "Ihre Erstattung wurde eingereicht...", "en": "Your reimbursement has been submitted..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (206, 'congratulations-screen', 'trackDescription', '{"de": "Um den Status Ihrer Erstattung zu verfolgen, besuchen Sie ", "en": "To track your reimbursement status on "}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (207, 'congratulations-screen', 'dashboardTitle', '{"de": "Ihr Dashboard", "en": "your Dashboard"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (208, 'help-screen', 'description', '{"de": "Tippen Sie unten, um ein Foto Ihres Belegs hochzuladen. Wischen Sie für Tipps und Anleitungen, wie Sie ein Bild Ihres Belegs machen.", "en": "Tap below to upload a photo of your receipt. Swipe for tips and guidelines for taking a picture of your receipt."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (209, 'summary-expense-screen', 'headerTitle', '{"de": "Zusammenfassung Spesen-1", "en": "Summary Expense-1"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (210, 'summary-expense-screen', 'title', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (211, 'summary-expense-screen', 'claimAmountTitle', '{"de": "Gesamter Erstattungsbetrag:", "en": "Total Claim Amount:"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (212, 'summary-expense-screen', 'category', '{"de": "Spesen Kategorie", "en": "Expense Category"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (213, 'summary-expense-screen', 'nature', '{"de": "Spesen Art", "en": "Expense Nature"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (214, 'summary-expense-screen', 'expenseTitle', '{"de": "Spesen Titel", "en": "Expense Title"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (215, 'summary-expense-screen', 'location', '{"de": "Standort", "en": "Location"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (216, 'summary-expense-screen', 'date', '{"de": "Spesendatum", "en": "Expense Date"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (217, 'summary-expense-screen', 'categoryName', '{"de": "Gastfreundschaft", "en": "Hospitality"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (218, 'summary-expense-screen', 'natureName', '{"de": "Team Meeting", "en": "Team Meeting"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (219, 'summary-expense-screen', 'expensetitleName', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (220, 'summary-expense-screen', 'locationName', '{"de": "Deutschland", "en": "Germany"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (221, 'summary-expense-screen', 'messageHeading', '{"de": "Überschreitet den maximalen Betrag", "en": "Exceeds the maximum amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (222, 'summary-expense-screen', 'messageDescription', '{"de": "Ihr Beleg überschreitet den maximalen Betrag, der pro Begünstigten gemäß der Unternehmensspesenrichtlinie erlaubt ist.", "en": "Your receipt exceeds the maximum amount allowed per beneficiary as defined by the corporate expense policy."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (223, 'summary-expense-screen', 'messageTime', '{"de": "Vor 1h", "en": "1h ago"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (224, 'summary-expense-screen', 'checkboxTitle', '{"de": "Alle Daten sind Maecenas consequat arcu ac justo accumsan", "en": "All datas are Maecenas consequat arcu ac justo accumsan"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (225, 'summary-expense-screen', 'cancelButtonText', '{"de": "Abbrechen", "en": "Cancel"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (226, 'summary-expense-screen', 'saveButtonText', '{"de": "Speichern", "en": "Save"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (227, 'navbar-screen', 'dashboardTitle', '{"de": "Dashboard", "en": "Dashboard"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (228, 'navbar-screen', 'reportTitle', '{"de": "Bericht", "en": "Report"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (229, 'navbar-screen', 'inactiveTitle', '{"de": "Inaktiv", "en": "Inactive"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (230, 'navbar-screen', 'notificationTitle', '{"de": "Benachrichtigung", "en": "Notification"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (231, 'navbar-screen', 'settingsTitle', '{"de": "Einstellungen", "en": "Settings"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (232, 'navbar-screen', 'profileTitle', '{"de": "Profil", "en": "Profile"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (233, 'add-external-beneficiary-component', 'headerTitle', '{"de": "Externen Begünstigten Hinzufügen", "en": "Add External Beneficiary"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (234, 'add-external-beneficiary-component', 'title', '{"de": "Externer Begünstigter", "en": "External beneficiary"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (235, 'add-external-beneficiary-component', 'checkboxTitle', '{"de": "Gast wurde vom Partner/Ehepartner betreut", "en": "Guest was attended by partner / spouse"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (236, 'add-external-beneficiary-component', 'expenseCategoryTitle', '{"de": "Spesen Kategorie", "en": "Expense Category"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (237, 'add-external-beneficiary-component', 'expenseNatureTitle', '{"de": "Spesen Art", "en": "Expense Nature"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (238, 'add-external-beneficiary-component', 'placeholderText', '{"de": "Auswählen", "en": "Select"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (239, 'add-external-beneficiary-component', 'expenseTitle', '{"de": "Spesen Titel", "en": "Expense Title"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (240, 'add-external-beneficiary-component', 'expenseTitlePlaceholderText', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (241, 'add-external-beneficiary-component', 'backButtonText', '{"de": "Zurück", "en": "Back"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (242, 'add-external-beneficiary-component', 'nextButtonText', '{"de": "Weiter", "en": "Next"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (243, 'add-internal-beneficiary-component', 'headerTitle', '{"de": "Internen Begünstigten Hinzufügen", "en": "Add Internal Beneficiary"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (244, 'add-internal-beneficiary-component', 'title', '{"de": "Interner Begünstigter", "en": "Internal beneficiary"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (245, 'add-internal-beneficiary-component', 'expenseCategoryTitle', '{"de": "Spesen Kategorie", "en": "Expense Category"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (246, 'add-internal-beneficiary-component', 'expenseNatureTitle', '{"de": "Spesen Art", "en": "Expense Nature"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (247, 'add-internal-beneficiary-component', 'placeholderText', '{"de": "Auswählen", "en": "Select"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (248, 'add-internal-beneficiary-component', 'expenseTitle', '{"de": "Spesen Titel", "en": "Expense Title"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (249, 'add-internal-beneficiary-component', 'expenseTitlePlaceholderText', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (250, 'add-internal-beneficiary-component', 'backButtonText', '{"de": "Zurück", "en": "Back"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (251, 'add-internal-beneficiary-component', 'nextButtonText', '{"de": "Weiter", "en": "Next"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (252, 'add-beneficiaries-component', 'header.searchPlaceholderText', '{"de": "Suchen...", "en": "Search..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (253, 'add-beneficiaries-component', 'header.backAlt', '{"de": "Zurück", "en": "Back"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (254, 'add-beneficiaries-component', 'header.searchAlt', '{"de": "Suchen", "en": "Search"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (255, 'add-beneficiaries-component', 'header.filterAlt', '{"de": "Filter", "en": "Filter"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (256, 'add-beneficiaries-component', 'favouriteTab.title', '{"de": "Favoriten", "en": "Favorites"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (257, 'add-beneficiaries-component', 'favouriteTab.contentDescription', '{"de": "Inhalt kommt hier", "en": "Content comes here"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (258, 'add-beneficiaries-component', 'internalTab.title', '{"de": "Intern", "en": "Internal"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (259, 'add-beneficiaries-component', 'internalTab.contentDescription', '{"de": "Inhalt kommt hier", "en": "Content comes here"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (260, 'add-beneficiaries-component', 'externalTabTitle', '{"de": "Extern", "en": "External"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (261, 'add-beneficiaries-component', 'saveButtonText', '{"de": "Speichern", "en": "Save"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (262, 'add-beneficiaries-component', 'searchDialogRef.searchPlaceholderText', '{"de": "Suchen...", "en": "Search..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (263, 'add-beneficiaries-component', 'searchDialogRef.recentSearchText', '{"de": "Kürzliche Suche", "en": "Recent Search"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (264, 'add-beneficiaries-component', 'searchDialogRef.clearAllText', '{"de": "Alle Löschen", "en": "Clear All"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (265, 'add-beneficiaries-component', 'internalExternalDialogRef.internalTitle', '{"de": "Intern", "en": "Internal"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (266, 'add-beneficiaries-component', 'internalExternalDialogRef.externalTitle', '{"de": "Extern", "en": "External"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (267, 'add-beneficiaries-component', 'internalExternalDialogRef.saveButtonText', '{"de": "Speichern", "en": "Save"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (268, 'add-expense-component', 'header.title', '{"de": "Spesen Hinzufügen-1", "en": "Add Expense-1"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (269, 'add-expense-component', 'step1.breadcrumb1Title', '{"de": "Spesen Details", "en": "Expense Details"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (270, 'add-expense-component', 'step1.expenseCategoryTitle', '{"de": "Spesen Kategorie", "en": "Expense Category"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (271, 'add-expense-component', 'step1.firstFieldPlaceholderText', '{"de": "Auswählen", "en": "Select"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (272, 'add-expense-component', 'step1.expenseNatureTitle', '{"de": "Spesen Art", "en": "Expense Nature"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (273, 'add-expense-component', 'step1.secondFieldPlaceholderText', '{"de": "Auswählen", "en": "Select"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (274, 'add-expense-component', 'step1.expenseTitle', '{"de": "Spesen Titel", "en": "Expense Title"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (275, 'add-expense-component', 'step1.thirdFieldPlaceholderText', '{"de": "Eingeben", "en": "Enter"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (276, 'add-expense-component', 'step1.locationTitle', '{"de": "Standort", "en": "Location"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (277, 'add-expense-component', 'step1.fourthFieldPlaceholderText', '{"de": "Auswählen", "en": "Select"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (278, 'add-expense-component', 'step1.checkbox1Text', '{"de": "Mitarbeiter oder externe Gäste für diese Ausgabe beteiligt", "en": "Co-Workers or External Guests involved for this expense"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (279, 'add-expense-component', 'step1.addBeneficiariesTitle', '{"de": "Begünstigte Hinzufügen", "en": "Add Beneficiaries"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (280, 'add-expense-component', 'step1.beneficiariesListText', '{"de": "Begünstigten Liste", "en": "Beneficiaries List"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (281, 'add-expense-component', 'step1.externalTitle', '{"de": "Extern", "en": "External"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (282, 'add-expense-component', 'step1.internalTitle', '{"de": "Intern", "en": "Internal"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (283, 'add-expense-component', 'step1.checkbox2Text', '{"de": "Spesen vom Team-Budget abziehen", "en": "Deduct Expense from Team Budget"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (284, 'add-expense-component', 'step2.breadcrumb2Title', '{"de": "Beleg Hochladen", "en": "Receipt Upload"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (285, 'add-expense-component', 'step2.field1Title', '{"de": "Beleg Hochladen", "en": "Upload Receipt"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (286, 'add-expense-component', 'step2.helpText', '{"de": "Nur PNG, JPEG, PDF oder Word-Dateien erlaubt, Größe (Bis 1 MB)", "en": "PNG, JPEG, PDF or Word File only allow, Size (Upto 1 MB)"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (287, 'add-expense-component', 'step2.uploadText1', '{"de": "Hochladen", "en": "Upload"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (288, 'add-expense-component', 'step2.receiptText1', '{"de": "Beleg Erste Hälfte", "en": "Receipt First Half"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (289, 'add-expense-component', 'step2.uploadText2', '{"de": "Hochladen", "en": "Upload"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (290, 'add-expense-component', 'step2.receiptText2', '{"de": "Beleg Fortsetzung", "en": "Receipt Continuation"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (291, 'add-expense-component', 'step2.receiptText3', '{"de": "Beleg Zweite Hälfte", "en": "Receipt Second Half"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (292, 'add-expense-component', 'step2.optionalText', '{"de": "Optional", "en": "Optional"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (293, 'add-expense-component', 'step2.helpScreen.description', '{"de": "Tippen Sie unten, um ein Foto Ihres Belegs hochzuladen. Wischen Sie für Tipps und Anleitungen, wie Sie ein Bild Ihres Belegs machen.", "en": "Tap below to upload a photo of your receipt. Swipe for tips and guidelines for taking a picture of your receipt."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (294, 'add-expense-component', 'step3.breadcrumb3Title', '{"de": "Spesendaten", "en": "Expense Data"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (295, 'add-expense-component', 'step3.field1Title', '{"de": "Spesendatum", "en": "Expense Date"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (296, 'add-expense-component', 'step3.sectionTitle', '{"de": "Spesendetails", "en": "Expense details"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (297, 'add-expense-component', 'step3.totalAmountFieldTitle', '{"de": "Gesamtbetrag Bezahlt", "en": "Total Amount Paid"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (298, 'add-expense-component', 'step3.totalAmountFieldplaceholder', '{"de": "Eingeben", "en": "Enter"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (299, 'add-expense-component', 'step3.gratuityFieldTitle', '{"de": "Trinkgeld", "en": "Gratuity"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (300, 'add-expense-component', 'step3.gratuityFieldPlaceholder', '{"de": "Eingeben", "en": "Enter"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (301, 'add-expense-component', 'step3.grossAmountTitle', '{"de": "Bruttobetrag", "en": "Gross Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (302, 'add-expense-component', 'step3.netAmountTitle', '{"de": "NET Betrag", "en": "NET Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (303, 'add-expense-component', 'step3.vatAmountTitle', '{"de": "MwSt. Betrag", "en": "VAT Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (304, 'add-expense-component', 'step3.vatOnText1', '{"de": "MwSt. auf", "en": "VAT on"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (305, 'add-expense-component', 'step3.vatOnText2', '{"de": "MwSt. auf", "en": "VAT on"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (306, 'add-expense-component', 'step3.totalsTitle', '{"de": "Gesamt", "en": "Totals"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (307, 'add-expense-component', 'step3.totalCalculationTitle', '{"de": "Berechneter Gesamtbetrag", "en": "Total Amount calculated"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (308, 'add-expense-component', 'step3.checkboxTitle', '{"de": "Keine Erstattung", "en": "No Reimbursed"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (309, 'add-expense-component', 'step3.checkboxText', '{"de": "Ich habe einen oder mehrere Artikel für meinen persönlichen Gebrauch gekauft", "en": "I bought one or more items for my personal use"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (310, 'add-expense-component', 'step3.checkboxText2', '{"de": "Alle Begünstigten erhielten unterschiedliche Leistungen/Geschenke/Bewirtungen.", "en": "All beneficiaries received different value on benefits / gifts / hospitality"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (311, 'add-expense-component', 'step4.breadcrumb4Title', '{"de": "Posten Hinzufügen", "en": "Add Itemisation"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (312, 'add-expense-component', 'step4.internalBeneficiaryTitle', '{"de": "Persönlicher Gebrauch: Artikel", "en": "Personal Use: Item"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (313, 'add-expense-component', 'step4.itemTitle1', '{"de": "Artikelname", "en": "Item Name"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (314, 'add-expense-component', 'step4.itemPlaceholder1', '{"de": "Espresso Macchiato", "en": "Espresso Macchiato"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (315, 'add-expense-component', 'step4.itemisationTitle1', '{"de": "Betrag", "en": "Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (316, 'add-expense-component', 'step4.itemTitle2', '{"de": "Artikelname", "en": "Item Name"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (317, 'add-expense-component', 'step4.itemPlaceholder2', '{"de": "Eingeben", "en": "Enter"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (318, 'add-expense-component', 'step4.itemisationTitle2', '{"de": "Betrag", "en": "Amount"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (319, 'add-expense-component', 'step4.selectBeneficiariesTitle', '{"de": "Begünstigte Auswählen", "en": "Select Beneficiaries"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (320, 'add-expense-component', 'step4.addItemText', '{"de": "Artikel Hinzufügen", "en": "Add Item"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (321, 'add-expense-component', 'step4.paidAmountTitle', '{"de": "Bezahlt Betrag", "en": "Amount Paid"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (322, 'add-expense-component', 'step4.claimAmountTitle', '{"de": "Betrag Erstatten", "en": "Amount to Claim"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (323, 'add-expense-component', 'backButtonText', '{"de": "Zurück", "en": "Back"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (324, 'add-expense-component', 'nextButtonText', '{"de": "Weiter", "en": "Next"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (325, 'add-expense-component', 'selectBeneficiariesDialogRef.externalTitle', '{"de": "Extern", "en": "External"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (326, 'add-expense-component', 'selectBeneficiariesDialogRef.internalTitle', '{"de": "Intern", "en": "Internal"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (327, 'add-expense-component', 'selectBeneficiariesDialogRef.doneButtonText', '{"de": "Fertig", "en": "Done"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (328, 'co-workers-component', 'header.placeholderText', '{"de": "Suche...", "en": "Search..."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (329, 'co-workers-component', 'picklistTab.tabButtonText', '{"de": "Auswahlliste", "en": "Pick List"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (330, 'co-workers-component', 'picklistTab.createPicklistButtonText', '{"de": "Auswahlliste erstellen", "en": "Create Pick List"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (331, 'co-workers-component', 'picklistTab.cancelButtonText', '{"de": "Abbrechen", "en": "Cancel"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (332, 'co-workers-component', 'picklistTab.nextButtonText', '{"de": "Weiter", "en": "Next"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (333, 'co-workers-component', 'internalTab.tabButtonText', '{"de": "Intern", "en": "Internal"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (334, 'co-workers-component', 'internalTab.firstAccordionTitle', '{"de": "Favoriten", "en": "Favorites"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (335, 'co-workers-component', 'externalTab.tabButtonText', '{"de": "Extern", "en": "External"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (336, 'co-workers-component', 'externalTab.addExternalButtonText', '{"de": "Extern hinzufügen", "en": "Add External"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (337, 'co-workers-component', 'externalTab.saveButtonText', '{"de": "Speichern", "en": "Save"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (338, 'co-workers-component', 'picklistDialogScreen.titleText', '{"de": "Auswahlliste erstellen", "en": "Create Pick List"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (339, 'co-workers-component', 'picklistDialogScreen.fieldText', '{"de": "Name der Auswahlliste", "en": "Pick List Name"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (340, 'co-workers-component', 'picklistDialogScreen.fieldPlaceholder', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (341, 'co-workers-component', 'picklistDialogScreen.addBeneficiariesButtonText', '{"de": "Begünstigte hinzufügen", "en": "Add Beneficiaries"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (342, 'co-workers-component', 'picklistDialogScreen.createButtonText', '{"de": "Erstellen", "en": "Create"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (343, 'co-workers-component', 'successfullyCreatedScreen.title', '{"de": "Erfolgreich erstellt", "en": "Successfully Created"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (344, 'co-workers-component', 'successfullyCreatedScreen.description', '{"de": "Sie haben erfolgreich die erste Auswahlliste erstellt.", "en": "You have successfully created first pick list."}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (345, 'co-workers-component', 'successfullyCreatedScreen.createPicklistButtonText', '{"de": "Auswahlliste erstellen", "en": "Create Pick List"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (346, 'co-workers-component', 'successfullyCreatedScreen.doLaterButtonText', '{"de": "Später erledigen", "en": "Ill do Later"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (347, 'dashboard-screen-component', 'reportTitle', '{"de": "Erstellen Sie Ihren ersten Spesenbericht", "en": "Create Your First Expense Report"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (348, 'dashboard-screen-component', 'reportParagraphFirst', '{"de": "Tippen Sie auf das ", "en": "Tap the "}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (349, 'dashboard-screen-component', 'reportParagraphSecond', '{"de": "Symbol unten, und wir kümmern uns um den Rest", "en": " icon below and we ll take care of everything else"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (350, 'dashboard-screen-component', 'travelTitle', '{"de": "Reisen", "en": "Travel"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (351, 'dashboard-screen-component', 'eventsTitle', '{"de": "Veranstaltungen", "en": "Events"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (352, 'dashboard-screen-component', 'totalTitle', '{"de": "Gesamt", "en": "Total"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (353, 'dashboard-screen-component', 'expenseTitle', '{"de": "Ausgaben", "en": "Expense"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (354, 'dashboard-screen-component', 'hospitalityTitle', '{"de": "Gastfreundschaft", "en": "Hospitality"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (355, 'dashboard-screen-component', 'giftsTitle', '{"de": "Geschenke", "en": "Gifts"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (356, 'dashboard-screen-component', 'expenseReportTitle', '{"de": "Mein Ausgabenbericht", "en": "My Expense Report"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (357, 'dashboard-screen-component', 'viewAllText', '{"de": "Alle anzeigen", "en": "View All"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (358, 'dashboard-screen-component', 'travelToVeniceText', '{"de": "Reise nach Venedig", "en": "Travel to Venice"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (359, 'dashboard-screen-component', 'approvedText', '{"de": "Genehmigt", "en": "Approved"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (360, 'dashboard-screen-component', 'giftText', '{"de": "Geschenk an den Kunden", "en": "Gift to Client"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (361, 'dashboard-screen-component', 'rejectedText', '{"de": "Abgelehnt", "en": "Rejected"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (362, 'dashboard-screen-component', 'travelText', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (363, 'dashboard-screen-component', 'reviewText', '{"de": "Überprüfung", "en": "Review"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (364, 'dashboard-screen-component', 'reportTitle1', '{"de": "Reise nach Venedig", "en": "Travel to Venice"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (365, 'dashboard-screen-component', 'reportTitle2', '{"de": "Genehmigt", "en": "Approved"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (366, 'dashboard-screen-component', 'reportTitle3', '{"de": "Geschenk an den Kunden", "en": "Gift to Client"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (367, 'dashboard-screen-component', 'reportTitle4', '{"de": "Abgelehnt", "en": "Rejected"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (368, 'dashboard-screen-component', 'reportTitle5', '{"de": "Geschenk an den Kunden", "en": "Gift to Client"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (369, 'dashboard-screen-component', 'reportTitle6', '{"de": "Abgelehnt", "en": "Rejected"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (370, 'dashboard-screen-component', 'header.title', '{"de": "Dashboard", "en": "Dashboard"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (371, 'dashboard-screen-component', 'overview.claimTitle', '{"de": "Gesamtforderungen", "en": "Total Claims"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (372, 'dashboard-screen-component', 'overview.submittedTitle', '{"de": "Gesamt eingereicht", "en": "Total Submitted"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (373, 'dashboard-screen-component', 'overview.reviewTitle', '{"de": "In Überprüfung", "en": "Under Review"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (374, 'dashboard-screen-component', 'overview.rejectTitle', '{"de": "Anzahl abgelehnt", "en": "No. of Rejected"}', '2025-10-22 11:51:29.548')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (375, 'global-login', 'changePassword', '{"de": "Passwort ändern", "en": "Change Password"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (376, 'global-login', 'logout', '{"de": "Abmelden", "en": "Logout"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (377, 'dashboard-screen-component', 'favouriteBeneficiaries', '{"de": "Favoriten", "en": "Favourite Beneficiaries"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (378, 'dashboard-screen-component', 'teamBudget', '{"de": "Team-Budget", "en": "Team Budget"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (379, 'dashboard-screen-component', 'myManagers', '{"de": "Meine Manager", "en": "My Managers"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (380, 'add-deputy-screen', 'heading', '{"de": "Stellvertreter hinzufügen", "en": "Add Deputy"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (381, 'add-deputy-screen', 'shareToDeputy', '{"de": "Mit Stellvertreter teilen", "en": "Share to Deputy"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (382, 'manager-profile-screen', 'myDepartment', '{"de": "Meine Abteilung", "en": "My Department"}', '2025-10-22 13:18:35.585')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (389, 'dashboard-screen-component', 'notAllocated', '{"de": "Nicht zugewiesen", "en": "Not Allocated"}', '2025-10-22 13:29:27.417')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (390, 'dashboard-screen-component', 'usedAmount', '{"de": "Verwendeter Betrag", "en": "Used Amount"}', '2025-10-22 13:29:27.417')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (391, 'dashboard-screen-component', 'balanceAmount', '{"de": "Restbetrag", "en": "Balance Amount"}', '2025-10-22 13:29:27.417')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (392, 'deputy-profile-screen', '1hAgo', '{"de": "Vor 1 Stunde", "en": "1h ago"}', '2025-10-22 13:29:27.417')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (393, 'add-beneficiaries-component', 'myDepartment', '{"de": "Meine Abteilung", "en": "My Department"}', '2025-10-22 13:29:27.417')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (394, 'dashboard-screen-component', 'myDeputies', '{"de": "Meine Stellvertreter", "en": "My Deputies"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (395, 'add-deputy-screen', 'teamOuting', '{"de": "Teamausflug", "en": "Team Outing"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (396, 'add-deputy-screen', 'italyTrip', '{"de": "Italienreise", "en": "Italy Trip"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (397, 'add-deputy-screen', 'deputyAssignedText1', '{"de": "Sie wurden als Stellvertreter für", "en": "You have been assigned as the deputy for"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (398, 'add-deputy-screen', 'deputyAssignedText2', '{"de": "Nun können Sie Erstattungen im Namen Ihres Managers einreichen.", "en": "Now you can submit reimbursement on behalf of your manager."}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (399, 'add-deputy-screen', 'addDeputyDescription', '{"de": "Sie können Ihren Stellvertreter hinzufügen, um Ihre Spesenabrechnungen zu verwalten", "en": "You can add your deputy to manage your expense reports"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (400, 'add-deputy-screen', 'addDeputy', '{"de": "Stellvertreter hinzufügen", "en": "Add Deputy"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (401, 'add-deputy-screen', 'createEditSubmit', '{"de": "Erstellen/Bearbeiten", "en": "Create/Edit"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (402, 'add-deputy-screen', 'hospitalityTeam', '{"de": "Gastfreundschaft | Teammeeting", "en": "Hospitality | Team Meeting"}', '2025-10-22 13:46:05.323')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (412, 'add-deputy-screen', 'save', '{"de": "Speichern", "en": "Save"}', '2025-10-22 13:51:56.604')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (413, 'add-deputy-screen', 'pickListNamePlaceholder', '{"de": "Kundenmeeting Mailand", "en": "Client Meeting Milano"}', '2025-10-22 13:51:56.604')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations, created_at) VALUES (414, 'add-deputy-screen', 'cancel', '{"de": "Abbrechen", "en": "Cancel"}', '2025-10-22 13:51:56.604')
    ON CONFLICT ON CONSTRAINT translations_component_key_key
    DO UPDATE SET translations = EXCLUDED.translations^;

INSERT INTO rexbase.translations (id, component, key, translations) VALUES (415, 'dashboard-screen-component', 'invoicesTitle',          '{"en": "Invoices Awaiting Processing", "de": "Rechnungen zur Bearbeitung"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (416, 'dashboard-screen-component', 'workspaceReceiptsTitle', '{"en": "Receipts Awaiting Approval", "de": "Belege zur Genehmigung"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (417, 'dashboard-screen-component', 'manageReceiptsTitle',    '{"en": "Receipts Submitted for Approval", "de": "Eingereichte Belege"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (418, 'dashboard-screen-component', 'vendorName',             '{"en": "Vendor Name", "de": "Lieferantenname"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (419, 'dashboard-screen-component', 'invoiceTotal',           '{"en": "Invoice Total", "de": "Rechnungssumme"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (420, 'dashboard-screen-component', 'status',                 '{"en": "Status", "de": "Status"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (421, 'dashboard-screen-component', 'receiptTitle',           '{"en": "Receipt Title", "de": "Belegtitel"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (422, 'dashboard-screen-component', 'amount',                 '{"en": "Amount", "de": "Betrag"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (423, 'dashboard-screen-component', 'category',               '{"en": "Category", "de": "Kategorie"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (424, 'dashboard-screen-component', 'eventName',              '{"en": "Event Name", "de": "Veranstaltungsname"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;
INSERT INTO rexbase.translations (id, component, key, translations) VALUES (425, 'dashboard-screen-component', 'eventDate',              '{"en": "Event Date", "de": "Veranstaltungsdatum"}') ON CONFLICT ON CONSTRAINT translations_component_key_key DO UPDATE SET translations = EXCLUDED.translations^;

SELECT setval('rexbase.translations_id_seq', 425, true)^;
