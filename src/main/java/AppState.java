import java.io.File;
import java.util.List;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AppState {

    private String encryptedKey1;       // encryptedKey1 = encrypt(sharedKey1, answer1), and sharedKey1 = scheme.split(secret)[0] or decrypt(encryptedKey1, answer1)
    private String encryptedKey2;       // encryptedKey2 = encrypt(sharedKey2, answer2), and sharedKey2 = scheme.split(secret)[1] or decrypt(encryptedKey2, answer3)
    private String encryptedKey3;       // encryptedKey3 = encrypt(sharedKey3, answer3), and sharedKey3 = scheme.split(secret)[2] or decrypt(encryptedKey3, answer3)
    private String encryptedContent;    // encryptedContent = encrypt(content, secret)
    private boolean canDecrypt;
    private boolean canSave;

    /* called when a blank file is created */
    public AppState() {
        this.encryptedKey1 = "not yet ready";
        this.encryptedKey2 = "not yet ready";
        this.encryptedKey3 = "not yet ready";
        this.encryptedContent = "not yet ready";
        this.canDecrypt = false;
        this.canSave = true;
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
        return this.canSave && (numOfNonEmptyAnswers >= 3) && (secretText.getText().length() > 0);
    }

    /* called when we open and read an encrypted file */
    public void readFromFile(File file, List<JTextField> questionList, JTextArea secretText) {
        questionList.get(0).setText("read question1 from file");
        questionList.get(1).setText("read question2 from file");
        questionList.get(2).setText("read question3 from file");
        this.encryptedKey1 = "read encryptedKey1 from file";
        this.encryptedKey2 = "read encryptedKey2 from file";
        this.encryptedKey3 = "read encryptedKey3 from file";
        this.encryptedContent = "read encryptedContent from file";
        this.canDecrypt = true;
        this.canSave = false;
        secretText.setText(String.format("answer the questions to decrypt and see the content saved in %s", file.getAbsolutePath()));
    }

    /* called when we want to encrypt and save the file */
    public void saveToFile(File file, List<JTextField> questionList, List<JPasswordField> answerList, JTextArea secretText) {
        // save questionList.get(0) to file
        // save questionList.get(1) to file
        // save questionList.get(2) to file
        String secret = EncryptionManager.generateRandomString();
        List<String> threeShares = EncryptionManager.getThreeSplitShares(secret);
        this.encryptedKey1 = EncryptionManager.encrypt(threeShares.get(0), String.valueOf(answerList.get(0).getPassword()));
        this.encryptedKey2 = EncryptionManager.encrypt(threeShares.get(1), String.valueOf(answerList.get(1).getPassword()));
        this.encryptedKey3 = EncryptionManager.encrypt(threeShares.get(2), String.valueOf(answerList.get(2).getPassword()));
        this.encryptedContent = EncryptionManager.encrypt(secretText.getText(), secret);
        this.canDecrypt = true;
        this.canSave = false;
        secretText.setText(String.format("answer the questions to decrypt and see the content saved in %s", file.getAbsolutePath()));
    }

    /* called when decrypt is clicked */
    public void decrypt(List<JPasswordField> answerList, JTextArea secretText) {
        String sharedKey1 = answerList.get(0).getPassword().length > 0 ? EncryptionManager.decrypt(this.encryptedKey1, String.valueOf(answerList.get(0).getPassword())) : null;
        String sharedKey2 = answerList.get(1).getPassword().length > 0 ? EncryptionManager.decrypt(this.encryptedKey2, String.valueOf(answerList.get(1).getPassword())) : null;
        String sharedKey3 = answerList.get(2).getPassword().length > 0 ? EncryptionManager.decrypt(this.encryptedKey3, String.valueOf(answerList.get(2).getPassword())) : null;
        int idA = sharedKey1 != null ? 1 : 3;
        int idB = sharedKey2 != null ? 2 : 3;
        String shareA = sharedKey1 != null ? sharedKey1 : sharedKey3;
        String shareB = sharedKey2 != null ? sharedKey2 : sharedKey3;
        String secret = EncryptionManager.computeSecretFromShares(idA, shareA, idB, shareB);
        this.canDecrypt = false;
        this.canSave = true;
        secretText.setText(EncryptionManager.decrypt(this.encryptedContent, secret));
    }

}
