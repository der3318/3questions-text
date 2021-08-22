import java.io.File;
import java.util.List;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AppState {

    private String encryptedKey1;       // encryptedKey1 = encrypt(sharedKey1, answer1)
    private String encryptedKey2;       // encryptedKey2 = encrypt(sharedKey2, answer2)
    private String encryptedKey3;       // encryptedKey3 = encrypt(sharedKey3, answer3)
    private String sharedKey1;          // sharedKey1 = scheme.split(secret)[0] or decrypt(encryptedKey1, answer1)
    private String sharedKey2;          // sharedKey2 = scheme.split(secret)[1] or decrypt(encryptedKey2, answer3)
    private String sharedKey3;          // sharedKey3 = scheme.split(secret)[2] or decrypt(encryptedKey3, answer3)
    private String encryptedContent;    // encryptedContent = encrypt(content, secret)
    private boolean canDecrypt;
    private boolean canSave;
    private boolean isBlankFile;

    /* called when a blank file is created */
    public AppState() {
        this.encryptedKey1 = "not yet ready";
        this.encryptedKey2 = "not yet ready";
        this.encryptedKey3 = "not yet ready";
        this.sharedKey1 = "randomly get one using scheme.split(secret)[0]";
        this.sharedKey2 = "randomly get one using scheme.split(secret)[1]";
        this.sharedKey3 = "randomly get one using scheme.split(secret)[2]";
        this.encryptedContent = "not yet ready";
        this.canDecrypt = false;
        this.canSave = true;
        this.isBlankFile = true;
    }

    public boolean canDecrypt(List<JPasswordField> answerList) {
        int numOfNonEmptyAnswers = 0;
        for (JPasswordField answer : answerList) {
            numOfNonEmptyAnswers += (answer.getPassword().length > 0 ? 1 : 0);
        }
        return this.canDecrypt && (numOfNonEmptyAnswers >= 2);
    }

    public boolean canSave(List<JPasswordField> answerList, JTextArea secretText) {
        int numOfNonEmptyAnswers = 0;
        for (JPasswordField answer : answerList) {
            numOfNonEmptyAnswers += (answer.getPassword().length > 0 ? 1 : 0);
        }
        return this.canSave && (numOfNonEmptyAnswers >= (this.isBlankFile ? 3 : 2)) && (secretText.getText().length() > 0);
    }

    /* called when we open and read an encrypted file */
    public void readFromFile(File file, List<JTextField> questionList, JTextArea secretText) {
        questionList.get(0).setText("read question1 from file");
        questionList.get(1).setText("read question2 from file");
        questionList.get(2).setText("read question3 from file");
        this.encryptedKey1 = "read encryptedKey1 from file";
        this.encryptedKey2 = "read encryptedKey2 from file";
        this.encryptedKey3 = "read encryptedKey3 from file";
        this.sharedKey1 = "not yet ready";
        this.sharedKey2 = "not yet ready";
        this.sharedKey3 = "not yet ready";
        this.encryptedContent = "read encryptedContent from file";
        this.canDecrypt = true;
        this.canSave = false;
        this.isBlankFile = false;
        secretText.setText(String.format("answer the questions to decrypt and see the content saved in %s", file.getAbsolutePath()));
    }

    /* called when we want to encrypt and save the file */
    public void saveToFile(File file, List<JTextField> questionList, List<JPasswordField> answerList, JTextArea secretText) {
        // save questionList.get(0) to file
        // save questionList.get(1) to file
        // save questionList.get(2) to file
        this.encryptedKey1 = "compute encrypt(sharedKey1 + answerList.get(0)) and save to file";
        this.encryptedKey2 = "compute encrypt(sharedKey2 + answerList.get(1)) and save to file";
        this.encryptedKey3 = "compute encrypt(sharedKey3 + answerList.get(2)) and save to file";
        // compute secret using scheme.join(sharedKey1, sharedKey2, sharedKey3);
        this.sharedKey1 = "cleaned up";
        this.sharedKey2 = "cleaned up";
        this.sharedKey3 = "cleaned up";
        this.encryptedContent = "compute encrypt(secretText, secret) and save to file";
        this.canDecrypt = true;
        this.canSave = false;
        this.isBlankFile = false;
        secretText.setText(String.format("answer the questions to decrypt and see the content saved in %s", file.getAbsolutePath()));
    }

    /* called when decrypt is clicked */
    public void decrypt(List<JPasswordField> answerList, JTextArea secretText) {
        this.encryptedKey1 = "untouched";
        this.encryptedKey2 = "untouched";
        this.encryptedKey3 = "untouched";
        this.sharedKey1 = "compute decrypt(encryptedKey1, answerList.get(0))";
        this.sharedKey2 = "compute decrypt(encryptedKey2, answerList.get(1))";
        this.sharedKey3 = "compute decrypt(encryptedKey3, answerList.get(2))";
        // compute secret using scheme.join(sharedKey1, sharedKey2, sharedKey3);
        this.encryptedContent = "untouched";
        this.canDecrypt = false;
        this.canSave = true;
        this.isBlankFile = false;
        secretText.setText("compute decrypt(encryptedContent, secret)");
    }

}
